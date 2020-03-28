/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.sql.support

import scala.util.control.NonFatal
import scala.util.Using

import java.sql.Connection
import javax.sql.DataSource

import io.mango.sql.Implicits.ConnectionExt._

// FIXME: need to implement tests
class ThreadLocalPersistentContext(
    private val ds: DataSource,
    private val closeAction: Option[ThreadLocalPersistentContext => Unit] = None) extends PersistentContext {
  override def getConnection: Connection = ThreadLocalConnection.get().getOrElse {
    val conn = ds.getConnection
    try {
      new ThreadLocalConnection(conn)
    }
    catch {
      case NonFatal(ex) =>
        conn.close()
        throw ex
    }
  }

  override def withConn[R](action: Connection => R): R = ThreadLocalConnection.get() match {
    case Some(conn) => action(conn)
    case _ => Using.resource(getConnection)(action)
  }

  override def withinTx[R](isolationLevel: Int)(action: Connection => R): R = ThreadLocalConnection.get() match {
    case Some(conn) => conn.withinTx(isolationLevel)(action)
    case _ => Using.resource(getConnection)(_.withinTx(isolationLevel)(action))
  }

  override def withinTx[R](action: Connection => R): R = ThreadLocalConnection.get() match {
    case Some(conn) => conn.withinTx(action)
    case _ => Using.resource(getConnection)(_.withinTx(action))
  }

  override def close(): Unit = {
    closeAction.foreach(closeAction => closeAction(this))
  }
}
