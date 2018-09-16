/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-16
  */
package io.mango.services

import scala.reflect.{classTag, ClassTag}

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

class ServicesTests extends FunSuite with BeforeAndAfter with Matchers with MockFactory {
  import ServicesTests._

  before {
    Services.setLocator(null)
  }

  after {
    Services.setLocator(null)
  }

  test("get methods should throw IllegalStateException if locator is not defined") {
    Services.setLocator(null)
    intercept[IllegalStateException](Services.get[String])
    intercept[IllegalStateException](Services.get[String]("test"))
    intercept[IllegalStateException](Services.getAll[String])
  }

  test("get[T] should return None if service is not registered") {
    val locator = mock[ServiceLocator]
    (locator.get(_: ClassTag[Service])).expects(classTag[Service]).returns(None)

    Services.setLocator(locator)

    val actualService = Services.get[Service]
    assert(actualService != null)
    assert(actualService.isEmpty)
  }

  test("get[T] should return Some(service) if service is registered") {
    val expectedService = ServiceImpl1

    val locator = mock[ServiceLocator]
    (locator.get(_: ClassTag[Service])).expects(classTag[Service]).returns(Some(expectedService))

    Services.setLocator(locator)

    val actualService = Services.get[Service]
    assert(actualService != null)
    assert(actualService.isDefined)
    assert(actualService.get == expectedService)
  }

  test("get[T](key) should return None if service is not registered with specified key") {
    val key = "test"

    val locator = mock[ServiceLocator]
    (locator.get(_: Any)(_: ClassTag[Service])).expects(key, classTag[Service]).returns(None)

    Services.setLocator(locator)

    val actualService = Services.get[Service](key)
    assert(actualService != null)
    assert(actualService.isEmpty)
  }

  test("get[T](key) should return Some(service) if service is registered with specified key") {
    val key = "test"
    val expectedService = ServiceImpl1

    val locator = mock[ServiceLocator]
    (locator.get(_: Any)(_: ClassTag[Service])).expects(key, classTag[Service]).returns(Some(expectedService))

    Services.setLocator(locator)

    val actualService = Services.get[Service](key)
    assert(actualService != null)
    assert(actualService.isDefined)
    assert(actualService.get == expectedService)
  }

  test("getAll[T] should return all found services") {
    val expectedServices = Seq(ServiceImpl1, ServiceImpl2)

    val locator = mock[ServiceLocator]
    (locator.getAll(_: ClassTag[Service])).expects(classTag[Service]).returns(expectedServices)

    Services.setLocator(locator)

    val actualServices = Services.getAll[Service]
    assert(actualServices == expectedServices)
  }
}

object ServicesTests {
  trait Service
  object ServiceImpl1 extends Service
  object ServiceImpl2 extends Service
}
