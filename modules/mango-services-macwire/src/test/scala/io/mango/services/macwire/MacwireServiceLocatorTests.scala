/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-16
  */
package io.mango.services.macwire

import org.scalatest.{FunSuite, Matchers}

import com.softwaremill.macwire._

class MacwireServiceLocatorTests extends FunSuite with Matchers {
  import MacwireServiceLocatorTests._

  test("get[T] should return None if services is not defined") {
    val module = new Module1 {}
    val locator = MacwireServiceLocator(wiredInModule(module))
    val service = locator.get[Service2]
    assert(service != null)
    assert(service.isEmpty)
  }

  test("get[T] should return Some if services is defined") {
    val module = new Module1 {}
    val locator = MacwireServiceLocator(wiredInModule(module))
    val service = locator.get[Service1]
    assert(service != null)
    assert(service.isDefined)
    assert(service.get == module.service1)
  }

  test("get[T] should throw IllegalStateException if defined more than one services with specified type") {
    val module = new Module2 {}
    val locator = MacwireServiceLocator(wiredInModule(module))
    intercept[IllegalStateException](locator.get[Service2])
  }

  test("get[T](key) should throw UnsupportedOperationException") {
    val module = new Module1 {}
    val locator = MacwireServiceLocator(wiredInModule(module))
    intercept[UnsupportedOperationException](locator.get[Service1]("key"))
  }

  test("getAll[T] should return all services with specified type") {
    val module = new Module2 {}
    val locator = MacwireServiceLocator(wiredInModule(module))
    val services = locator.getAll[Service2]
    assert(services != null)
    assert(services.contains(module.service2Int1))
    assert(services.contains(module.service2Int2))
  }
}

object MacwireServiceLocatorTests {
  trait Service1
  class Service1Impl extends Service1

  trait Module1 {
    lazy val service1: Service1Impl = wire[Service1Impl]
  }

  trait Service2
  class Service2Impl1 extends Service2
  class Service2Impl2 extends Service2

  trait Module2 {
    lazy val service2Int1: Service2Impl1 = wire[Service2Impl1]
    lazy val service2Int2: Service2Impl2 = wire[Service2Impl2]
  }
}