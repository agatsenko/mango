/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-14
 */
package io.mango.sql

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SqlQueryBuilderTests extends AnyFunSuite with Matchers {
  import Implicits.SqlQueryBuilder._

  test("build sql") {
    val sqlFragment1 = "fragment1"
    val sqlFragment2 = "fragment2"
    val param1 = 1
    val param2 = None
    val param3 = Some(true)

    val expectedSql = s"static text, fragments: ($sqlFragment1, $sqlFragment2), params: (?, ?, ?)"
    val expectedParams = Seq[Any](param1, null, param3.get)

    val query =
      sql"static text, fragments: (${sqlf(sqlFragment1)}, ${sqlf(sqlFragment2)}), params: ($param1, $param2, $param3)"
    assert(query.sql == expectedSql)
    assert(query.params == expectedParams)
  }
}
