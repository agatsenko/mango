/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-22
  */
package io.mango.common.resource

sealed case class ManagedResource4[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC] private[resource](
    private[resource] val r1: R1,
    private[resource] val r2: R2,
    private[resource] val r3: R3,
    private[resource] val r4: R4) extends ManagedResource {
  def using[T](f: (R1, R2, R3, R4) => T): Option[T] = if (isOpen) Some(f(r1, r2, r3, r4)) else None

  def and[R5 <: AC](r5: => R5): ManagedResource5[R1, R2, R3, R4, R5] = {
    newResource(ManagedResource5(r1, r2, r3, r4, r5))
  }

  override protected[resource] def getResources: Seq[AC] = Seq(r1, r2, r3, r4)
}
