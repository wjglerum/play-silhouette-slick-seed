package models

import java.util.UUID

import com.mohiva.play.silhouette.api.Identity

case class User(id: UUID, name: String, email: String) extends Identity