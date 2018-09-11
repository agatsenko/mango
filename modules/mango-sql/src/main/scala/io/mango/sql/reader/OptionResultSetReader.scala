/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.sql.reader

import java.sql.ResultSet

import io.mango.sql.ResultSetReader

trait OptionResultSetReader {
  implicit def optionReader[T <: AnyRef](implicit r: ResultSetReader[T]): ResultSetReader[Option[T]] =
    (rs: ResultSet, column: Int) => Option(r.read(rs, column))
}
