/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-14
 */
package io.mango.common.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class CheckTests extends AnyFunSuite with TableDrivenPropertyChecks with Matchers {
  test("arg(condition, msg)") {
    val data = Table[Boolean, CharSequence, Boolean](
      ("condition", "msg", "isErrorExpected"),
      (true, "test error", false),
      (false, "test error", true)
    )

    forAll(data) { (condition, msg, isErrorExpected) =>
      if (isErrorExpected) {
        val error = intercept[IllegalArgumentException](Check.arg(condition, msg))
        assert(error.getMessage == msg.toString)
      }
      else {
        Check.arg(condition, msg)
      }
    }
  }

  test("argNotNull(arg, argName)") {
    val data = Table[Any, CharSequence](
      ("arg", "argName"),
      (1, "testArg"),
      ("string value", "testArg"),
      (null, "testArg")
    )

    forAll(data) { (arg, argName) =>
      if (arg == null) {
        val error = intercept[IllegalArgumentException](Check.argNotNull(arg, argName))
        assert(error.getMessage != null)
        assert(error.getMessage.contains(argName.toString))
      }
      else {
        Check.argNotNull(arg, argName)
      }
    }
  }

  test("argNotEmpty(arg: TraversableOnce, argName)") {
    val data = Table[Iterable[_], CharSequence](
      ("arg", "argName"),
      (null, "testArg"),
      (Seq(), "testArg"),
      (List(), "testArg"),
      (Seq(1, 2, 3), "testArg"),
      (List(1, 2, 3), "testArg")
    )

    forAll(data) { (arg, argName) =>
      if (arg == null || arg.isEmpty) {
        val error = intercept[IllegalArgumentException](Check.argNotEmpty(arg, argName))
        assert(error.getMessage != null)
        assert(error.getMessage.contains(argName.toString))
      }
      else {
        Check.argNotEmpty(arg, argName)
      }
    }
  }

  test("argNotEmpty(arg: Array, argName)") {
    val data = Table[Array[_], CharSequence](
      ("arg", "argName"),
      (null, "testArg"),
      (Array.empty[Int], "testArg"),
      (Array(1, 2, 3), "testArg")
    )

    forAll(data) { (arg, argName) =>
      if (arg == null || arg.isEmpty) {
        val error = intercept[IllegalArgumentException](Check.argNotEmpty(arg, argName))
        assert(error.getMessage != null)
        assert(error.getMessage.contains(argName.toString))
      }
      else {
        Check.argNotEmpty(arg, argName)
      }
    }
  }

  test("argDefined(arg, argName)") {
    val data = Table[Option[_], CharSequence](
      ("arg", "argName"),
      (null, "testArg"),
      (None, "testArg"),
      (Some(1), "testArg"),
      (Some(null), "testArg")
    )

    forAll(data) { (arg, argName) =>
      if (arg == null || arg.isEmpty) {
        val error = intercept[IllegalArgumentException](Check.argDefined(arg, argName))
        assert(error.getMessage != null)
        assert(error.getMessage.contains(argName.toString))
      }
      else {
        Check.argDefined(arg, argName)
      }
    }
  }

  test("state(condition, msg)") {
    val data = Table[Boolean, CharSequence, Boolean](
      ("condition", "msg", "isErrorExpected"),
      (true, "test error", false),
      (false, "test error", true)
    )

    forAll(data) { (condition, msg, isErrorExpected) =>
      if (isErrorExpected) {
        val error = intercept[IllegalStateException](Check.state(condition, msg))
        assert(error.getMessage == msg.toString)
      }
      else {
        Check.state(condition, msg)
      }
    }
  }
}
