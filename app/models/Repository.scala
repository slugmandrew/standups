package models

import java.time.ZoneId

import org.h2.engine.User
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.util.Logging

import scala.concurrent.Future

/** Base Repository Pattern
  *
  * @tparam T the type of entity this repository relates to
  * @tparam F the form relating to the type T
  */
abstract class Repository[T <: HasId, F](dbConfigProvider: DatabaseConfigProvider) extends Logging {

  // We want the JdbcProfile for this provider
  protected val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.

  import dbConfig._
  import profile.api._

  implicit def utilDateToSqlDate(utilDate: java.util.Date): java.sql.Date = new java.sql.Date(utilDate.getTime)

  implicit def sqlDateToUtilDate(sqlDate: java.sql.Date): java.util.Date = new java.util.Date(sqlDate.getTime)

  // ========== TABLES ========== //

  // These are here because they need to be in the same scope, so we can link using foreign keys

  protected class PersonTable(tag: Tag) extends Table[Person](tag, "person") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def age = column[Int]("age")

    /** This is the tables default "projection".
      *
      * It defines how the columns are converted to and from the Person object.
      *
      * In this case, we are simply passing the id, name and page parameters to the Person case classes
      * apply and unapply methods.
      */
    def * = (id, name, age) <> ((Person.apply _).tupled, Person.unapply)
  }

  object DateMapper {

    val utilDateToLocalDate = MappedColumnType.base[java.util.Date, java.time.LocalDate](
      { utilDate => utilDate.toInstant.atZone(ZoneId.systemDefault()).toLocalDate },
      { localDate => java.util.Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant) })

    val utilDateToSqlTimestamp = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
      { utilDate => new java.sql.Timestamp(utilDate.getTime) },
      { sqlTimestamp => new java.util.Date(sqlTimestamp.getTime) })

    val utilDateToSqlDate = MappedColumnType.base[java.util.Date, java.sql.Date](
      { utilDate => new java.sql.Date(utilDate.getTime) },
      { sqlDate => new java.util.Date(sqlDate.getTime) })

  }

  protected lazy val people = TableQuery[PersonTable]

  // ========== CRUD METHODS ========== //

  // create
  def create(form: F): Future[T]

  // read
  def get(id: Long): Future[T]

  // read
  def list(): Future[Seq[T]]

  // update
  def update(id: Long, entity: T): Future[Int]

  // delete
  def delete(id: Long): Future[Int]
}
