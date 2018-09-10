/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.common.util

import java.{lang => jl}

object SimpleValExt {
  implicit class BooleanExt(val v: Boolean) extends AnyVal {
    def box: jl.Boolean = Boolean.box(v)
  }

  implicit class JavaBooleanExt(val v: jl.Boolean) extends AnyVal {
    def unbox: Boolean = v.booleanValue()
  }

  implicit class ByteExt(val v: Byte) extends AnyVal {
    def box: jl.Byte = Byte.box(v)
  }

  implicit class JavaByteExt(val v: jl.Byte) extends AnyVal {
    def unbox: Byte = v.byteValue()
  }

  implicit class ShortExt(val v: Short) extends AnyVal {
    def box: jl.Short = Short.box(v)
  }

  implicit class JavaShortExt(val v: jl.Short) extends AnyVal {
    def unbox: Short = v.shortValue()
  }

  implicit class IntExt(val v: Int) extends AnyVal {
    def box: jl.Integer = Int.box(v)
  }

  implicit class JavaIntegerExt(val v: jl.Integer) extends AnyVal {
    def unbox: Int = v.intValue()
  }

  implicit class LongExt(val v: Long) extends AnyVal {
    def box: jl.Long = Long.box(v)
  }

  implicit class JavaLongExt(val v: jl.Long) extends AnyVal {
    def unbox: Long = v.longValue()
  }

  implicit class FloatExt(val v: Float) extends AnyVal {
    def box: jl.Float = Float.box(v)
  }

  implicit class JavaFloatExt(val v: jl.Float) extends AnyVal {
    def unbox: Float = v.floatValue()
  }

  implicit class DoubleExt(val v: Double) extends AnyVal {
    def box: jl.Double = Double.box(v)
  }

  implicit class JavaDoubleExt(val v: jl.Double) extends AnyVal {
    def unbox: Double = v.doubleValue()
  }
}
