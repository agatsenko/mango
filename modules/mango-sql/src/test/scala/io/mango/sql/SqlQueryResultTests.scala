/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-14
 */
package io.mango.sql

import java.sql.{PreparedStatement, ResultSet}

import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class SqlQueryResultTests extends AnyFunSuite with MockFactory with TableDrivenPropertyChecks with Matchers {
  test("close() should close prepareStatement and resultSet") {
    val ps = mock[PreparedStatement]
    (ps.isClosed _).expects().returns(false)
    (ps.close _).expects()

    val rs = mock[ResultSet]
    (rs.close _).expects()

    val result = new SqlQueryResult(ps, rs)
    result.close()
  }

  test("foreachRows(action) should apply action for all rows in resultSet") {
    val testData = Table(
      "expectedRows",
      Seq.empty,
      Seq("one"),
      Seq("one", "two", "three")
    )

    forAll(testData) { expectedRows =>
      val ps = stub[PreparedStatement]
      val rs = mock[ResultSet]
      for (rowNum <- 1 to expectedRows.size) {
        (rs.next _).expects().returns(true)
        (rs.getRow _).expects().returns(rowNum)
      }
      (rs.next _).expects().returns(false)

      val queryResult = new SqlQueryResult(ps, rs)
      val actualRowsBuilder = Seq.newBuilder[String]
      queryResult.foreachRows(rs => actualRowsBuilder += expectedRows(rs.getRow - 1))
      val actualRows = actualRowsBuilder.result()
      assert(actualRows.size == expectedRows.size)
      assert(actualRows.forall(expectedRows.contains))
    }
  }

  test("mapRows(mapper) should map all rows and return specified collection") {
    val testData = Table(
      "expectedRows",
      Seq.empty,
      Seq("one"),
      Seq("one", "two", "three")
    )

    forAll(testData) { expectedRows =>
      val ps = stub[PreparedStatement]
      val rs = mock[ResultSet]
      for (rowNum <- 1 to expectedRows.size) {
        (rs.next _).expects().returns(true)
        (rs.getRow _).expects().returns(rowNum)
      }
      (rs.next _).expects().returns(false)

      val queryResult = new SqlQueryResult(ps, rs)
      val actualRows: Seq[String] = queryResult.mapRows(rs => expectedRows(rs.getRow - 1))
      assert(actualRows.size == expectedRows.size)
      assert(actualRows.forall(expectedRows.contains))
    }
  }

  test("mapOneRow should map only first row and return result") {
    val testData = Table(
      ("rows", "expected"),
      (Seq.empty[String], None),
      (Seq("one"), Some("one")),
      (Seq("one", "two", "three"), Some("one"))
    )

    forAll(testData) { (rows, expected) =>
      val ps = stub[PreparedStatement]
      val rs = mock[ResultSet]
      if (rows.isEmpty) {
        (rs.next _).expects().returns(false)
      }
      else {
        (rs.next _).expects().returns(true)
        (rs.getRow _).expects().returns(1)
      }

      val queryResult = new SqlQueryResult(ps, rs)
      val actual = queryResult.mapOneRow(rs => rows(rs.getRow - 1))
      assert(actual == expected)
    }
  }
}
