package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class PersonRepository @Inject()(dbConfigProvider: DatabaseConfigProvider) extends Repository[Person, PersonForm](dbConfigProvider) {

  import dbConfig._
  import profile.api._

  override def create(form: PersonForm): Future[Person] = db.run {
    logger.info(s"Creating user: [$form]")
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    people.map(u => (u.name, u.age))
      .returning(people.map(_.id))
      .into((fields, id) => Person(id, fields._1, fields._2)) +=
      (form.name, form.age)
  }

  override def get(id: Long): Future[Person] = ???

  override def list(): Future[Seq[Person]] = db.run {
    logger.info("Getting all users")
    people.result
  }

  override def update(id: Long, entity: Person): Future[Int] = ???

  override def delete(id: Long): Future[Int] = ???
}
