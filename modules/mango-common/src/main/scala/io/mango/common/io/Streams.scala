/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-27
 */
package io.mango.common
package io

import java.io.{BufferedInputStream, BufferedOutputStream, InputStream, OutputStream}

object Streams {
  def toBufferedInputStream(in: InputStream, buffSize: Option[Int] = None): InputStream = {
    in match {
      case _: BufferedInputStream => in
      case _ => buffSize.map(new BufferedInputStream(in, _)).getOrElse(new BufferedInputStream(in))
    }
  }

  def toBufferedOutputStream(out: OutputStream, buffSize: Option[Int] = None): OutputStream = {
    out match {
      case _: BufferedOutputStream => out
      case _ => buffSize.map(new BufferedOutputStream(out, _)).getOrElse(new BufferedOutputStream(out))
    }
  }

  def copy(in: InputStream, out: OutputStream): Unit = {
    val buffIn = toBufferedInputStream(in)
    val buffOut = toBufferedOutputStream(out)
    var b = -1
    while ({b = buffIn.read(); b > -1}) {
      buffOut.write(b)
    }
    buffOut.flush()
  }
}
