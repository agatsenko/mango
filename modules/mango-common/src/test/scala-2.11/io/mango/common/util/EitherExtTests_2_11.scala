/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-14
  */
package io.mango.common.util

import scala.util.Try

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class EitherExtTests_2_11 extends FunSuite with TableDrivenPropertyChecks with Matchers {
  import EitherExtTests_2_11._

  test("toTry should convert Either[E <: Throwable, T] to Try[T]") {
    val ex1 = new Test1Exception
    val ex2 = new Test2Exception
    val data = Table(
      ("either", "expectedTry"),
      (new Right(10), Try(10)),
      (new Left(ex1), Try(throw ex1)),
      (new Left(ex2), Try(throw ex2))
    )

    forAll(data) { (either, expectedTry) =>
      assert(either.toTry == expectedTry)
    }
  }
}

object EitherExtTests_2_11 {
  class Test1Exception extends Exception
  class Test2Exception extends Exception
}
