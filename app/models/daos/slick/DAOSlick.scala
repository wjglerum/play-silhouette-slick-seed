package models.daos.slick

import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import play.api.db.slick.HasDatabaseConfig

/**
 * Trait that contains generic slick db handling code to be mixed in with DAOs
 */
trait DAOSlick extends DBTableDefinitions with HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](play.api.Play.current)
  import driver.api._
}