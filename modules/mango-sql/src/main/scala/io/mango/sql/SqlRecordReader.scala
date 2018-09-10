/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.sql

import java.sql.ResultSet

trait SqlRecordReader[+T] {
  def read(rs: ResultSet, column: Int): T
}

object SqlRecordReader {
  def readAs[T](rs: ResultSet, column: Int)(implicit r: SqlRecordReader[T]): T = r.read(rs, column)

  def readOrElse[T](rs: ResultSet, column: Int, default: => T)(implicit r: SqlRecordReader[Option[T]]): T = {
    r.read(rs, column).getOrElse(default)
  }
}
