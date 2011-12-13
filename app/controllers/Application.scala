package controllers

import views._

import play.Logger
import play.api._
import play.api.ws._
import play.api.mvc._
import play.api.libs._
import play.api.libs.concurrent._
import play.api.libs.iteratee._
import com.ning.http.client.Realm.AuthScheme

import scala.collection.JavaConversions._

object Application extends Controller {

  // Application

  def index = Action {
      Ok(html.index())
  }

  def twitter(term:String) = Action { request =>
      val username:String = request.queryString.get("username").get.head
      val password:String = request.queryString.get("password").get.head


    Ok[String]((it:Iteratee[String, Unit]) => {
      WS.url("https://stream.twitter.com/1/statuses/filter.json?track=" + term)
        .auth(username, password, AuthScheme.BASIC)
        .get({ res:ResponseHeaders =>
            Enumeratee.map[Array[Byte]](bytes => new String(bytes)).transform(it)
        })
        ()
    }
    )

  }

}
/*
trait Secured extends Security.AllAuthenticated {
  override def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Twitter.index)
}
*/
