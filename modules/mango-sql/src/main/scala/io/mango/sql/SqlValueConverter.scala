/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-01-31
  */
package io.mango.sql

import java.{math => jm, sql => jsql}
import java.time._

trait SqlValueConverter {
  def toSqlValue(value: Any): Any = {
    val refinedValue = value match {
      case None => null
      case Some(v) => v
      case v => v
    }

    refinedValue match {
      case null => null
      case v: BigInt => v.bigInteger.longValueExact()
      case v: jm.BigInteger => v.longValueExact()
      case v: BigDecimal => v.bigDecimal
      case v: LocalTime => toSqlTime(v)
      case v: LocalDate => toSqlDate(v)
      case v: LocalDateTime => toSqlTimestamp(v)
      case v: ZonedDateTime => toSqlTimestamp(v)
      case v: OffsetDateTime => toSqlTimestamp(v)
      case v => v
    }
  }

  protected def toSqlTime(v: LocalTime): jsql.Time = if (v == null) null else jsql.Time.valueOf(v)

  protected def toSqlDate(v: LocalDate): jsql.Date = if (v == null) null else jsql.Date.valueOf(v)

  protected def toSqlTimestamp(v: LocalDateTime): jsql.Timestamp = if (v == null) null else jsql.Timestamp.valueOf(v)

  protected def toSqlTimestamp(v: ZonedDateTime): jsql.Timestamp = {
    if (v == null) null else jsql.Timestamp.from(v.toInstant)
  }

  protected def toSqlTimestamp(v: OffsetDateTime): jsql.Timestamp = {
    if (v == null) null else jsql.Timestamp.from(v.toInstant)
  }
}

object SqlValueConverter extends SqlValueConverter {
}
