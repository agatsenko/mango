/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-24
  */
package io.mango.common.resource

import org.scalatest.{Matchers, TestSuite}

trait BaseManagedResourceTests extends TestSuite with Matchers {
  protected[resource] def assertAllResourcesOpened(managedRes: ManagedResource): Unit = {
    assert(managedRes.getResources.forall(r => r.isInstanceOf[TestResource] && !r.asInstanceOf[TestResource].isClosed))
  }

  protected[resource] def assertAllResourcesClosed(managedRes: ManagedResource): Unit = {
    assert(managedRes.getResources.forall(r => r.isInstanceOf[TestResource] && r.asInstanceOf[TestResource].isClosed))
  }

  case class TestResource(name: String, closeExOpt: Option[Throwable] = None) extends AC {
    private var closed = false

    def isClosed: Boolean = closed

    override def close(): Unit = {
      closed = true
      if (closeExOpt.isDefined) {
        throw closeExOpt.get
      }
    }
  }

  class TestException(val name: String = null) extends Exception {
    override def toString: String = if (name == null) super.toString else s"${getClass.getSimpleName}($name)"
  }
}
