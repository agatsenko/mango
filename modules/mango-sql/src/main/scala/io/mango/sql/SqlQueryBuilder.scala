/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.sql

import scala.collection.mutable

object SqlQueryBuilder {
  implicit class SqlQueryBuilder(val sc: StringContext) extends AnyVal {
    def sql(args: Any*): SqlQuery = {
      val sqlBuilder = StringBuilder.newBuilder
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