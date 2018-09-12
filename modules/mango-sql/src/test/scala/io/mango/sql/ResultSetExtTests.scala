/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-12
  */
package io.mango.sql

import io.mango.common.resource.using
import io.mango.sql.test.infrastructure.{Record, TestWithRecord}
import org.scalatest.{FunSuite, Matchers}

class ResultSetExtTests extends FunSuite with TestWithRecord with Matchers {
  import Implicits.All._
  import Record._

  val connUrl = "jdbc:h2:mem:result_set_ext_test"

  test("get by columnPos") {
    testWithinConn { implicit conn =>
      val expected = Record(1, Some("test"), None)
      insertRecord(conn, expected)

      val actual =
        sql"""
          select ${sqlf(ID_CLM)}, ${sqlf(STR_CLM)}, ${sqlf(INT_CLM)}
            from ${sqlf(TABLE)}
            where ${sqlf(ID_CLM)} = ${expected.id}
        """.execOneRow { rs =>
          Record(
            rs.get(1),
            rs.get(2),
            rs.get(3)
          )
        }
      assert(actual.isDefined)
      assert(actual.get == expected)
    }
  }

  test("get by columnName") {
    testWithinConn { implicit conn =>
      val expected = Record(1, Some("test"), None)
      insertRecord(conn, expected)

      val actual =
        sql"""
          select ${sqlf(ID_CLM)}, ${sqlf(STR_CLM)}, ${sqlf(INT_CLM)}
            from ${sqlf(TABLE)}
            where ${sqlf(ID_CLM)} = ${expected.id}
        """.execOneRow { rs =>
          Record(
            rs.get(ID_CLM),
            rs.get(STR_CLM),
            rs.get(INT_CLM)
          )
        }
      assert(actual.isDefined)
      assert(actual.get == expected)
    }
  }

  test("foreach rows") {
    testWithinConn { conn =>
      val expected = createRecords()
      expected.foreach(insertRecord(conn, _))

      val sql = s"select * from $TABLE"
      val actualBuilder = Seq.newBuilder[Record]
      using(conn.prepareStatement(sql)) { ps =>
        using(ps.executeQuery()) { rs =>
          rs.foreach(rs => actualBuilder += Record(rs.get(ID_CLM), rs.get(STR_CLM), rs.get(INT_CLM)))
        }
      }
      val actual = actualBuilder.result()
      assert(actual.size == expected.size)
      assert(actual.forall(expected.contains(_)))
    }
  }

  test("map rows") {
    testWithinConn { conn =>
      val expected = createRecords()
      expected.foreach(insertRecord(conn, _))

      val sql = s"select * from $TABLE"
      val actual: Set[Record] = using(conn.prepareStatement(sql)) { ps =>
        using(ps.executeQuery()) { rs =>
          rs.map(rs => Record(rs.get(ID_CLM), rs.get(STR_CLM), rs.get(INT_CLM)))
        }
      }
      assert(actual.size == expected.size)
      assert(actual.forall(expected.contains(_)))
    }
  }
}
