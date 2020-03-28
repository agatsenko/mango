/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-17
 */
package io.mango.common.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class OptionExTests extends AnyFunSuite with TableDrivenPropertyChecks with Matchers {
  import io.mango.common.util.OptionExTests._

  test("getOrThrow(ex) should get value if Option is defined") {
    val value = 10
    val opt = Some(value)
    assert(opt.getOrThrow(new TestException) == value)
  }

  test("getOrThrow(ex) should throw specified exception if Option is undefined") {
    val opt = None
    intercept[TestException](opt.getOrThrow(new TestException))
  }
}

object OptionExTests {
  class TestException extends Exception
}