/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.sql.test.infrastructure

import java.sql.{Date, Timestamp}
import java.time.{LocalDate, ZonedDateTime, ZoneId}

object ZonedDateTimeExt {
  implicit class ZonedDateTimeExt(val zdt: ZonedDateTime) extends AnyVal {
    def toSqlTimestamp: Timestamp = Timestamp.valueOf(zdt.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime)

    def toSqlDate: Date = Date.valueOf(zdt.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate)

    def toSystemLocalDate: LocalDate = zdt.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate
  }
}
