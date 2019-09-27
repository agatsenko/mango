/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-14
 */
package io.mango.common.util

import scala.util.{Failure, Success, Try}

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class TryExtTests extends FunSuite with TableDrivenPropertyChecks with Matchers {
  import TryExtTests._

  test("eventually(action) should perform action regardless of the Try result") {
    val data = Table[Either[Throwable, Any]](
      "tryResult",
      Right(10),
      Left(new TestException)
    )

    forAll(data) { tryResult =>
      var actionPerformed = false
      val tr = Try {
        tryResult match {
          case Right(v) => v
          case Left(ex) => throw ex
        }
      }.eventually { tr =>
        tr match {
          case Success(v) => assert(v == tryResult.getOrElse(null))
          case Failure(ex) => assert(ex == tryResult.swap.getOrElse(null))
        }
        actionPerformed = true
      }

      assert(actionPerformed)
      tr match {
        case Success(v) => assert(v == tryResult.getOrElse(null))
        case Failure(ex) => assert(ex == tryResult.swap.getOrElse(null))
      }
    }
  }

  test("eventually(action) should return Failure if eventually throw exception") {
    val tryResult = 10
    val eventuallyEx = new TestException

    val tr = Try(tryResult).eventually { tr =>
      assert(tr.isSuccess)
      throw eventuallyEx
    }
    assert(tr.isFailure)
    assert(tr.failed.get == eventuallyEx)
  }

  test("eventually(action) should add Try exception into Eventually exception as suppressed") {
    val tryEx = new TestException
    val eventuallyEx = new TestException

    val tr = Try(throw tryEx).eventually { tr =>
      assert(tr.isFailure)
      throw eventuallyEx
    }
    assert(tr.isFailure)

    val ex = tr.failed.get
    assert(ex == eventuallyEx)
    assert(ex.getSuppressed.length == 1)
    assert(ex.getSuppressed()(0) == tryEx)
  }

  test("convertFailure(converter) should not perform converter if try result is Success") {
    val tryResult = 10
    var convertPerformed = false
    val tr = Try(tryResult).convertFailure { _ =>
      convertPerformed = true
      new TestException
    }
    assert(!convertPerformed)
    assert(tr.isSuccess)
    assert(tr.get == tryResult)
  }

  test("convertFailure(converter) should convert Try exception if src exception is instance of specified exception") {
    val tryEx = new Exception
    val convertedEx = new TestException
    val tr = Try(throw tryEx).convertFailure { ex =>
      convertedEx.addSuppressed(ex)
      convertedEx
    }
    assert(tr.isFailure)

    val ex = tr.failed.get
    assert(ex == convertedEx)
    assert(ex.getSuppressed != null)
    assert(ex.getSuppressed.length == 1)
    assert(ex.getSuppressed()(0) eq tryEx)
  }
}

object TryExtTests {
  class TestException extends Exception
}
