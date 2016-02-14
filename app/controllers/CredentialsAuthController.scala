package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers._
import forms.SignInForm
import models.User
import models.services.UserService
import play.api.i18n.{Messages, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Action

import scala.concurrent.Future
import scala.language.postfixOps

/**
  * The credentials auth controller.
  *
  * @param messagesApi         The Play messages API.
  * @param env                 The Silhouette environment.
  * @param userService         The user service implementation.
  * @param credentialsProvider The credentials provider.
  */
class CredentialsAuthController @Inject()(val messagesApi: MessagesApi,
                                          val env: Environment[User, SessionAuthenticator],
                                          userService: UserService,
                                          credentialsProvider: CredentialsProvider)
  extends Silhouette[User, SessionAuthenticator] {

  /**
    * Authenticates a user against the credentials provider.
    *
    * @return The result to display.
    */
  def authenticate = Action.async { implicit request =>
    SignInForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.signIn(form))),
      data => {
        val credentials = Credentials(data.email, data.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          val result = Redirect(routes.ApplicationController.index())
          userService.retrieve(loginInfo).flatMap {
            case Some(user) =>
              env.authenticatorService.create(loginInfo).map {
                case authenticator => authenticator
              }.flatMap { authenticator =>
                env.eventBus.publish(LoginEvent(user, request, request2Messages))
                env.authenticatorService.init(authenticator).flatMap { v =>
                  env.authenticatorService.embed(v, result)
                }
              }
            case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
          }
        }.recover {
          case e: ProviderException =>
            Redirect(routes.ApplicationController.signIn()).flashing("error" -> Messages("invalid.credentials"))
        }
      }
    )
  }
}
