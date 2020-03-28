/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-14
 */
package io.mango.common.util

import java.{lang => jl}

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SimpleValExtTests extends AnyFunSuite with Matchers {
  import SimpleValExt._

  //#region boolean box/unbox

  test("box boolean") {
    val bool = true
    val box = bool.box
    assert(box.isInstanceOf[jl.Boolean])
    assert(box.booleanValue() == bool)
  }

  test("unbox boolean") {
    val box = jl.Boolean.valueOf(true)
    val bool = box.unbox
    assert(bool == box.booleanValue())
  }

  test("unbox null boolean should throw NullPointerException") {
    val box: jl.Boolean = null
    intercept[NullPointerException](box.unbox)
  }

  //#endregion boolean box/unbox

  //#region byte box/unbox

  test("box byte") {
    val byte = 10.toByte
    val box = byte.box
    assert(box.isInstanceOf[jl.Byte])
    assert(box.byteValue() == byte)
  }

  test("unbox byte") {
    val box = jl.Byte.valueOf(10.toByte)
    val byte = box.unbox
    assert(byte == box.byteValue())
  }

  test("unbox null byte should throw NullPointerException") {
    val box: jl.Byte = null
    intercept[NullPointerException](box.unbox)
  }

  //#endregion byte box/unbox

  //#region short box/unbox

  test("box short") {
    val short = 10.toShort
    val box = short.box
    assert(box.isInstanceOf[jl.Short])
    assert(box.shortValue() == short)
  }

  test("unbox short") {
    val box = jl.Short.valueOf(10.toShort)
    val short = box.unbox
    assert(short == box.shortValue())
  }

  test("unbox null short should throw NullPointerException") {
    val box: jl.Short = null
    intercept[NullPointerException](box.unbox)
  }

  //#endregion short box/unbox

  //#region int box/unbox

  test("box int") {
    val int = 10
    val box = int.box
    assert(box.isInstanceOf[jl.Integer])
    assert(box.shortValue() == int)
  }

  test("unbox int") {
    val box = jl.Integer.valueOf(10)
    val int = box.unbox
    assert(int == box.intValue())
  }

  test("unbox null int should throw NullPointerException") {
    val box: jl.Integer = null
    intercept[NullPointerException](box.unbox)
  }

  //#endregion int box/unbox

  //#region long box/unbox

  test("box long") {
    val int = 10
    val box = int.box
    assert(box.isInstanceOf[jl.Integer])
    assert(box.shortValue() == int)
  }

  test("unbox long") {
    val box = jl.Long.valueOf(10L)
    val long = box.unbox
    assert(long == box.longValue())
  }

  test("unbox null long should throw NullPointerException") {
    val box: jl.Long = null
    intercept[NullPointerException](box.unbox)
  }

  //#endregion long box/unbox

  //#region float box/unbox

  test("box float") {
    val float = 10f
    val box = float.box
    assert(box.isInstanceOf[jl.Float])
    assert(box.shortValue() == float)
  }

  test("unbox float") {
    val box = jl.Float.valueOf(10)
    val float = box.unbox
    assert(float == box.floatValue())
  }

  test("unbox null float should throw NullPointerException") {
    val box: jl.Float = null
    intercept[NullPointerException](box.unbox)
  }

  //#endregion float box/unbox

  //#region double box/unbox

  test("box double") {
    val double = 10.0
    val box = double.box
    assert(box.isInstanceOf[jl.Double])
    assert(box.shortValue() == double)
  }

  test("unbox double") {
    val box = jl.Double.valueOf(10.0)
    val double = box.unbox
    assert(double == box.doubleValue())
  }

  test("unbox null double should throw NullPointerException") {
    val box: jl.Double = null
    intercept[NullPointerException](box.unbox)
  }

  //#endregion double box/unbox
}
