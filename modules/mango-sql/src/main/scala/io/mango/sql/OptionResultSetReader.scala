/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.sql

import java.sql.ResultSet

trait OptionResultSetReader {
  implicit def optionReader[T <: AnyRef](implicit r: ResultSetReader[T]): ResultSetReader[Option[T]] =
    (rs: ResultSet, column: Int) => Option(r.read(rs, column))
}
