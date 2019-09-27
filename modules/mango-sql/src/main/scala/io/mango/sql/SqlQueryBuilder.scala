/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-12
 */
package io.mango.sql

import scala.language.implicitConversions

import scala.collection.mutable

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
