/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-04
  */
package io.mango

import scala.language.higherKinds

import scala.collection._
import scala.collection.generic.CanBuildFrom

import java.sql.{Connection, ResultSet}

package object sql {
    implicit class ConnectionExt(val conn: Connection) extends AnyVal {
    def withinTx[R](txIsolationLevel: Int = Connection.TRANSACTION_READ_COMMITTED)(action: Connection => R): R = {
      var result: R = null.asInstanceOf[R]
      val isRootTx = conn.getAutoCommit
      if (isRootTx) {
        conn.setTransactionIsolation(txIsolationLevel)
        conn.setAutoCommit(false)
      }
      try {
        result = action(conn)
        if (isRootTx) {
          conn.commit()
        }
      }
      catch {
        case ex: Exception =>
          if (isRootTx) {
            try {
              conn.rollback()
            }
            catch {
              case rollbackEx: Exception =>
                rollbackEx.addSuppressed(ex)
                throw rollbackEx
            }
          }
          throw ex
      }
      finally {
        if (isRootTx) {
          conn.setAutoCommit(true)
        }
      }
      result
    }
  }

  implicit class ResultSetExt(val rs: ResultSet) extends AnyVal {
    def get[T](columnPos: Int)(implicit r: SqlRecordReader[T]): T = r.read(rs, columnPos)

    def get[T](columnName: String)(implicit r: SqlRecordReader[T]): T = get(rs.findColumn(columnName))

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
