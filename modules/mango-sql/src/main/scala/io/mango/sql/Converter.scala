/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-01-31
  */
package io.mango.sql

import scala.reflect.ClassTag

import java.{lang => jl, math => jm, sql => jsql}
import java.time._

object Converter {
  def toSqlType(tpe: Class[_]): Class[_] = {
    tpe match {
      case Types.BOOL_VAL => Types.BOOL_REF
      case Types.BYTE_VAL => Types.BYTE_REF
      case Types.SHORT_VAL => Types.SHORT_REF
      case Types.INT_VAL => Types.INT_REF
      case Types.LONG_VAL => Types.LONG_REF
      case Types.FLOAT_VAL => Types.FLOAT_REF
      case Types.DOUBLE_VAL => Types.DOUBLE_REF
      case Types.CHAR_VAL => Types.CHAR_REF
      case Types.BIG_INT_SCALA => Types.LONG_REF
      case Types.BIG_INT_JAVA => Types.LONG_REF
      case Types.BIG_DECIMAL_SCALA => Types.BIG_DECIMAL_JAVA
      case Types.LOCAL_TIME => Types.SQL_TIME
      case Types.LOCAL_DATE => Types.SQL_DATE
      case Types.LOCAL_DATE_TIME => Types.SQL_TIMESTAMP
      case Types.ZONED_DATE_TIME => Types.SQL_TIMESTAMP
      case Types.OFFSET_DATE_TIME => Types.SQL_TIMESTAMP
      case _ => tpe
    }
  }

  def toSqlValue(value: Any): Any = {
    val refinedValue = value match {
      case None => null
      case Some(v) => v
      case v => v
    }

    refinedValue match {
      case null => null
      case v: BigInt => v.longValue()
      case v: jm.BigInteger => v.longValue()
      case v: BigDecimal => v.bigDecimal
      case v: LocalTime => toSqlTime(v)
      case v: LocalDate => toSqlDate(v)
      case v: LocalDateTime => toSqlTimestamp(v)
      case v: ZonedDateTime => toSqlTimestamp(v)
      case v: OffsetDateTime => toSqlTimestamp(v)
      case v => v
    }
  }

