package extension.io

import okhttp3._

import java.io.IOException
import scala.concurrent.{Future, Promise}

class HTTP(client: OkHttpClient = new OkHttpClient()) {

  @deprecated
  def request(
      method:  String                 = "GET",
      uri:     String,
      headers: List[(String, String)] = List(),
      body:    String                 = null
  ): Future[Response] = send(method, uri, headers, body)

  def send(
      method:  String                 = "GET",
      uri:     String,
      headers: List[(String, String)] = List(),
      body:    String                 = null
  ): Future[Response] = {
    val builder = new Request.Builder().url(uri)
    var contentType: String = null
    headers.foreach { head =>
      if (head._1.equalsIgnoreCase("content-type")) {
        contentType = head._2
      }
      builder.header(head._1, head._2)
    }
    val reqBody =
      if (body == null) null
      else
        RequestBody.create(body, MediaType.get(contentType))
    builder.method(method, reqBody)

    val req = builder.build()

    val frsp = Promise[Response]
    client
      .newCall(req)
      .enqueue(new Callback() {
        def onFailure(call: Call, e: IOException): Unit = {
          frsp.failure(e)
        }

        def onResponse(call: Call, rsp: Response): Unit = {
          frsp.success(rsp)
        }
      })
    frsp.future
  }

}

object HTTP extends HTTP(new OkHttpClient())
