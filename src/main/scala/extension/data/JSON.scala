package extension.data

import extension.data.JSON.codec
import org.json4s.{
  CustomSerializer,
  DefaultFormats,
  DefaultReaders,
  Formats,
  JDecimal,
  JString,
  Reader,
  Serializer
}
import org.json4s.ext.EnumNameSerializer
import org.json4s.native.{JsonMethods, Serialization}

import java.io.InputStream

class JSON(
    codecs: Iterable[JSON#Codec[_]] = List()
) extends DefaultReaders {

  type JValue   = org.json4s.JValue
  type Codec[T] = Serializer[T]

  implicit object DecimalReader extends Reader[BigDecimal] {
    def read(value: JValue): BigDecimal =
      value match {
        case JString(s) => BigDecimal(s)
        case x          => BigDecimalReader.read(x)
      }
  }

  object BigDecimalSerializer
      extends CustomSerializer[BigDecimal]({ _ =>
        (
          { case x: JValue => DecimalReader.read(x) },
          { case x: BigDecimal => JDecimal(x) }
        )
      })

  implicit val formats: Formats = DefaultFormats + BigDecimalSerializer ++ codecs

  def parse(s: String): JValue = JsonMethods.parse(s, useBigDecimalForDouble = true)

  def parse(in: InputStream): JValue = JsonMethods.parse(in, useBigDecimalForDouble = true)

  def read[T](s: String)(implicit mf: scala.reflect.Manifest[T]): T = {
    Serialization.read[T](s)
  }

  def write[T](x: T): String = {
    Serialization.write[T](x)
  }
}

object JSON extends JSON(codecs = List()) {

  def codec(e: Enumeration) = new EnumNameSerializer(e)

}
