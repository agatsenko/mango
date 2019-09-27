/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-22
  */
package io.mango.common.resource

sealed case class ManagedResource8[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, R5 <: AC, R6 <: AC, R7 <: AC, R8 <: AC] private[resource](
    private[resource] val r1: R1,
    private[resource] val r2: R2,
    private[resource] val r3: R3,
    private[resource] val r4: R4,
    private[resource] val r5: R5,
    private[resource] val r6: R6,
    private[resource] val r7: R7,
    private[resource] val r8: R8) extends ManagedResource {
  def using[T](f: (R1, R2, R3, R4, R5, R6, R7, R8) => T): Option[T] = {
    if (isOpen) Some(f(r1, r2, r3, r4, r5, r6, r7, r8)) else None
  }

  def and[R9 <: AC](r9: => R9): ManagedResource9[R1, R2, R3, R4, R5, R6, R7, R8, R9] = {
    newResource(ManagedResource9(r1, r2, r3, r4, r5, r6, r7, r8, r9))
  }

  override protected[resource] def getResources: Seq[AC] = Seq(r1, r2, r3, r4, r5, r6, r7, r8)
}
