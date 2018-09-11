/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.sql

import scala.language.higherKinds

import scala.collection.generic.CanBuildFrom

import java.sql.ResultSet

object ResultSetExt {
  implicit class ResultSetExt(val rs: ResultSet) extends AnyVal {
    def get[T](columnPos: Int)(implicit r: ResultSetReader[T]): T = r.read(rs, columnPos)

    def get[T](columnName: String)(implicit r: ResultSetReader[T]): T = get(rs.findColumn(columnName))

    def foreach(action: ResultSet => Unit): Unit = {
      while (rs.next()) {
        action(rs)
      }
    }

    def map[T, C[_]](mapper: ResultSet => T)(implicit cbf: CanBuildFrom[Nothing, T, C[T]]): C[T] = {
      val builder = cbf.apply()
      while (rs.next()) {
        builder += mapper(rs)
      }
      builder.result()
    }
  }
}
