/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.sql

import java.sql.Connection

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.mango.common.resource.using
import org.h2.jdbcx.JdbcDataSource
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

class ConnectionExtTests extends FunSuite with BeforeAndAfter with Matchers {
  import ConnectionExt._
  import ConnectionExtTests._
  import ResultSetExt._
  import ResultSetReaders._
  import SqlQueryBuilder._

  var connPool: HikariDataSource = _

  before {
    connPool = createConnPool()
    createDb()
  }

  after {
    if (connPool != null) {
      try {
        connPool.close()
      }
      finally {
        connPool = null
      }
    }
  }

  test("test successful root tx") {
    testWithinConn { c =>
      val expectedRecords = createRecords()
      c.withinTx { implicit conn =>
        expectedRecords.foreach(inertRecordQry(_).execUpdate())
      }

      val actualRecords = selAllRecords
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

      def rooTx(conn: Connection): Unit = rootRecords.foreach(inertRecordQry(_).execUpdate()(conn))

      def innerTx(conn: Connection): Unit = innerRecords.foreach(inertRecordQry(_).execUpdate()(conn))

      c.withinTx { rootConn =>
        rooTx(rootConn)
        rootConn.withinTx { innerConn =>
          innerTx(innerConn)
        }
      }

      val actualRecords = selAllRecords
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
        rootRecords.foreach(inertRecordQry(_).execUpdate()(conn))
      }

      def innerTx(conn: Connection): Unit = {
        innerRecords.foreach(inertRecordQry(_).execUpdate()(conn))
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

    val actualRecords = selAllRecords
    assert(actualRecords.isEmpty)
  }

  test("test failed root tx and successful inner tx") {
    testWithinConn { c =>
      val seq = RecordSeq()
      val rootRecords = createRecords(seq)
      val innerRecords = createRecords(seq)

      def rooTx(conn: Connection): Unit = {
        rootRecords.foreach(inertRecordQry(_).execUpdate()(conn))
        throw new TestException
      }

      def innerTx(conn: Connection): Unit = {
        innerRecords.foreach(inertRecordQry(_).execUpdate()(conn))
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

    val actualRecords = selAllRecords
    assert(actualRecords.isEmpty)
  }

  def createConnPool(): HikariDataSource = {
    val nativeDataSource = new JdbcDataSource()
    nativeDataSource.setURL(CONN_URL)

    val conf = new HikariConfig()
    conf.setDataSource(nativeDataSource)
    conf.setAutoCommit(true)
    conf.setMinimumIdle(5)
    conf.setMaximumPoolSize(10)

    new HikariDataSource(conf)
  }

  def createDb(): Unit = {
    using(connPool.getConnection) { implicit conn =>
      sql"""
        create table if not exists ${sqlf(TABLE)} (
         ${sqlf(ID_CLM)} bigint not null,
         ${sqlf(STR_CLM)} varchar(50),
         ${sqlf(INT_CLM)} int,

         constraint pk_${sqlf(TABLE)} primary key (${sqlf(ID_CLM)})
        );
        delete from ${sqlf(TABLE)};
      """.execUpdate()
    }
  }

  def createRecords(seq: RecordSeq = RecordSeq()): Seq[Record] = {
    Seq(
      Record(seq.next(), Some("one"), Some(1)),
      Record(seq.next(), Some("two"), None),
      Record(seq.next(), None, Some(3)),
      Record(seq.next(), None, None)
    )
  }

  def selAllRecords: Seq[Record] = {
    using(connPool.getConnection) { implicit c =>
      sql"select * from ${sqlf(TABLE)}".execRows { rs =>
        Record(
          rs.get[Long](ID_CLM),
          rs.get[Option[String]](STR_CLM),
          rs.get[Option[Int]](INT_CLM)
        )
      }
    }
  }

  def inertRecordQry(r: Record): SqlQuery = {
    sql"""
      insert into ${sqlf(TABLE)} (${sqlf(ID_CLM)}, ${sqlf(STR_CLM)}, ${sqlf(INT_CLM)}) values (
        ${r.id}, ${r.str}, ${r.int}
      )
    """
  }

  def testWithinConn(action: Connection => Unit): Unit = {
    using(connPool.getConnection) { c =>
      action(c)
    }
  }
}

object ConnectionExtTests {
  val CONN_URL = "jdbc:h2:mem:connection_ext_test"

  val TABLE = "test_table"
  val ID_CLM = "id"
  val STR_CLM = "str_val"
  val INT_CLM = "int_clm"

  class TestException extends Exception

  case class Record(id: Long, str: Option[String], int: Option[Int])

  class RecordSeq {
    private var seq = 0L

    def next(): Long = {
      seq += 1
      seq
    }
  }

  object RecordSeq {
    def apply(): RecordSeq = new RecordSeq
  }
}
