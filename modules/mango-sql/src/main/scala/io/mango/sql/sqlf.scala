/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-04
  */
package io.mango.sql

case class sqlf(value: CharSequence) extends AnyVal {
  override def toString: String = if (value == null) "" else value.toString
}
