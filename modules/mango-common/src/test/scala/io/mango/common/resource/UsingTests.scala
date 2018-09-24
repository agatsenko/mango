/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-14
  */
package io.mango.common.resource

import org.scalamock.matchers.Matchers
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.prop.TableDrivenPropertyChecks

class UsingTests extends FunSuite with TableDrivenPropertyChecks with MockFactory with Matchers {
  test("using(resource)(action) should close resource after action is completed") {
    val resourceFactories = Table[() => CloseableResource](
      "resourceFactory",
      () => null,
      () => {
        val resource = mock[CloseableResource]
        (resource.isClosed _).expects().returns(false)
        (resource.close _).expects()
        (resource.isClosed _).expects().returns(true)
        resource
      }
    )

    forAll(resourceFactories) { factory =>
      val resource = factory()
      using(resource) { r =>
        if (r != null) {
          assert(r.isOpened)
        }
      }
      if (resource != null) {
        assert(resource.isClosed)
      }
    }
  }
}

