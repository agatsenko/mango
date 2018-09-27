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

    def toByteExact: Byte = {
      if (v > Byte.MaxValue || v < Byte.MinValue) {
        throw new ArithmeticException(s"$v out of Byte range")
      }
      v.toByte
    }
  }

  implicit class JavaShortExt(val v: jl.Short) extends AnyVal {
    def unbox: Short = v.shortValue()
  }

  implicit class IntExt(val v: Int) extends AnyVal {
    def box: jl.Integer = Int.box(v)

    def toByteExact: Byte = {
      if (v > Byte.MaxValue || v < Byte.MinValue) {
        throw new ArithmeticException(s"$v out of Byte range")
      }
      v.toByte
    }

    def toShortExact: Short = {
      if (v > Short.MaxValue || v < Short.MinValue) {
        throw new ArithmeticException(s"$v out of Short range")
      }
      v.toShort
    }
  }

  implicit class JavaIntegerExt(val v: jl.Integer) extends AnyVal {
    def unbox: Int = v.intValue()
  }

  implicit class LongExt(val v: Long) extends AnyVal {
    def box: jl.Long = Long.box(v)

    def toByteExact: Byte = {
      if (v > Byte.MaxValue || v < Byte.MinValue) {
        throw new ArithmeticException(s"$v out of Byte range")
      }
      v.toByte
    }

    def toShortExact: Short = {
      if (v > Short.MaxValue || v < Short.MinValue) {
        throw new ArithmeticException(s"$v out of Short range")
      }
      v.toShort
    }

    def toIntExact: Int = {
      if (v > Int.MaxValue || v < Int.MinValue) {
        throw new ArithmeticException(s"$v out of Int range")
      }
      v.toInt
    }
  }

  implicit class JavaLongExt(val v: jl.Long) extends AnyVal {
    def unbox: Long = v.longValue()
  }

  implicit class FloatExt(val v: Float) extends AnyVal {
    def box: jl.Float = Float.box(v)

    def toByteExact: Byte = {
      if (v > Byte.MaxValue || v < Byte.MinValue) {
        throw new ArithmeticException(s"$v out of Byte range")
      }
      v.toByte
    }

    def toShortExact: Short = {
      if (v > Short.MaxValue || v < Short.MinValue) {
        throw new ArithmeticException(s"$v out of Short range")
      }
      v.toShort
    }

    def toIntExact: Int = {
      if (v > Int.MaxValue || v < Int.MinValue) {
        throw new ArithmeticException(s"$v out of Int range")
      }
      v.toInt
    }

    def toLongExact: Long = {
      if (v > Long.MaxValue || v < Long.MinValue) {
        throw new ArithmeticException(s"$v out of Long range")
      }
      v.toLong
    }
  }

  implicit class JavaFloatExt(val v: jl.Float) extends AnyVal {
    def unbox: Float = v.floatValue()
  }

  implicit class DoubleExt(val v: Double) extends AnyVal {
    def box: jl.Double = Double.box(v)

    def toByteExact: Byte = {
      if (v > Byte.MaxValue || v < Byte.MinValue) {
        throw new ArithmeticException(s"$v out of Byte range")
      }
      v.toByte
    }

    def toShortExact: Short = {
      if (v > Short.MaxValue || v < Short.MinValue) {
        throw new ArithmeticException(s"$v out of Short range")
      }
      v.toShort
    }

    def toIntExact: Int = {
      if (v > Int.MaxValue || v < Int.MinValue) {
        throw new ArithmeticException(s"$v out of Int range")
      }
      v.toInt
    }

    def toLongExact: Long = {
      if (v > Long.MaxValue || v < Long.MinValue) {
        throw new ArithmeticException(s"$v out of Long range")
      }
      v.toLong
    }
  }

  implicit class JavaDoubleExt(val v: jl.Double) extends AnyVal {
    def unbox: Double = v.doubleValue()
  }
}
