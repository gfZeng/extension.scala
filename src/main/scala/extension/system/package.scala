package extension

import java.net.URL
import scala.util.Random

package object system {

  implicit final class StringConversation(s: String) {

    def resource: URL = Thread.currentThread().getContextClassLoader.getResource(s)

    def prop: String = {
      val p = System.getProperty(s)
      if (p == null) {
        val k = s.replace('.', '_').toUpperCase
        System.getenv(k)
      } else p
    }

    def prop(default: => String): String = {
      val v = this.prop
      if (v != null) v else default
    }

  }

  def randString(n: Int): String = {
    Random.alphanumeric.take(n).mkString("")
  }

  def halt(status: Int): Unit = halt(status)

  def nowMs():      Long = System.currentTimeMillis()
  def nowSeconds(): Long = System.currentTimeMillis() / 1000L

}
