/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-22
  */
package io.mango.common.resource

sealed case class ManagedResource7[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, R5 <: AC, R6 <: AC, R7 <: AC] private[resource](
    private[resource] val r1: R1,
    private[resource] val r2: R2,
    private[resource] val r3: R3,
    private[resource] val r4: R4,
    private[resource] val r5: R5,
    private[resource] val r6: R6,
    private[resource] val r7: R7) extends ManagedResource {
  def using[T](f: (R1, R2, R3, R4, R5, R6, R7) => T): Option[T] = {
    if (isOpened) Some(f(r1, r2, r3, r4, r5, r6, r7)) else None
  }

  def and[R8 <: AC](r8: => R8): ManagedResource8[R1, R2, R3, R4, R5, R6, R7, R8] = {
    newResource(ManagedResource8(r1, r2, r3, r4, r5, r6, r7, r8))
  }

  override protected[resource] def getResources: Seq[AC] = Seq(r1, r2, r3, r4, r5, r6, r7)
}
