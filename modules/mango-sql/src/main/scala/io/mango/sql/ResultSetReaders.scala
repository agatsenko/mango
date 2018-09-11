/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.sql

import java.{lang => jl, math => jm, sql => js, util => ju}
import java.time._

trait ResultSetReaders {
  //#region Boolean / java.lang.Boolean readers

  implicit val booleanReader: ResultSetReader[Boolean] = (rs, c) => {
    val value = rs.getBoolean(c)
    if (rs.wasNull()) {
      throw new IllegalStateException(s"unable to read #$c column because value is null")
    }
    value
  }

  implicit val booleanOptReader: ResultSetReader[Option[Boolean]] = (rs, c) => {
    val value = rs.getBoolean(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val booleanRefReader: ResultSetReader[jl.Boolean] = (rs, c) => {
    val value = rs.getBoolean(c)
    if (rs.wasNull()) null else value
  }

  //#endregion Boolean / java.lang.Boolean readers

  //#region Byte / java.lang.Byte readers

  implicit val byteReader: ResultSetReader[Byte] = (rs, c) => {
    val value = rs.getByte(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    value
  }

  implicit val byteOptReader: ResultSetReader[Option[Byte]] = (rs, c) => {
    val value = rs.getByte(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val byteRefReader: ResultSetReader[jl.Byte] = (rs, c) => {
    val value = rs.getByte(c)
    if (rs.wasNull()) null else value
  }

  //#endregion Byte / java.lang.Byte readers

  //#region Short / java.lang.Short readers

  implicit val shortReader: ResultSetReader[Short] = (rs, c) => {
    val value = rs.getShort(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    value
  }

  implicit val shortOptReader: ResultSetReader[Option[Short]] = (rs, c) => {
    val value = rs.getShort(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val shortRefReader: ResultSetReader[jl.Short] = (rs, c) => {
    val value = rs.getShort(c)
    if (rs.wasNull()) null else value
  }

  //#endregion Short / java.lang.Short readers

  //#region Int / java.lan.Integer readers

  implicit val intReader: ResultSetReader[Int] = (rs, c) => {
    val value = rs.getInt(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    value
  }

  implicit val intOptReader: ResultSetReader[Option[Int]] = (rs, c) => {
    val value = rs.getInt(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val intRefReader: ResultSetReader[jl.Integer] = (rs, c) => {
    val value = rs.getInt(c)
    if (rs.wasNull()) null else value
  }

  //#endregion Int / java.lan.Integer readers

  //#region Long / java.lang.Long readers

  implicit val longReader: ResultSetReader[Long] = (rs, c) => {
    val value = rs.getLong(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    value
  }

  implicit val longOptReader: ResultSetReader[Option[Long]] = (rs, c) => {
    val value = rs.getLong(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val longRefReader: ResultSetReader[jl.Long] = (rs, c) => {
    val v = rs.getLong(c)
    if (rs.wasNull()) null else v
  }

  //#endregion Long / java.lang.Long readers

  //#region Float / java.lang.Float readers

  implicit val floatReader: ResultSetReader[Float] = (rs, c) => {
    val v = rs.getFloat(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    v
  }

  implicit val floatOptReader: ResultSetReader[Option[Float]] = (rs, c) => {
    val v = rs.getFloat(c)
    if (rs.wasNull()) None else Some(v)
  }

  implicit val floatRefReader: ResultSetReader[jl.Float] = (rs, c) => {
    val v = rs.getFloat(c)
    if (rs.wasNull()) null else v
  }

  //#endregion Float / java.lang.Float readers

  //#region Double / java.lang.Double readers

  implicit val doubleReader: ResultSetReader[Double] = (rs, c) => {
    val v = rs.getDouble(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    v
  }

  implicit val doubleOptReader: ResultSetReader[Option[Double]] = (rs, c) => {
    val v = rs.getDouble(c)
    if (rs.wasNull()) None else Some(v)
  }

  implicit val doubleRefReader: ResultSetReader[jl.Double] = (rs, c) => {
    val v = rs.getDouble(c)
    if (rs.wasNull()) null else v
  }

  //#endregion Double / java.lang.Double readers

  //#region BigInt / java.math.BigInteger readers

  implicit val javaBigIntReader: ResultSetReader[jm.BigInteger] = (rs, c) => {
    rs.getObject(c) match {
      case v: jm.BigInteger => v
      case v: jm.BigDecimal => v.toBigInteger
      case v: Number => jm.BigInteger.valueOf(v.longValue())
      case null => null
      case v => throwUnableToConvert(classOf[jm.BigInteger], v, c)
    }
  }

  implicit val bigIntReader: ResultSetReader[BigInt] = (rs, c) => {
    javaBigIntReader.read(rs, c) match {
      case null => null
      case v => BigInt(v)
    }
  }

  //#endregion BigInt / java.math.BigInteger readers

  //#region BigDecimal / java.math.BigDecimal readers

  implicit val javaBigDecimalReader: ResultSetReader[jm.BigDecimal] = (rs, c) => {
    rs.getObject(c) match {
      case v: jm.BigInteger => new jm.BigDecimal(v)
      case v: jm.BigDecimal => v
      case v: Number => new jm.BigDecimal(v.toString)
      case null => null
      case v => throwUnableToConvert(classOf[jm.BigDecimal], v, c)
    }
  }

  implicit val bigDecimalReader: ResultSetReader[BigDecimal] = (rs, c) => {
    javaBigDecimalReader.read(rs, c) match {
      case null => null
      case v => BigDecimal(v)
    }
  }

  //#endregion BigDecimal / java.math.BigDecimal readers

  //#region String reader

  implicit val stringReader: ResultSetReader[String] = (rs, c) => rs.getString(c)

  //#endregion String reader

  //#region Date / Time readers

  implicit val dateReader: ResultSetReader[ju.Date] = (rs, c) => {
    rs.getObject(c) match {
      case null => null
      case v: ju.Date => v
      case v => throwUnableToConvert(classOf[ju.Date], v, c)
    }
  }

  implicit val localTimeReader: ResultSetReader[LocalTime] = (rs, c) => {
    rs.getObject(c) match {
      case null => null
      case v: js.Time => v.toLocalTime
      case v: js.Timestamp => v.toLocalDateTime.toLocalTime
      case v => throwUnableToConvert(classOf[LocalTime], v, c)
    }
  }

  implicit val localDateReader: ResultSetReader[LocalDate] = (rs, c) => {
    rs.getObject(c) match {
      case null => null
      case v: js.Timestamp => v.toLocalDateTime.toLocalDate
      case v: js.Date => v.toLocalDate
      case v => throwUnableToConvert(classOf[LocalTime], v, c)
    }
  }

  implicit val localDateTimeReader: ResultSetReader[LocalDateTime] = (rs, c) => {
    rs.getObject(c) match {
      case null => null
      case v: js.Timestamp => v.toLocalDateTime
      case v: js.Date => LocalDateTime.of(v.toLocalDate, LocalTime.MIDNIGHT)
      case v => throwUnableToConvert(classOf[LocalTime], v, c)
    }
  }

  implicit val offsetDateTimeReader: ResultSetReader[OffsetDateTime] = (rs, c) => {
    rs.getObject(c) match {
      case null => null
      case v: js.Timestamp => OffsetDateTime.ofInstant(v.toInstant, ZoneId.systemDefault())
      case v: js.Date => OffsetDateTime.of(v.toLocalDate, LocalTime.MIDNIGHT, OffsetDateTime.now().getOffset)
      case v => throwUnableToConvert(classOf[LocalTime], v, c)
    }
  }

  implicit val zonedDateTimeReader: ResultSetReader[ZonedDateTime] = (rs, c) => {
    rs.getObject(c) match {
      case null => null
      case v: js.Timestamp => ZonedDateTime.of(v.toLocalDateTime, ZoneId.systemDefault())
      case v: js.Date => ZonedDateTime.of(v.toLocalDate, LocalTime.MIDNIGHT, ZoneId.systemDefault())
      case v => throwUnableToConvert(classOf[LocalTime], v, c)
    }
  }

  //#endregion Date / Time readers

  private def throwUnableToReadNull(column: Int): Nothing = {
    throw new IllegalStateException(s"unable to read #$column column because value is null")
  }

  private def throwUnableToConvert(valueCls: Class[_], value: Any, column: Int): Nothing = {
    throw new IllegalStateException(
      s"unable to convert '$value' to ${valueCls.getCanonicalName} type (column: $column)"
    )
  }
}

object ResultSetReaders extends ResultSetReaders with OptionResultSetReader {
}
