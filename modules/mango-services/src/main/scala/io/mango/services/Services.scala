/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-16
  */
package io.mango.services

import scala.reflect.ClassTag

import io.mango.common.util.Check

object Services extends ServiceLocator {
  @volatile private var _locator: ServiceLocator = _

  def getLocator: Option[ServiceLocator] = {
    val locator = _locator
    Option(locator)
  }

  def setLocator(locator: ServiceLocator): Unit = _locator = locator

  override def get[T: ClassTag]: Option[T] = checkIsLocatorDefined(getLocator).get[T]

  override def get[T: ClassTag](key: Any): Option[T] = checkIsLocatorDefined(getLocator).get[T](key)

  override def getAll[T: ClassTag]: Seq[T] = checkIsLocatorDefined(getLocator).getAll[T]

  private def checkIsLocatorDefined(locatorOpt: Option[ServiceLocator]): ServiceLocator = {
    Check.state(locatorOpt.isDefined, "locator is not defined")
    locatorOpt.get
  }
}
