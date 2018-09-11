/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.sql.test.infrastructure

import java.sql.{Date, Timestamp}
import java.time._

object OffsetDateTimeExt {
  implicit class OffsetDateTimeExt(val odt: OffsetDateTime) extends AnyVal {
    def toSqlTimestamp: Timestamp = Timestamp.valueOf(LocalDateTime.ofInstant(odt.toInstant, ZoneId.systemDefault()))

    def toSqlDate: Date = Date.valueOf(odt.withOffsetSameInstant(ZonedDateTime.now().getOffset).toLocalDate)

    def toSystemLocalDate: LocalDate = odt.withOffsetSameInstant(ZonedDateTime.now().getOffset).toLocalDate
  }
}
