/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-21
  */
package io.mango.common.resource

sealed case class ManagedResource2[R1 <: AC, R2 <: AC] private[resource](
    private[resource] val r1: R1,
    private[resource] val r2: R2) extends ManagedResource {
  def using[T](f: (R1, R2) => T): Option[T] = if (isOpened) Some(f(r1, r2)) else None

  def and[R3 <: AC](r3: => R3): ManagedResource3[R1, R2, R3] = newResource(ManagedResource3(r1, r2, r3))

  override protected[resource] def getResources: Seq[AC] = Seq(r1, r2)
}