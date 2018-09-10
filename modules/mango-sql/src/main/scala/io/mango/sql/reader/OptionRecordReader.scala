/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.sql.reader

import java.sql.ResultSet

import io.mango.sql.SqlRecordReader

trait OptionRecordReader {
  implicit def optionReader[T <: AnyRef](implicit r: SqlRecordReader[T]): SqlRecordReader[Option[T]] =
    (rs: ResultSet, column: Int) => Option(r.read(rs, column))
}
