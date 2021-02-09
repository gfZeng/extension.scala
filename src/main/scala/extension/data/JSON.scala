package extension.data

import org.json4s.{DefaultFormats, Formats, JString, MappingException, Reader}
import org.json4s.native.{JsonMethods, Serialization}

import java.io.InputStream

object JSON extends {

  type JValue = org.json4s.JValue

  object DefaultReaders extends org.json4s.DefaultReaders {

    implicit object DecimalReader extends Reader[BigDecimal] {
      def read(value: JValue): BigDecimal =
        value match {
          case JString(s) => BigDecimal(s)
          case x          => BigDecimalReader.read(x)
        }
    }
  }

  implicit val formats: Formats = DefaultFormats

  def parse(s: String): JValue = JsonMethods.parse(s)

  def parse(in: InputStream): JValue = JsonMethods.parse(in)

  def read[T](s: String)(implicit mf: scala.reflect.Manifest[T]): T = {
    Serialization.read[T](s)
  }

  def write[T](x: T): String = {
    Serialization.write[T](x)
  }
}
