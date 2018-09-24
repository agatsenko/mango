/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-24
  */
package io.mango.common.resource

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class ManagedResource9Tests extends FunSuite
                                    with BaseManagedResourceTests
                                    with TableDrivenPropertyChecks
                                    with MockFactory
                                    with Matchers {
  type AC = AutoCloseable
  type TManagedResource = ManagedResource9[
      TestResource,
      TestResource,
      TestResource,
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
      TestResource,
      TestResource,
      TestResource,
      TestResource
  ) => TActionResult

  test("using(managedResource)(action) should close all managed resources after action is successful completed") {
    val managedRes = newManagedResource
    val actionResult = 10
    val action = newActionMock(managedRes, actionResult)

    val actualActionResultOpt = using(managedRes)(action(_, _, _, _, _, _, _, _, _))
    assert(actualActionResultOpt != null)
    assert(actualActionResultOpt.isDefined)
    assert(actualActionResultOpt.get == actionResult)
    assertAllResourcesClosed(managedRes)
  }

  test("using(managedResource)(action) should close all managed resources, even if resources throw exception") {
    val managedRes = newManagedResourceWithCloseEx
    val action = newActionMock(managedRes, 10)
    intercept[Throwable](using(managedRes)(action(_, _, _, _, _, _, _, _, _)))
    assertAllResourcesClosed(managedRes)
  }

  test("using(managedResource)(action) should close all managed resources after action is unsuccessful completed") {
    val managedRes = newManagedResource
    val actionResult = new TestException()
    val action = newActionMock(managedRes, actionResult)

    val actualActionResult = intercept[TestException](using(managedRes)(action(_, _, _, _, _, _, _, _, _)))
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
        and(newTestResource("res6")).
        and(newTestResource("res7")).
        and(newTestResource("res8")).
        and(newTestResource("res9"))
  }

  private def newManagedResourceWithCloseEx: TManagedResource = {
    managed(newTestResource("res1", new TestException("close res1"))).
        and(newTestResource("res2", new TestException("close res2"))).
        and(newTestResource("res3", new TestException("close res3"))).
        and(newTestResource("res4", new TestException("close res4"))).
        and(newTestResource("res5", new TestException("close res5"))).
        and(newTestResource("res6", new TestException("close res6"))).
        and(newTestResource("res7", new TestException("close res7"))).
        and(newTestResource("res8", new TestException("close res8"))).
        and(newTestResource("res9", new TestException("close res8")))
  }

  private def newActionMock(managedRes: TManagedResource, result: TActionResult): TAction = {
    val action = mockFunction[
        TestResource,
        TestResource,
        TestResource,
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
          managedRes.r6,
          managedRes.r7,
          managedRes.r8,
          managedRes.r9
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
          managedRes.r6,
          managedRes.r7,
          managedRes.r8,
          managedRes.r9
        ).
        throws(result)
    action
  }
}
