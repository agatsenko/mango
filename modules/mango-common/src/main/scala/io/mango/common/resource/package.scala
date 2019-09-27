/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.common

import scala.util.Try

import _root_.io.mango.common.util.TryExt

package object resource {
  type AC = AutoCloseable

  def using[R <: AC, T](r: R)(f: R => T): T = {
    Try(f(r)).eventually { _ =>
      if (r != null) {
        r.close()
      }
    }.get
  }

  def managed[R <: AC](res: R): ManagedResource1[R] = ManagedResource1(res)

  def using[R1 <: AC, T](r: ManagedResource1[R1])(f: (R1) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }

  def using[R1 <: AC, R2 <: AC, T](r: ManagedResource2[R1, R2])(f: (R1, R2) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }

  def using[R1 <: AC, R2 <: AC, R3 <: AC, T](r: ManagedResource3[R1, R2, R3])(f: (R1, R2, R3) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }

  def using[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, T](
      r: ManagedResource4[R1, R2, R3, R4])(
      f: (R1, R2, R3, R4) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }

  def using[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, R5 <: AC, T](
      r: ManagedResource5[R1, R2, R3, R4, R5])(
      f: (R1, R2, R3, R4, R5) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }

  def using[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, R5 <: AC, R6 <: AC, T](
      r: ManagedResource6[R1, R2, R3, R4, R5, R6])(
      f: (R1, R2, R3, R4, R5, R6) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }

  def using[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, R5 <: AC, R6 <: AC, R7 <: AC, T](
      r: ManagedResource7[R1, R2, R3, R4, R5, R6, R7])(
      f: (R1, R2, R3, R4, R5, R6, R7) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }

  def using[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, R5 <: AC, R6 <: AC, R7 <: AC, R8 <: AC, T](
      r: ManagedResource8[R1, R2, R3, R4, R5, R6, R7, R8])(
      f: (R1, R2, R3, R4, R5, R6, R7, R8) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }

  def using[R1 <: AC, R2 <: AC, R3 <: AC, R4 <: AC, R5 <: AC, R6 <: AC, R7 <: AC, R8 <: AC, R9 <: AC, T](
      r: ManagedResource9[R1, R2, R3, R4, R5, R6, R7, R8, R9])(
      f: (R1, R2, R3, R4, R5, R6, R7, R8, R9) => T): Option[T] = {
    Try(r.using(f)).eventually(_ => r.close()).get
  }
}
