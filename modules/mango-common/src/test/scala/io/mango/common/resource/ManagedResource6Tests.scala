/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-24
  */
package io.mango.common.resource

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class ManagedResource6Tests extends FunSuite
                                    with BaseManagedResourceTests
                                    with TableDrivenPropertyChecks
                                    with MockFactory
                                    with Matchers {
  type AC = AutoCloseable
  type TManagedResource = ManagedResource6[
      TestResource,
      TestResource,
      TestResource,
      TestResource,
      TestResource,
      TestResource
  ]
  type TActionResult = Int
  type TAction = (
      TestResource,
      TestResource,
      TestResource,
      TestResource,
      TestResource,
      TestResource
  ) => TActionResult

  test("and(res) should close source managed resource and create new managed resource") {
    val managedRes = newManagedResource
    val newRes = newTestResource("newRes")
    val newManagedRes = managedRes.and(newRes)

    assert(managedRes.isClosed)
    assert(!managedRes.isOpened)
    assertAllResourcesOpened(managedRes)

    assert(!newManagedRes.isClosed)
    assert(newManagedRes.isOpened)
    assertAllResourcesOpened(newManagedRes)
    assert((managedRes.getResources :+ newRes) == newManagedRes.getResources)
  }

  test("and(res) should close all resources if res throw exception") {
    val managedResources = Table(
      "managedRes",
      newManagedResource,
      newManagedResourceWithCloseEx
    )

    forAll(managedResources) { managedRes =>
      val exception = new TestException("actionError")
      val actualException = intercept[TestException](managedRes.and(throw exception))
      assert(actualException == exception || actualException.getSuppressed.contains(exception))
      assertAllResourcesClosed(managedRes)
    }
  }

  test("using(managedResource)(action) should close all managed resources after action is successful completed") {
    val managedRes = newManagedResource
    val actionResult = 10
    val action = newActionMock(managedRes, actionResult)

    val actualActionResultOpt = using(managedRes)(action(_, _, _, _, _, _))
    assert(actualActionResultOpt != null)
    assert(actualActionResultOpt.isDefined)
    assert(actualActionResultOpt.get == actionResult)
    assertAllResourcesClosed(managedRes)
  }

  test("using(managedResource)(action) should close all managed resources, even if resources throw exception") {
    val managedRes = newManagedResourceWithCloseEx
    val action = newActionMock(managedRes, 10)
    intercept[Throwable](using(managedRes)(action(_, _, _, _, _, _)))
    assertAllResourcesClosed(managedRes)
  }

  test("using(managedResource)(action) should close all managed resources after action is unsuccessful completed") {
    val managedRes = newManagedResource
    val actionResult = new TestException()
    val action = newActionMock(managedRes, actionResult)

    val actualActionResult = intercept[TestException](using(managedRes)(action(_, _, _, _, _, _)))
    assert(actualActionResult == actionResult)
    assertAllResourcesClosed(managedRes)
  }

  private def newTestResource(name: String): TestResource = TestResource(name)

  private def newTestResource(name: String, closeEx: Throwable): TestResource = TestResource(name, Some(closeEx))

  private def newManagedResource: TManagedResource = {
    managed(newTestResource("res1")).
        and(newTestResource("res2")).
        and(newTestResource("res3")).
        and(newTestResource("res4")).
        and(newTestResource("res5")).
        and(newTestResource("res6"))
  }

  private def newManagedResourceWithCloseEx: TManagedResource = {
    managed(newTestResource("res1", new TestException("close res1"))).
        and(newTestResource("res2", new TestException("close res2"))).
        and(newTestResource("res3", new TestException("close res3"))).
        and(newTestResource("res4", new TestException("close res4"))).
        and(newTestResource("res5", new TestException("close res5"))).
        and(newTestResource("res6", new TestException("close res6")))
  }

  private def newActionMock(managedRes: TManagedResource, result: TActionResult): TAction = {
    val action = mockFunction[
        TestResource,
        TestResource,
        TestResource,
        TestResource,
        TestResource,
        TestResource,
        TActionResult
    ]
    action.
        expects(
          managedRes.r1,
          managedRes.r2,
          managedRes.r3,
          managedRes.r4,
          managedRes.r5,
          managedRes.r6
        ).
        returns(result)
    action
  }

  private def newActionMock(managedRes: TManagedResource, result: Throwable): TAction = {
    val action = mockFunction[
        TestResource,
        TestResource,
        TestResource,
        TestResource,
        TestResource,
        TestResource,
        TActionResult
    ]
    action.
        expects(
          managedRes.r1,
          managedRes.r2,
          managedRes.r3,
          managedRes.r4,
          managedRes.r5,
          managedRes.r6
        ).
        throws(result)
    action
  }
}
