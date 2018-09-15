/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-15
  */
package io.mango.common.util

import scala.util.Try

import io.mango.common.util.TryExtTests.TestException
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class TryExtTests_2_11 extends FunSuite with TableDrivenPropertyChecks with Matchers {
  test("toEither should convert Try[T] to Either[Throwable, T]") {
    val tryEx = new TestException()
    val data = Table[Try[Any], Either[Throwable, Any]](
      ("tr", "expectedResult"),
      (Try(10), Right(10)),
      (Try(throw tryEx), Left(tryEx))
    )
    forAll(data) { (tr, expectedResult) =>
      assert(tr.toEither == expectedResult)
    }
  }
}
