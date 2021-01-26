package extension.io

import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.net.http.HttpResponse.{BodyHandler, BodyHandlers}
import java.net.URI
import scala.concurrent.Future
import extension.concurrent.javaFuture2Scala

object http {
  implicit val stringBodyHandler:    BodyHandler[String]      = BodyHandlers.ofString()
  implicit val byteArrayBodyHandler: BodyHandler[Array[Byte]] = BodyHandlers.ofByteArray()

  lazy final val defaultClient = HttpClient.newBuilder().build()

  def client(implicit impl: HttpClient = defaultClient): HttpClient = impl

  def request[T](
      method:  String                 = "GET",
      uri:     URI,
      headers: List[(String, String)] = List(),
      body:    String                 = null
  )(implicit
      bodyHandler: BodyHandler[T]
  ): Future[HttpResponse[T]] = {
    val rBody =
      if (body == null)
        HttpRequest.BodyPublishers.noBody()
      else
        HttpRequest.BodyPublishers.ofString(body)
    val builder = HttpRequest.newBuilder()
    builder.uri(uri).method(method, rBody)
    headers.foreach((builder.header _).tupled)
    client.sendAsync(builder.build(), bodyHandler)
  }
}
