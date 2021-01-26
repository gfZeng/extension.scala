package extension

object Launch {

  def main(args: Array[String]): Unit = {
    val obj_method = args(0).split("/")
    val obj        = obj_method(0)
    val method = if (obj_method.size == 1) {
      "main"
    } else {
      obj_method(1)
    }

    val clazz      = Class.forName(obj)
    val instance   = clazz.getDeclaredConstructor().newInstance()
    val parameters = args.drop(1)

    (parameters.isEmpty && (try {
      val fn = clazz.getDeclaredMethod(method)
      fn.invoke(instance)
      true
    } catch {
      case e: NoSuchMethodException => false
    })) || (try {
      val fn = clazz.getDeclaredMethod(method, classOf[String])
      fn.invoke(instance, parameters(0))
      true
    } catch {
      case e: NoSuchMethodException => false
    }) || {
      val fn = clazz.getDeclaredMethod(method, classOf[Array[String]])
      fn.invoke(instance, parameters)
      true
    }
  }
}
