package extension.io

import extension.system.StringConversation
import extension.logging.Log

import java.net.URLEncoder
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Notifier[T] {
  def notify(msg: T): Future[Boolean]
}

class Telegram extends Notifier[String] {
  private final val chatId = "chat.id".prop
  private final val url = {
    val token = "bot.token".prop
    s"https://api.telegram.org/bot${token}/sendMessage"
  }

  override def notify(msg: String): Future[Boolean] = {
    val escaped = URLEncoder.encode(msg, "UTF-8")
    val data    = s"chat_id=$chatId&text=$escaped&parse_mode=Markdown"
    val headers = List("Content-Type" -> "application/x-www-form-urlencoded")
    HTTP
      .request(method = "POST", uri = url, headers = headers, body = data)
      .map { rsp =>
        rsp.body().string()
        true
      }
      .recover { e =>
        Log.error(e)("Uncaught Error")
        false
      }
  }
}
