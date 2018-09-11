/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-04
  */
package io.mango.sql

import scala.language.higherKinds

import scala.collection.generic.CanBuildFrom

import java.sql.{Connection, PreparedStatement, ResultSet}

import io.mango.common.resource.using

class SqlQuery private[sql](val sql: String, paramValues: Seq[Any]) {
  val params: Seq[Any] = if (paramValues == null) Seq.empty else paramValues.map(Converter.toSqlValue)

  def prepareStatement(implicit conn: Connection): PreparedStatement = {
    val ps = conn.prepareStatement(sql)
    params.indices.foreach(i => ps.setObject(i + 1, params(i)))
    ps
  }

  def execScalar[T](implicit c: Connection, r: ResultSetReader[Option[T]]): Option[T] = {
    using(prepareStatement) { ps =>
      val rs = ps.executeQuery()
      if (rs.next()) {
        rs.get[Option[T]](1)
      }
      else {
        None
      }
    }
  }

  def execQuery(implicit conn: Connection): SqlQueryResult = {
    val ps = prepareStatement
    new SqlQueryResult(ps, ps.executeQuery())
  }

  def execRows[T, C[_]](
      mapper: ResultSet => T)(
      implicit conn: Connection,
      cbf: CanBuildFrom[Nothing, T, C[T]]): C[T] = using(execQuery)(_.mapRows(mapper))

  def execRows(action: ResultSet => Unit)(implicit conn: Connection): Unit = {
    using(execQuery)(_.foreachRows(action))
  }

  def execOneRow[T](mapper: ResultSet => T)(implicit conn: Connection): Option[T] = {
    using(execQuery)(_.mapOneRow(mapper))
  }

  def execUpdate()(implicit conn: Connection): Long = {
    using(prepareStatement) { ps =>
      ps.executeUpdate()
    }
  }
}
