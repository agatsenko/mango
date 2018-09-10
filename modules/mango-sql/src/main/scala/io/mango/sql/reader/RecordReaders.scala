/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.sql.reader

import java.{lang => jl}
import java.{math => jm}

import io.mango.sql.SqlRecordReader

trait RecordReaders {
  //#region Boolean / java.lang.Boolean readers

  implicit val booleanReader: SqlRecordReader[Boolean] = (rs, c) => {
    val value = rs.getBoolean(c)
    if (rs.wasNull()) {
      throw new IllegalStateException(s"unable to read #$c column because value is null")
    }
    value
  }

  implicit val booleanOptReader: SqlRecordReader[Option[Boolean]] = (rs, c) => {
    val value = rs.getBoolean(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val booleanRefReader: SqlRecordReader[jl.Boolean] = (rs, c) => {
    val value = rs.getBoolean(c)
    if (rs.wasNull()) null else value
  }

  //#endregion Boolean / java.lang.Boolean readers

  //#region Byte / java.lang.Byte readers

  implicit val byteReader: SqlRecordReader[Byte] = (rs, c) => {
    val value = rs.getByte(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    value
  }

  implicit val byteOptReader: SqlRecordReader[Option[Byte]] = (rs, c) => {
    val value = rs.getByte(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val byteRefReader: SqlRecordReader[jl.Byte] = (rs, c) => {
    val value = rs.getByte(c)
    if (rs.wasNull()) null else value
  }

  //#endregion Byte / java.lang.Byte readers

  //#region Short / java.lang.Short readers

  implicit val shortReader: SqlRecordReader[Short] = (rs, c) => {
    val value = rs.getShort(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    value
  }

  implicit val shortOptReader: SqlRecordReader[Option[Short]] = (rs, c) => {
    val value = rs.getShort(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val shortRefReader: SqlRecordReader[jl.Short] = (rs, c) => {
    val value = rs.getShort(c)
    if (rs.wasNull()) null else value
  }

  //#endregion Short / java.lang.Short readers

  //#region Int / java.lan.Integer readers

  implicit val intReader: SqlRecordReader[Int] = (rs, c) => {
    val value = rs.getInt(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    value
  }

  implicit val intOptReader: SqlRecordReader[Option[Int]] = (rs, c) => {
    val value = rs.getInt(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val intRefReader: SqlRecordReader[jl.Integer] = (rs, c) => {
    val value = rs.getInt(c)
    if (rs.wasNull()) null else value
  }

  //#endregion Int / java.lan.Integer readers

  //#region Long / java.lang.Long readers

  implicit val longReader: SqlRecordReader[Long] = (rs, c) => {
    val value = rs.getLong(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    value
  }

  implicit val longOptReader: SqlRecordReader[Option[Long]] = (rs, c) => {
    val value = rs.getLong(c)
    if (rs.wasNull()) None else Some(value)
  }

  implicit val longRefReader: SqlRecordReader[jl.Long] = (rs, c) => {
    val v = rs.getLong(c)
    if (rs.wasNull()) null else v
  }

  //#endregion Long / java.lang.Long readers

  //#region Float / java.lang.Float readers

  implicit val floatReader: SqlRecordReader[Float] = (rs, c) => {
    val v = rs.getFloat(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    v
  }

  implicit val floatOptReader: SqlRecordReader[Option[Float]] = (rs, c) => {
    val v = rs.getFloat(c)
    if (rs.wasNull()) None else Some(v)
  }

  implicit val floatRefReader: SqlRecordReader[jl.Float] = (rs, c) => {
    val v = rs.getFloat(c)
    if (rs.wasNull()) null else v
  }

  //#endregion Float / java.lang.Float readers

  //#region Double / java.lang.Double readers

  implicit val doubleReader: SqlRecordReader[Double] = (rs, c) => {
    val v = rs.getDouble(c)
    if (rs.wasNull()) {
      throwUnableToReadNull(c)
    }
    v
  }

  implicit val doubleOptReader: SqlRecordReader[Option[Double]] = (rs, c) => {
    val v = rs.getDouble(c)
    if (rs.wasNull()) None else Some(v)
  }

  implicit val doubleRefReader: SqlRecordReader[jl.Double] = (rs, c) => {
    val v = rs.getDouble(c)
    if (rs.wasNull()) null else v
  }

  //#endregion Double / java.lang.Double readers

  //#region BigInt / java.math.BigInteger readers

  implicit val javaBigIntReader: SqlRecordReader[jm.BigInteger] = (rs, c) => {
    rs.getObject(c) match {
      case v: jm.BigInteger => v
      case v: jm.BigDecimal => v.toBigInteger
      case v: Number => jm.BigInteger.valueOf(v.longValue())
      case null => null
      case v => throwUnableToConvert(classOf[jm.BigInteger], v, c)
    }
  }

  implicit val bigIntReader: SqlRecordReader[BigInt] = (rs, c) => {
    javaBigIntReader.read(rs, c) match {
      case null => null
      case v => BigInt(v)
    }
  }

  //#endregion BigInt / java.math.BigInteger readers

  //#region BigDecimal / java.math.BigDecimal readers

  implicit val javaBigDecimalReader: SqlRecordReader[jm.BigDecimal] = (rs, c) => {
    rs.getObject(c) match {
      case v: jm.BigInteger => new jm.BigDecimal(v)
      case v: jm.BigDecimal => v
      case v: Number => new jm.BigDecimal(v.toString)
      case null => null
      case v => throwUnableToConvert(classOf[jm.BigDecimal], v, c)
    }
  }

  implicit val bigDecimalReader: SqlRecordReader[BigDecimal] = (rs, c) => {
    javaBigDecimalReader.read(rs, c) match {
      case null => null
      case v => BigDecimal(v)
    }
  }

  //#endregion BigDecimal / java.math.BigDecimal readers

  private def throwUnableToReadNull(column: Int): Nothing = {
    throw new IllegalStateException(s"unable to read #$column column because value is null")
  }

  private def throwUnableToConvert(valueCls: Class[_], value: Any, column: Int): Nothing = {
    throw new IllegalStateException(s"unable to convert $value to ${valueCls.getCanonicalName} (column: $column)")
  }
}

object RecordReaders extends RecordReaders with OptionRecordReader {
}
