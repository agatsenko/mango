/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-12
 */
package io.mango.sql

import scala.language.implicitConversions

import scala.collection.mutable

import io.mango.common.util.Check

// FIXME: need to implement tests for collection param value
object SqlQueryBuilder {
  implicit class SqlQueryBuilderImplicit(val sc: StringContext) extends AnyVal {
    def sql(args: Any*)(implicit converter: SqlValueConverter): SqlQuery = {
      val sqlBuilder = new StringBuilder
      val paramValues = new mutable.ArrayBuffer[Any](args.size)
      val partsIter = sc.parts.iterator
      val argsIter = args.iterator
      while (argsIter.hasNext) {
        sqlBuilder.append(partsIter.next())
        argsIter.next() match {
          case sqlf(value) => sqlBuilder.append(value)
          case coll: Iterable[_] =>
            Check.arg(!coll.isEmpty, s"iterable argument is empty")
            val iter = coll.iterator
            while (iter.hasNext) {
              val item = iter.next()
              sqlBuilder.append("?,")
              paramValues += item
            }
            sqlBuilder.delete(sqlBuilder.length - 1, sqlBuilder.length)
          case arg =>
            sqlBuilder.append('?')
            paramValues += arg
        }
      }
      sqlBuilder.append(partsIter.next())
      new SqlQuery(sqlBuilder.toString().stripMargin, paramValues)
    }
  }
}

trait SqlQueryBuilderImplicits {
  @inline implicit def wrapStringContextToSqlQueryBuilder(sc: StringContext): SqlQueryBuilder.SqlQueryBuilderImplicit =
    new SqlQueryBuilder.SqlQueryBuilderImplicit(sc)
}
