package extension.io

import okhttp3.{Call, Callback, MediaType, OkHttpClient, Request, RequestBody, Response}

import java.io.IOException
import java.util.concurrent.CompletableFuture
import scala.concurrent.Future
import extension.concurrent.javaFuture2Scala

import java.net.{URI, URL}

object HTTP {

  implicit val defaultClient: OkHttpClient = new OkHttpClient()

  def client(implicit impl: OkHttpClient = defaultClient): OkHttpClient = impl

  def request(
      method:  String                 = "GET",
      uri:     URL,
      headers: List[(String, String)] = List(),
      body:    String                 = null
  ): Future[Response] = request(method, uri.toString, headers, body)

  def request(
      method:  String                 = "GET",
      uri:     URI,
      headers: List[(String, String)] = List(),
      body:    String                 = null
  ): Future[Response] = request(method, uri.toString, headers, body)

  def request(
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
    if (body != null) {
      val reqBody = RequestBody.create(body, MediaType.get(contentType))
      builder.method(method, reqBody)
    }
    val req = builder.build()

    val frsp = new CompletableFuture[Response]()
    client
      .newCall(req)
      .enqueue(new Callback() {
        def onFailure(call: Call, e: IOException): Unit = {
          frsp.completeExceptionally(e)
        }

        def onResponse(call: Call, rsp: Response): Unit = {
          frsp.complete(rsp)
        }
      })
    frsp
  }

}
