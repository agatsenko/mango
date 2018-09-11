/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-11
  */
package io.mango.sql

import io.mango.common.util.SimpleValExt.{ByteExt, DoubleExt, FloatExt, IntExt, LongExt, ShortExt}
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks
import java.{math => jm}
import java.sql.{Date, Time, Timestamp}
import java.time._

import io.mango.sql.test.infrastructure.OffsetDateTimeExt.OffsetDateTimeExt
import io.mango.sql.test.infrastructure.ZonedDateTimeExt.ZonedDateTimeExt

class SqlValueConverterTests extends FunSuite with TableDrivenPropertyChecks with Matchers {
  test("test toSqlValue") {
    val testData = Table[Any, Either[Class[_], Any]](
      ("srcValue", "expectedSqlValue"),
      (2.toByte, Right(2.toByte)),
      (2.toByte.box, Right(2.toByte.box)),
      (2.toShort, Right(2.toShort)),
      (2.toShort.box, Right(2.toShort.box)),
      (2, Right(2)),
      (2.box, Right(2.box)),
      (2L, Right(2L)),
      (2L.box, Right(2L.box)),
      (2f, Right(2f)),
      (2f.box, Right(2f.box)),
      (2.1, Right(2.1)),
      (2.1.box, Right(2.1.box)),
      (BigInt(2), Right(2L)),
      (BigInt("9223372036854775807123456789"), Left(classOf[ArithmeticException])),
      (jm.BigInteger.valueOf(2), Right(2L)),
      (new jm.BigInteger("9223372036854775807123456789"), Left(classOf[ArithmeticException])),
      (BigDecimal(2), Right(new jm.BigDecimal(2))),
      (new jm.BigDecimal(2), Right(new jm.BigDecimal(2))),
      ("string", Right("string")),
      (LocalTime.of(1, 2, 3), Right(Time.valueOf(LocalTime.of(1, 2, 3)))),
      (LocalDate.of(2018, 9, 11), Right(Date.valueOf(LocalDate.of(2018, 9, 11)))),
      (LocalDateTime.of(2018, 9, 11, 1, 2), Right(Timestamp.valueOf(LocalDateTime.of(2018, 9, 11, 1, 2)))),
      (
          ZonedDateTime.of(LocalDate.of(2018, 9, 11), LocalTime.of(1, 2, 3), ZoneOffset.of("-04:05")),
          Right(
            ZonedDateTime.of(LocalDate.of(2018, 9, 11), LocalTime.of(1, 2, 3), ZoneOffset.of("-04:05")).toSqlTimestamp
          )
      ),
      (
          OffsetDateTime.of(LocalDate.of(2018, 9, 11), LocalTime.of(1, 2, 3), ZoneOffset.of("-04:05")),
          Right(
            OffsetDateTime.of(LocalDate.of(2018, 9, 11), LocalTime.of(1, 2, 3), ZoneOffset.of("-04:05")).toSqlTimestamp
          )
      ),
      (null, Right(null)),
      (None, Right(null)),
      (Some(10), Right(10)),
      (Some(LocalTime.of(1, 2, 3)), Right(Time.valueOf(LocalTime.of(1, 2, 3))))
    )

    forAll(testData) { (srcValue, expectedSqlValue) =>
      expectedSqlValue match {
        case Right(v) => assert(SqlValueConverter.toSqlValue(srcValue) == v)
        case Left(exType) =>
          intercept[Throwable](SqlValueConverter.toSqlValue(srcValue)).getClass == exType
      }
    }
  }
}
