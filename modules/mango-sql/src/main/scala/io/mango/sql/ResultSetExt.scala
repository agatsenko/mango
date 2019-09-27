/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-25
 */
package io.mango.sql

import scala.language.implicitConversions

import scala.collection.Factory

import java.sql.ResultSet

object ResultSetExt {
  implicit class ResultSetExtImplicit(val rs: ResultSet) extends AnyVal {
    def get[T](columnPos: Int)(implicit r: ResultSetReader[T]): T = r.read(rs, columnPos)

    def get[T](columnName: String)(implicit r: ResultSetReader[T]): T = get(rs.findColumn(columnName))

    def foreach(action: ResultSet => Unit): Unit = {
      while (rs.next()) {
        action(rs)
      }
    }

    def map[T, C[_]](mapper: ResultSet => T)(implicit f: Factory[T, C[T]]): C[T] = {
      val builder = f.newBuilder
      while (rs.next()) {
        builder += mapper(rs)
      }
      builder.result()
    }
  }
}

trait ResultSetExtImplicits {
  @inline implicit def toResultSetExt(rs: ResultSet): ResultSetExt.ResultSetExtImplicit =
    new ResultSetExt.ResultSetExtImplicit(rs)
}