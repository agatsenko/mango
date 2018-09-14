/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-14
  */
package io.mango.common.resource

import org.scalamock.matchers.Matchers
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.prop.TableDrivenPropertyChecks

class CloseableResourceTests extends FunSuite with TableDrivenPropertyChecks with MockFactory with Matchers {
  test("isOpened should return !isClosed value") {
    val testData = Table(
      ("isClosed", "expectedIsOpened"),
      (false, true),
      (true, false)
    )

    forAll(testData) { (isClosed, expectedIsOpened) =>
      val resource = stub[CloseableResource]
      (resource.isClosed _).when().returns(isClosed)

      assert(resource.isOpened == expectedIsOpened)
    }
  }
}
