/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.common.util

object Check {
  def arg(condition: Boolean, msg: => CharSequence): Unit = {
    check(condition, msg, new IllegalArgumentException(_))
  }

  def argNotNull[T](arg: T, argName: CharSequence): Unit = {
    Check.arg(arg != null, s"$argName is null")
  }

  def argNotNullOrEmpty(arg: CharSequence, argName: CharSequence): Unit = {
    Check.arg(arg != null && arg.length() > 0, s"$argName is null or empty")
  }

  def argNotNullOrEmpty[T](arg: TraversableOnce[T], argName: CharSequence): Unit = {
    Check.arg(arg != null && arg.nonEmpty, s"$argName is null or empty")
  }

  def argNotNullOrEmpty[T](arg: Array[T], argName: CharSequence): Unit = {
    Check.arg(arg != null && arg.nonEmpty, s"$argName is null or empty")
  }

  def argDefined[T](arg: Option[T], argName: CharSequence): Unit = {
    argNotNull(arg, argName)
    Check.arg(arg.isDefined, s"$argName is not defined")
  }

  def state(condition: Boolean, msg: => CharSequence): Unit = {
    check(condition, msg, new IllegalStateException(_))
  }

  private def check[E <: Throwable](condition: Boolean, msg: => CharSequence, ex: String => E): Unit = {
    if (!condition) {
      throw ex(msg.toString)
    }
  }
}
