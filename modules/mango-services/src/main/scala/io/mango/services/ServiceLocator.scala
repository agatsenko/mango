/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-16
  */
package io.mango.services

import scala.reflect.ClassTag

trait ServiceLocator {
  def get[T: ClassTag]: Option[T]

  def get[T: ClassTag](key: Any): Option[T]

  def getAll[T: ClassTag]: Seq[T]
}
