/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-22
  */
package io.mango.common.resource

sealed case class ManagedResource5[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, R5 <: AC] private[resource](
    private[resource] val r1: R1,
    private[resource] val r2: R2,
    private[resource] val r3: R3,
    private[resource] val r4: R4,
    private[resource] val r5: R5) extends ManagedResource {
  def using[T](f: (R1, R2, R3, R4, R5) => T): Option[T] = if (isOpen) Some(f(r1, r2, r3, r4, r5)) else None

  def and[R6 <: AC](r6: => R6): ManagedResource6[R1, R2, R3, R4, R5, R6] = {
    newResource(ManagedResource6(r1, r2, r3, r4, r5, r6))
  }

  override protected[resource] def getResources: Seq[AC] = Seq(r1, r2, r3, r4, r5)
}
