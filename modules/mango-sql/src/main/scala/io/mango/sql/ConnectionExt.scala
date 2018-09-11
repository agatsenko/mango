/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.sql

import scala.util.Try

import java.sql.Connection

import io.mango.common.util.TryExt
import org.slf4j.LoggerFactory

object ConnectionExt {
  private val logger = LoggerFactory.getLogger(getClass)

  implicit class ConnectionExt(val conn: Connection) extends AnyVal {
    def withinTx[R](txIsolationLevel: Int)(action: Connection => R): R = {
      val isRootTx = conn.getAutoCommit
      val oldTxIsolation = conn.getTransactionIsolation
      Try {
        if (isRootTx) {
          logger.debug(
            "start root transaction (oldIsolationLevel: {}, newIsolationLevel: {})",
            oldTxIsolation,
            txIsolationLevel
          )
          conn.setTransactionIsolation(txIsolationLevel)
          conn.setAutoCommit(false)
        }
        else {
          logger.debug("start inner transaction")
        }
        val result = action(conn)
        if (isRootTx) {
          conn.commit()
          logger.debug(
            "root transaction has been successful completed (oldIsolationLevel: {}, newIsolationLevel: {})",
            oldTxIsolation,
            txIsolationLevel
          )
        }
        else {
          logger.debug("inner transaction has been successful completed")
        }
        result
      }.eventually { tr =>
        if (tr.isFailure) {
          if (isRootTx) {
            logger.debug(
              "root transaction has been failed, start rollback (oldIsolationLevel: {}, newIsolationLevel: {})",
              oldTxIsolation,
              txIsolationLevel
            )
            conn.rollback()
            logger.debug(
              "rollback has been successful completed (oldIsolationLevel: {}, newIsolationLevel: {})",
              oldTxIsolation,
              txIsolationLevel
            )
          }
          else {
            logger.debug("inner transaction has been failed")
          }
        }
      }.eventually { _ =>
        if (isRootTx) {
          conn.setAutoCommit(true)
        }
      }.eventually { _ =>
        if (isRootTx) {
          conn.setTransactionIsolation(oldTxIsolation)
        }
      }.get
    }

    def withinTx[R](action: Connection => R): R = withinTx(Connection.TRANSACTION_READ_COMMITTED)(action)
  }
}
