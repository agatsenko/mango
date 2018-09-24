/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-21
  */
package io.mango.common.resource

sealed case class ManagedResource1[R <: AC] private[resource](private[resource] val r1: R) extends ManagedResource {
  def using[T](f: R => T): Option[T] = if (isOpened) Some(f(r1)) else None

  def and[R2 <: AC](r2: => R2): ManagedResource2[R, R2] = newResource(ManagedResource2(r1, r2))

  override protected[resource] def getResources: Seq[AC] = Seq(r1)
}
