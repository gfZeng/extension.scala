package extension.io

import extension.system.StringConversation
import extension.io.http.stringBodyHandler
import extension.logging.Log

import java.net.URLEncoder
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Notifier[T] {
  def notify(msg: T): Future[Boolean]
}

class Telegram extends Notifier[String] {
  private final val chatId = -1001229266534L //-380659103
  private final val url = {
    val token = "bot.token".prop
    s"https://api.telegram.org/bot${token}/sendMessage"
  }

  override def notify(msg: String): Future[Boolean] = {
    val escaped = URLEncoder.encode(msg, "UTF-8")
    val data    = s"chat_id=$chatId&text=$escaped&parse_mode=Markdown"
    val headers = List("Content-Type" -> "application/x-www-form-urlencoded")
    http
      .request[String](method = "POST", uri = url, headers = headers, body = data)
      .map { rsp =>
        true
      }
      .recover { e =>
        Log.error(e)("Uncaught Error")
        false
      }
  }
}
