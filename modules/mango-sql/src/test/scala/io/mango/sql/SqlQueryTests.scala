/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-12
  */
package io.mango.sql

import java.sql.{Connection, PreparedStatement}

import io.mango.common.resource.using
import io.mango.common.util.SimpleValExt.IntExt
import io.mango.sql.test.infrastructure.{Record, TestWithRecord}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}

class SqlQueryTests extends FunSuite with TestWithRecord with MockFactory with Matchers {
  import Implicits.All._
  import Record._

  val connUrl = "jdbc:h2:mem:sql_query_test"

  test("test prepareStatement") {
    val ps = mock[PreparedStatement]
    val conn = mock[Connection]

    val sql = "select * from table where f1 = ? and f2 = ?"
    val params = Seq[Any](1.box, "test")

    (conn.prepareStatement(_: String)).expects(sql).returns(ps)
    (ps.setObject(_: Int, _: Any)).expects(1, params(0))
    (ps.setObject(_: Int, _: Any)).expects(2, params(1))

    new SqlQuery(sql, params).prepareStatement(conn)
  }

  test("test execScalar") {
    testWithinConn { implicit conn =>
      val records = createRecords()
      records.foreach(insertRecord(conn, _))

      val query = new SqlQuery(s"select count(*) from $TABLE", null)
      val count = query.execScalar[Int]
      assert(count.isDefined)
      assert(count.get == records.size)
    }
  }

  test("test execQuery") {
    testWithinConn { implicit conn =>
      val query = new SqlQuery(s"select count(*) from $TABLE", null)
      using(query.execQuery) { qr =>
        assert(qr != null)

        assert(qr.preparedStatement != null)
        assert(!qr.preparedStatement.isClosed)

        assert(qr.resultSet != null)
        assert(!qr.resultSet.isClosed)
      }
    }
  }

  test("test execRows") {
    testWithinConn { implicit conn =>
      val expected = createRecords()
      expected.foreach(insertRecord(conn, _))

      val actual: List[Record] = new SqlQuery(s"select * from $TABLE", null).execRows { rs =>
        Record(rs.get[Long](ID_CLM), rs.get[Option[String]](STR_CLM), rs.get[Option[Int]](INT_CLM))
      }
      assert(actual.size == expected.size)
      assert(actual.forall(expected.contains))
    }
  }

  test("test execForeachRows") {
    testWithinConn { implicit conn =>
      val expected = createRecords()
      expected.foreach(insertRecord(conn, _))

      val actualBuilder = Seq.newBuilder[Record]
      new SqlQuery(s"select * from $TABLE", null).execForeachRows { rs =>
        actualBuilder += Record(rs.get[Long](ID_CLM), rs.get[Option[String]](STR_CLM), rs.get[Option[Int]](INT_CLM))
      }
      val actual = actualBuilder.result()
      assert(actual.size == expected.size)
      assert(actual.forall(expected.contains))
    }
  }

  test("test execOneRow") {
    testWithinConn { implicit conn =>
      val expected = Record(1, Some("test"), None)
      insertRecord(conn, expected)

      val actual = new SqlQuery(s"select * from $TABLE where $ID_CLM = ?", Seq(expected.id)).execOneRow { rs =>
        Record(rs.get[Long](ID_CLM), rs.get[Option[String]](STR_CLM), rs.get[Option[Int]](INT_CLM))
      }
      assert(actual.orNull == expected)
    }
  }

  test("execUpdate") {
    testWithinConn { implicit conn =>
      val newRecord = Record(1, Some("test"), None)
      val query = new SqlQuery(
        s"insert into $TABLE ($ID_CLM, $STR_CLM, $INT_CLM) values (?, ?, ?)",
        Seq(newRecord.id, newRecord.str, newRecord.int)
      )
      assert(query.execUpdate() == 1)

      val allRecords = selAllRecords(conn)
      assert(allRecords.contains(newRecord))
    }
  }
}
