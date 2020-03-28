/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-11
 */
package io.mango.sql

import scala.util.Using

import java.sql.Connection

import io.mango.sql.test.infrastructure.{Record, TestWithRecord}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ConnectionExtTests extends AnyFunSuite with TestWithRecord with Matchers {
  import ConnectionExtTests._
  import Implicits.All._
  import Record._

  val connUrl = "jdbc:h2:mem:connection_ext_test"

  test("test successful root tx") {
    testWithinConn { c =>
      val expectedRecords = createRecords()
      c.withinTx { conn =>
        expectedRecords.foreach(insertRecord(conn, _))
      }

      val actualRecords = Using.resource(connPool.getConnection)(selAllRecords)
      assert(actualRecords.size == expectedRecords.size)
      assert(actualRecords.forall(expectedRecords.contains(_)))
    }
  }

  test("test successful root and inner txs") {
    testWithinConn { c =>
      val seq = RecordSeq()
      val rootRecords = createRecords(seq)
      val innerRecords = createRecords(seq)
      val expectedRecords = rootRecords ++ innerRecords

      def rooTx(conn: Connection): Unit = rootRecords.foreach(insertRecord(conn, _))

      def innerTx(conn: Connection): Unit = innerRecords.foreach(insertRecord(conn, _))

      c.withinTx { rootConn =>
        rooTx(rootConn)
        rootConn.withinTx { innerConn =>
          innerTx(innerConn)
        }
      }

      val actualRecords = Using.resource(connPool.getConnection)(selAllRecords)
      assert(actualRecords.size == expectedRecords.size)
      assert(actualRecords.forall(expectedRecords.contains(_)))
    }
  }

  test("test successful root tx and filed inner tx") {
    testWithinConn { c =>
      val seq = RecordSeq()
      val rootRecords = createRecords(seq)
      val innerRecords = createRecords(seq)

      def rooTx(conn: Connection): Unit = {
        rootRecords.foreach(insertRecord(conn, _))
      }

      def innerTx(conn: Connection): Unit = {
        innerRecords.foreach(insertRecord(conn, _))
        throw new TestException
      }

      intercept[TestException] {
        c.withinTx { rootConn =>
          rooTx(rootConn)
          rootConn.withinTx { innerConn =>
            innerTx(innerConn)
          }
        }
      }
    }

    val actualRecords = Using.resource(connPool.getConnection)(selAllRecords)
    assert(actualRecords.isEmpty)
  }

  test("test failed root tx and successful inner tx") {
    testWithinConn { c =>
      val seq = RecordSeq()
      val rootRecords = createRecords(seq)
      val innerRecords = createRecords(seq)

      def rooTx(conn: Connection): Unit = {
        rootRecords.foreach(insertRecord(conn, _))
        throw new TestException
      }

      def innerTx(conn: Connection): Unit = {
        innerRecords.foreach(insertRecord(conn, _))
      }

      intercept[TestException] {
        c.withinTx { rootConn =>
          rootConn.withinTx { innerConn =>
            innerTx(innerConn)
          }
          rooTx(rootConn)
        }
      }
    }

    val actualRecords = Using.resource(connPool.getConnection)(selAllRecords)
    assert(actualRecords.isEmpty)
  }
}

object ConnectionExtTests {
  val CONN_URL = "jdbc:h2:mem:connection_ext_test"

  class TestException extends Exception
}
