/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-21
  */
package io.mango.common.resource

sealed case class ManagedResource3[R1 <: AC, R2 <: AC, R3 <: AC] private[resource](
    private[resource] val r1: R1,
    private[resource] val r2: R2,
    private[resource] val r3: R3) extends ManagedResource {
  def using[T](f: (R1, R2, R3) => T): Option[T] = if (isOpen) Some(f(r1, r2, r3)) else None

  def and[R4 <: AC](r4: => R4): ManagedResource4[R1, R2, R3, R4] = newResource(ManagedResource4(r1, r2, r3, r4))

  override protected[resource] def getResources: Seq[AC] = Seq(r1, r2, r3)
}