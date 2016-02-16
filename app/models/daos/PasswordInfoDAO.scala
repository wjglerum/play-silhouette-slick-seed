package models.daos

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

class PasswordInfoDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends DelegableAuthInfoDAO[PasswordInfo] with DAOSlick {

  import driver.api._

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    val query = passwords.filter(_.email === loginInfo.providerKey)
    db.run(query.result.headOption).map { dbPasswordInfoOption =>
      dbPasswordInfoOption.map(dbPasswordInfo =>
        PasswordInfo(dbPasswordInfo.hash, dbPasswordInfo.password, dbPasswordInfo.salt))
    }
  }

  def add(loginInfo: LoginInfo, authInfo: PasswordInfo) = save(loginInfo, authInfo)

  def update(loginInfo: LoginInfo, authInfo: PasswordInfo) = save(loginInfo, authInfo)

  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    val dbPasswordInfo = DBPasswordInfo(authInfo.hasher, authInfo.password, authInfo.salt, loginInfo.providerKey)
    val query = passwords.insertOrUpdate(dbPasswordInfo)
    db.run(query).map(_ => authInfo)
  }

  def remove(loginInfo: LoginInfo): Future[Unit] = {
    val query = passwords.filter(_.email === loginInfo.providerKey)
    db.run(query.delete).map(_ => ())
  }
}