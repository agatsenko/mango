/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-21
  */
package io.mango.common.resource

import scala.util.Try

import io.mango.common.util.TryExt

private[resource] abstract class ManagedResource extends CloseableResource {
  private var closed = false

  final override def isClosed: Boolean = closed

  final override def close(): Unit = {
    try {
      if (isOpen) {
        closeResources(getResources)
      }
    }
    finally {
      closed = true
    }
  }

  protected[resource] def getResources: Seq[AC]

  protected def newResource[R <: ManagedResource](f: => R): R = {
    Try(f).eventually { tr =>
      if (tr.isFailure) {
        close()
      }
    }.eventually(_ => this.closed = true).get
  }

  private def closeResources(res: Seq[AC]): Unit = {
    if (res.nonEmpty) {
      val tr = Try(res.last.close())
      for (i <- (res.length - 2) to 0 by -1 if i > -1) {
        tr.eventually(_ => res(i).close())
      }
      tr.get
    }
  }
}