  def fromSqlValue[T](sqlValue: Any)(implicit ct: ClassTag[T]): Option[T] = {
    val valueType = ct.runtimeClass

    sqlValue match {
      case null => None
      case bool: jl.Boolean => valueType match {
        case Types.BOOL_VAL |
             Types.BOOL_REF => Some(bool).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(bool)).asInstanceOf[Option[T]]
      }
      case byte: jl.Byte => valueType match {
        case Types.BYTE_VAL |
             Types.BYTE_REF => Some(byte).asInstanceOf[Option[T]]
        case Types.SHORT_VAL |
             Types.SHORT_REF => Some(byte.shortValue()).asInstanceOf[Option[T]]
        case Types.INT_VAL |
             Types.INT_REF => Some(byte.intValue()).asInstanceOf[Option[T]]
        case Types.LONG_VAL |
             Types.LONG_REF => Some(byte.longValue()).asInstanceOf[Option[T]]
        case Types.FLOAT_VAL |
             Types.FLOAT_REF => Some(byte.floatValue()).asInstanceOf[Option[T]]
        case Types.DOUBLE_VAL |
             Types.DOUBLE_REF => Some(byte.doubleValue()).asInstanceOf[Option[T]]
        case Types.BIG_INT_JAVA => Some(jm.BigInteger.valueOf(byte.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_INT_SCALA => Some(BigInt(byte.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_JAVA => Some(jm.BigDecimal.valueOf(byte.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_SCALA => Some(BigDecimal(byte.longValue())).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(byte)).asInstanceOf[Option[T]]
      }
      case short: jl.Short => valueType match {
        case Types.SHORT_VAL |
             Types.SHORT_REF => Some(short).asInstanceOf[Option[T]]
        case Types.INT_VAL |
             Types.INT_REF => Some(short.intValue()).asInstanceOf[Option[T]]
        case Types.LONG_VAL |
             Types.LONG_REF => Some(short.longValue()).asInstanceOf[Option[T]]
        case Types.FLOAT_VAL |
             Types.FLOAT_REF => Some(short.floatValue()).asInstanceOf[Option[T]]
        case Types.DOUBLE_VAL |
             Types.DOUBLE_REF => Some(short.doubleValue()).asInstanceOf[Option[T]]
        case Types.BIG_INT_JAVA => Some(jm.BigInteger.valueOf(short.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_INT_SCALA => Some(BigInt(short.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_JAVA => Some(jm.BigDecimal.valueOf(short.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_SCALA => Some(BigDecimal(short.longValue())).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(short)).asInstanceOf[Option[T]]
      }
      case int: jl.Integer => valueType match {
        case Types.INT_VAL |
             Types.INT_REF => Some(int).asInstanceOf[Option[T]]
        case Types.LONG_VAL |
             Types.LONG_REF => Some(int.longValue()).asInstanceOf[Option[T]]
        case Types.FLOAT_VAL |
             Types.FLOAT_REF => Some(int.floatValue()).asInstanceOf[Option[T]]
        case Types.DOUBLE_VAL |
             Types.DOUBLE_REF => Some(int.doubleValue()).asInstanceOf[Option[T]]
        case Types.BIG_INT_JAVA => Some(jm.BigInteger.valueOf(int.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_INT_SCALA => Some(BigInt(int.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_JAVA => Some(jm.BigDecimal.valueOf(int.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_SCALA => Some(BigDecimal(int.longValue())).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(int)).asInstanceOf[Option[T]]
      }
      case long: jl.Long => valueType match {
        case Types.LONG_VAL |
             Types.LONG_REF => Some(long).asInstanceOf[Option[T]]
        case Types.FLOAT_VAL |
             Types.FLOAT_REF => Some(long.floatValue()).asInstanceOf[Option[T]]
        case Types.DOUBLE_VAL |
             Types.DOUBLE_REF => Some(long.doubleValue()).asInstanceOf[Option[T]]
        case Types.BIG_INT_JAVA => Some(jm.BigInteger.valueOf(long.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_INT_SCALA => Some(BigInt(long.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_JAVA => Some(jm.BigDecimal.valueOf(long.longValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_SCALA => Some(BigDecimal(long.longValue())).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(long)).asInstanceOf[Option[T]]
      }
      case float: jl.Float => valueType match {
        case Types.FLOAT_VAL |
             Types.FLOAT_REF => Some(float).asInstanceOf[Option[T]]
        case Types.DOUBLE_VAL |
             Types.DOUBLE_REF => Some(float.doubleValue()).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_JAVA => Some(jm.BigDecimal.valueOf(float.doubleValue())).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_SCALA => Some(BigDecimal(float.doubleValue())).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(float)).asInstanceOf[Option[T]]
      }
      case double: jl.Double => valueType match {
        case Types.DOUBLE_VAL |
             Types.DOUBLE_REF => Some(double).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_JAVA => Some(jm.BigDecimal.valueOf(double)).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_SCALA => Some(BigDecimal(double)).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(double)).asInstanceOf[Option[T]]
      }
      case decimal: jm.BigDecimal => valueType match {
        case Types.BIG_DECIMAL_JAVA => Some(decimal).asInstanceOf[Option[T]]
        case Types.BIG_DECIMAL_SCALA => Some(BigDecimal(decimal)).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(decimal)).asInstanceOf[Option[T]]
      }
      case char: jl.Character => valueType match {
        case Types.CHAR_VAL |
             Types.CHAR_REF => Some(char).asInstanceOf[Option[T]]
        case Types.STRING => Some(char.toString).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(char)).asInstanceOf[Option[T]]
      }
      case sqlTime: jsql.Time => valueType match {
        case Types.SQL_TIME => Some(sqlTime).asInstanceOf[Option[T]]
        case Types.LOCAL_TIME => Some(toLocalTime(sqlTime)).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(sqlTime)).asInstanceOf[Option[T]]
      }
      case sqlDate: jsql.Date => valueType match {
        case Types.SQL_DATE => Some(sqlDate).asInstanceOf[Option[T]]
        case Types.LOCAL_DATE => Some(toLocalDate(sqlDate)).asInstanceOf[Option[T]]
        case Types.LOCAL_DATE_TIME => Some(toLocalDateTime(sqlDate)).asInstanceOf[Option[T]]
        case Types.ZONED_DATE_TIME => Some(toZonedDateTime(sqlDate)).asInstanceOf[Option[T]]
        case Types.OFFSET_DATE_TIME => Some(toOffsetDateTime(sqlDate)).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(sqlDate)).asInstanceOf[Option[T]]
      }
      case sqlTimestamp: jsql.Timestamp => valueType match {
        case Types.SQL_TIMESTAMP => Some(sqlTimestamp).asInstanceOf[Option[T]]
        case Types.LOCAL_TIME => Some(toLocalTime(sqlTimestamp)).asInstanceOf[Option[T]]
        case Types.LOCAL_DATE => Some(toLocalDate(sqlTimestamp)).asInstanceOf[Option[T]]
        case Types.LOCAL_DATE_TIME => Some(toLocalDateTime(sqlTimestamp)).asInstanceOf[Option[T]]
        case Types.ZONED_DATE_TIME => Some(toZonedDateTime(sqlTimestamp)).asInstanceOf[Option[T]]
        case Types.OFFSET_DATE_TIME => Some(toOffsetDateTime(sqlTimestamp)).asInstanceOf[Option[T]]
        case _ => Some(valueType.cast(sqlTimestamp)).asInstanceOf[Option[T]]
      }
      case value => Some(valueType.cast(value)).asInstanceOf[Option[T]]
    }
  }

  private def toSqlTime(v: LocalTime): jsql.Time = if (v == null) null else jsql.Time.valueOf(v)

  private def toSqlDate(v: LocalDate): jsql.Date = if (v == null) null else jsql.Date.valueOf(v)

  private def toSqlTimestamp(v: LocalDateTime): jsql.Timestamp = if (v == null) null else jsql.Timestamp.valueOf(v)

  private def toSqlTimestamp(v: ZonedDateTime): jsql.Timestamp =
    if (v == null) null else jsql.Timestamp.from(v.toInstant)

  private def toSqlTimestamp(v: OffsetDateTime): jsql.Timestamp =
    if (v == null) null else jsql.Timestamp.from(v.toInstant)

  private def toLocalTime(v: jsql.Time): LocalTime = if (v == null) null else v.toLocalTime

  private def toLocalTime(v: jsql.Timestamp): LocalTime = if (v == null) null else v.toLocalDateTime.toLocalTime

  private def toLocalDate(v: jsql.Date): LocalDate = if (v == null) null else v.toLocalDate

  private def toLocalDate(v: jsql.Timestamp): LocalDate = if (v == null) null else v.toLocalDateTime.toLocalDate

  private def toLocalDateTime(v: jsql.Date): LocalDateTime =
    if (v == null) null else LocalDateTime.of(v.toLocalDate, LocalTime.MIDNIGHT)

  private def toLocalDateTime(v: jsql.Timestamp): LocalDateTime = if (v == null) null else v.toLocalDateTime

  private def toZonedDateTime(v: jsql.Date): ZonedDateTime =
    if (v == null) null else ZonedDateTime.of(v.toLocalDate, LocalTime.MIDNIGHT, ZoneId.systemDefault())

  private def toZonedDateTime(v: jsql.Timestamp): ZonedDateTime =
    if (v == null) null else ZonedDateTime.ofInstant(v.toInstant, ZoneId.systemDefault())

  private def toOffsetDateTime(v: jsql.Date): OffsetDateTime =
    if (v == null) null else OffsetDateTime.of(v.toLocalDate, LocalTime.MIDNIGHT, OffsetDateTime.now().getOffset)

  private def toOffsetDateTime(v: jsql.Timestamp): OffsetDateTime =
    if (v == null) null else OffsetDateTime.ofInstant(v.toInstant, ZoneId.systemDefault())

  private[sql] object Types {
    val BOOL_VAL: Class[jl.Boolean] = jl.Boolean.TYPE
    val BOOL_REF: Class[jl.Boolean] = classOf[jl.Boolean]
    val BYTE_VAL: Class[jl.Byte] = jl.Byte.TYPE
    val BYTE_REF: Class[jl.Byte] = classOf[jl.Byte]
    val SHORT_VAL: Class[jl.Short] = jl.Short.TYPE
    val SHORT_REF: Class[jl.Short] = classOf[jl.Short]
    val INT_VAL: Class[jl.Integer] = jl.Integer.TYPE
    val INT_REF: Class[jl.Integer] = classOf[jl.Integer]
    val LONG_VAL: Class[jl.Long] = jl.Long.TYPE
    val LONG_REF: Class[jl.Long] = classOf[jl.Long]
    val FLOAT_VAL: Class[jl.Float] = jl.Float.TYPE
    val FLOAT_REF: Class[jl.Float] = classOf[jl.Float]
    val DOUBLE_VAL: Class[jl.Double] = jl.Double.TYPE
    val DOUBLE_REF: Class[jl.Double] = classOf[jl.Double]
    val BIG_INT_SCALA: Class[BigInt] = classOf[BigInt]
    val BIG_INT_JAVA: Class[jm.BigInteger] = classOf[jm.BigInteger]
    val BIG_DECIMAL_SCALA: Class[BigDecimal] = classOf[BigDecimal]
    val BIG_DECIMAL_JAVA: Class[jm.BigDecimal] = classOf[jm.BigDecimal]
    val CHAR_VAL: Class[jl.Character] = jl.Character.TYPE
    val CHAR_REF: Class[jl.Character] = classOf[jl.Character]
    val STRING: Class[String] = classOf[String]
    val SQL_TIME: Class[jsql.Time] = classOf[jsql.Time]
    val SQL_DATE: Class[jsql.Date] = classOf[jsql.Date]
    val SQL_TIMESTAMP: Class[jsql.Timestamp] = classOf[jsql.Timestamp]
    val LOCAL_TIME: Class[LocalTime] = classOf[LocalTime]
    val LOCAL_DATE: Class[LocalDate] = classOf[LocalDate]
    val LOCAL_DATE_TIME: Class[LocalDateTime] = classOf[LocalDateTime]
    val OFFSET_DATE_TIME: Class[OffsetDateTime] = classOf[OffsetDateTime]
    val ZONED_DATE_TIME: Class[ZonedDateTime] = classOf[ZonedDateTime]
  }
}
