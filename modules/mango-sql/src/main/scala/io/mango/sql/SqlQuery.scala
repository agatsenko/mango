/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-04
 */
package io.mango.sql

import scala.collection.{Factory, Seq}
import scala.util.Using

import java.sql.{Connection, PreparedStatement, ResultSet}

class SqlQuery private[sql](val sql: String, paramValues: Seq[Any])(implicit converter: SqlValueConverter) {
  val params: Seq[Any] = if (paramValues == null) Seq.empty else paramValues.map(converter.toSqlValue)

  def prepareStatement(implicit conn: Connection): PreparedStatement = {
    val ps = conn.prepareStatement(sql)
    params.indices.foreach(i => ps.setObject(i + 1, params(i)))
    ps
  }

  def execScalar[T](implicit c: Connection, r: ResultSetReader[Option[T]]): Option[T] = {
    Using.resource(prepareStatement) { ps =>
      val rs = ps.executeQuery()
      if (rs.next()) {
        ResultSetReader.readAs[Option[T]](rs, 1)
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

  def execRows[T, C[_]](mapper: ResultSet => T)(implicit conn: Connection, cbf: Factory[T, C[T]]): C[T] =
    Using.resource(execQuery)(_.mapRows(mapper))

  def execForeachRows(action: ResultSet => Unit)(implicit conn: Connection): Unit = {
    Using.resource(execQuery)(_.foreachRows(action))
  }

  def execOneRow[T](mapper: ResultSet => T)(implicit conn: Connection): Option[T] = {
    Using.resource(execQuery)(_.mapOneRow(mapper))
  }

  def execUpdate()(implicit conn: Connection): Long = {
    Using.resource(prepareStatement) { ps =>
      ps.executeUpdate()
    }
  }

  override def toString: String = s"${classOf[SqlQuery].getSimpleName}('$sql', $paramValues)"
}
