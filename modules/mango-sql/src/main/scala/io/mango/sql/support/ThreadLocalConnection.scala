/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.sql.support

import java.sql.Connection

import io.mango.common.util.Check

// FIXME: need to implement tests
private[support] class ThreadLocalConnection(conn: Connection) extends ConnectionWrapper(conn) {
  import ThreadLocalConnection._

  Check.state(
    connHolder.get() == null,
    s"unable to create ${classOf[ThreadLocalConnection].getSimpleName} " +
    s"because another connection is used on this thread"
  )
  connHolder.set(this)

  override def close(): Unit = {
    if (connHolder.get() == this) {
      connHolder.remove()
    }
    super.close()
  }
}

private[support] object ThreadLocalConnection {
  private val connHolder = new ThreadLocal[Connection]()

  def get(): Option[Connection] = Option(connHolder.get())
}