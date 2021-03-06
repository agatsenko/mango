/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-01-31
 */
package io.mango.sql

import scala.collection.Factory
import scala.util.Try

import java.sql.{PreparedStatement, ResultSet}

import io.mango.common.util.TryExt

class SqlQueryResult private[sql](
    val preparedStatement: PreparedStatement,
    val resultSet: ResultSet) extends AutoCloseable {
  def isClosed: Boolean = preparedStatement.isClosed

  def isOpen: Boolean = !isClosed

  override def close(): Unit = {
    if (isOpen) {
      Try(resultSet.close()).eventually(_ => preparedStatement.close()).get
    }
  }

  def foreachRows(action: ResultSet => Unit): Unit = {
    while (resultSet.next()) {
      action(resultSet)
    }
  }

  def mapRows[R, C[_]](mapper: ResultSet => R)(implicit f: Factory[R, C[R]]): C[R] = {
    val builder = f.newBuilder
    while (resultSet.next()) {
      builder += mapper(resultSet)
    }
    builder.result()
  }

  def mapOneRow[R](mapper: ResultSet => R): Option[R] =
    if (resultSet.next()) Some(mapper(resultSet)) else None
}
