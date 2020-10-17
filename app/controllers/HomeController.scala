package controllers

import javax.inject._
import models.PersonRepository
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

//noinspection TypeAnnotation
@Singleton
class HomeController @Inject()(cc: ControllerComponents, repo: PersonRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def appSummary = Action {
    Ok(Json.obj("content" -> "Scala Play React Seed!"))
  }

  def test = Action.async {
    repo.list().map { users =>
      Ok(users.toList.toString)
    }
  }

  def getAllUsers = Action.async { implicit request =>
    repo.list().map { users =>
      Ok(users.toList.toString)
    }
  }
}
