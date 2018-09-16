/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-16
  */
package io.mango.services.macwire

import scala.reflect.{classTag, ClassTag}

import com.softwaremill.macwire.Wired
import io.mango.common.util.Check
import io.mango.services.ServiceLocator

class MacwireServiceLocator private(val wired: Wired) extends ServiceLocator {
  Check.argNotNull(wired, "wired")

  override def get[T: ClassTag]: Option[T] = {
    val services = getAll[T]
    Check.state(
      services.size < 2,
      s"unable to get ${classTag[T].runtimeClass.getName} service " +
      s"because found more than one instance with specified type"
    )
    services.headOption
  }

  override def get[T: ClassTag](key: Any): Option[T] = throw new UnsupportedOperationException

  override def getAll[T: ClassTag]: Seq[T] = wired.lookup(classTag[T].runtimeClass.asInstanceOf[Class[T]])
}

object MacwireServiceLocator {
  def apply(wired: Wired): MacwireServiceLocator = new MacwireServiceLocator(wired)
}
