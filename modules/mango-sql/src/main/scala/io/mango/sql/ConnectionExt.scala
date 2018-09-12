/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-12
  */
package io.mango.sql

import scala.language.implicitConversions

import scala.util.Try

import java.sql.Connection

import io.mango.common.util.TryExt

class ConnectionExt(val conn: Connection) extends AnyVal {
  def withinTx[R](isolationLevel: Int)(action: Connection => R): R = {
    if (conn.getAutoCommit) withinRootTx(isolationLevel, action) else withinInnerTx(action)
  }

  def withinTx[R](action: Connection => R): R = withinTx(Connection.TRANSACTION_READ_COMMITTED)(action)

  private def withinRootTx[R](isolationLevel: Int, action: Connection => R): R = {
    val oldIsolationLevel = conn.getTransactionIsolation
    Try {
      conn.setTransactionIsolation(isolationLevel)
      conn.setAutoCommit(false)
      val result = action(conn)
      conn.commit()
      result
    }.eventually { tr =>
      if (tr.isFailure) {
        conn.rollback()
      }
    }.eventually(_ => conn.setAutoCommit(true)).eventually(_ => conn.setTransactionIsolation(oldIsolationLevel)).get
  }

  private def withinInnerTx[R](action: Connection => R): R = action(conn)
}

trait ConnectionExtImplicits {
  @inline implicit def wrapConnectionToConnectionExt(conn: Connection): ConnectionExt = new ConnectionExt(conn)
}

object ConnectionExt extends ConnectionExtImplicits {
}