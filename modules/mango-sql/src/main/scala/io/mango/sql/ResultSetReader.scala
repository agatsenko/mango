/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.sql

import java.sql.ResultSet

trait ResultSetReader[+T] {
  def read(rs: ResultSet, column: Int): T
}

object ResultSetReader {
  def readAs[T](rs: ResultSet, column: Int)(implicit r: ResultSetReader[T]): T = r.read(rs, column)

  def readOrElse[T](rs: ResultSet, column: Int, default: => T)(implicit r: ResultSetReader[Option[T]]): T = {
    r.read(rs, column).getOrElse(default)
  }
}
