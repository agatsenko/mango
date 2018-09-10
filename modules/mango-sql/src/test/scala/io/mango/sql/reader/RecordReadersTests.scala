/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.sql.reader

import java.{lang => jl, math => jm}
import java.sql.ResultSet

import io.mango.sql.SqlRecordReader
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class RecordReadersTests extends FunSuite with TableDrivenPropertyChecks with Matchers with MockFactory {
  import io.mango.sql.reader.RecordReaders._
  import io.mango.common.util.SimpleValExt._

  //#region read Boolean / java.lan.Boolean

  test("read not null boolean") {
    val testData = Table(
      ("column", "rsValue"),
      (1, false),
      (2, true)
    )

    forAll(testData) { (column, rsValue) =>
      val rs = stub[ResultSet]
      (rs.wasNull _).when().returns(false)
      (rs.getBoolean(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[Boolean](rs, column) == rsValue)
    }
  }

  test("read null boolean") {
    val rs = stub[ResultSet]
    val column = 2
    val value = false
    val wasNull = true

    (rs.getBoolean(_: Int)).when(column).returns(value)
    (rs.wasNull _).when().returns(wasNull)

    intercept[IllegalStateException](SqlRecordReader.readAs[Boolean](rs, column))
  }

  test("read optional boolean") {
    val testData = Table(
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, false, false, Option(false)),
      (2, true, false, Option(true)),
      (3, false, true, None)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getBoolean(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[Option[Boolean]](rs, column) == expectedValue)
    }
  }

  test("read ref boolean") {
    val testData = Table(
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, false, false, jl.Boolean.valueOf(false)),
      (2, true, false, jl.Boolean.valueOf(true)),
      (3, false, true, null)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getBoolean(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[jl.Boolean](rs, column) == expectedValue)
    }
  }

  //#endregion read Boolean / java.lan.Boolean

  //#region read Byte / java.lang.Byte

  test("read not null byte") {
    val testData = Table[Int, Byte](
      ("column", "rsValue"),
      (1, 0),
      (2, 20)
    )

    forAll(testData) { (column, rsValue) =>
      val rs = stub[ResultSet]
      (rs.wasNull _).when().returns(false)
      (rs.getByte(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[Byte](rs, column) == rsValue)
    }
  }

  test("read null byte") {
    val rs = stub[ResultSet]
    val column = 2
    val value: Byte = 0
    val wasNull = true

    (rs.getByte(_: Int)).when(column).returns(value)
    (rs.wasNull _).when().returns(wasNull)

    intercept[IllegalStateException](SqlRecordReader.readAs[Short](rs, column))
  }

  test("read optional byte") {
    val testData = Table[Int, Byte, Boolean, Option[Byte]](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, Option(0.toByte)),
      (2, 20, false, Option(20.toByte)),
      (3, 0, true, None)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getByte(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[Option[Byte]](rs, column) == expectedValue)
    }
  }

  test("read ref byte") {
    val testData = Table[Int, Byte, Boolean, jl.Byte](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, jl.Byte.valueOf(0.toByte)),
      (2, 10, false, jl.Byte.valueOf(10.toByte)),
      (3, 0, true, null)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getByte(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[jl.Byte](rs, column) == expectedValue)
    }
  }

  //#endregion read Byte / java.lang.Byte

  //#region read Short / java.lang.Short

  test("read not null short") {
    val testData = Table[Int, Short](
      ("column", "rsValue"),
      (1, 0),
      (2, 20)
    )

    forAll(testData) { (column, rsValue) =>
      val rs = stub[ResultSet]
      (rs.wasNull _).when().returns(false)
      (rs.getShort(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[Short](rs, column) == rsValue)
    }
  }

  test("read null short") {
    val rs = stub[ResultSet]
    val column = 2
    val value: Short = 0
    val wasNull = true

    (rs.getShort(_: Int)).when(column).returns(value)
    (rs.wasNull _).when().returns(wasNull)

    intercept[IllegalStateException](SqlRecordReader.readAs[Short](rs, column))
  }

  test("read optional short") {
    val testData = Table[Int, Short, Boolean, Option[Short]](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, Option(0.toShort)),
      (2, 20, false, Option(20.toShort)),
      (3, 0, true, None)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getShort(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[Option[Short]](rs, column) == expectedValue)
    }
  }

  test("read ref short") {
    val testData = Table[Int, Short, Boolean, jl.Short](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, jl.Short.valueOf(0.toShort)),
      (2, 10, false, jl.Short.valueOf(10.toShort)),
      (3, 0, true, null)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getShort(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[jl.Short](rs, column) == expectedValue)
    }
  }

  //#endregion read Short / java.lang.Short

  //#region read Int / java.lang.Integer

  test("read not null int") {
    val testData = Table[Int, Int](
      ("column", "rsValue"),
      (1, 0),
      (2, 20)
    )

    forAll(testData) { (column, rsValue) =>
      val rs = stub[ResultSet]
      (rs.wasNull _).when().returns(false)
      (rs.getInt(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[Int](rs, column) == rsValue)
    }
  }

  test("read null int") {
    val rs = stub[ResultSet]
    val column = 2
    val value: Int = 0
    val wasNull = true

    (rs.getInt(_: Int)).when(column).returns(value)
    (rs.wasNull _).when().returns(wasNull)

    intercept[IllegalStateException](SqlRecordReader.readAs[Int](rs, column))
  }

  test("read optional int") {
    val testData = Table[Int, Int, Boolean, Option[Int]](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, Option(0)),
      (2, 20, false, Option(20)),
      (3, 0, true, None)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getInt(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[Option[Int]](rs, column) == expectedValue)
    }
  }

  test("read ref int") {
    val testData = Table[Int, Int, Boolean, jl.Integer](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, jl.Integer.valueOf(0)),
      (2, 10, false, jl.Integer.valueOf(10)),
      (3, 0, true, null)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getInt(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[jl.Integer](rs, column) == expectedValue)
    }
  }

  //#endregion read Int / java.lang.Integer

  //#region read Long / java.lang.Long

  test("read not null long") {
    val testData = Table[Int, Long](
      ("column", "rsValue"),
      (1, 0),
      (2, 20)
    )

    forAll(testData) { (column, rsValue) =>
      val rs = stub[ResultSet]
      (rs.wasNull _).when().returns(false)
      (rs.getLong(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[Long](rs, column) == rsValue)
    }
  }

  test("read null long") {
    val rs = stub[ResultSet]
    val column = 2
    val value: Long = 0
    val wasNull = true

    (rs.getLong(_: Int)).when(column).returns(value)
    (rs.wasNull _).when().returns(wasNull)

    intercept[IllegalStateException](SqlRecordReader.readAs[Long](rs, column))
  }

  test("read optional long") {
    val testData = Table[Int, Long, Boolean, Option[Long]](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, Option(0.toLong)),
      (2, 20, false, Option(20.toLong)),
      (3, 0, true, None)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getLong(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[Option[Long]](rs, column) == expectedValue)
    }
  }

  test("read ref long") {
    val testData = Table[Int, Long, Boolean, jl.Long](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, jl.Long.valueOf(0.toLong)),
      (2, 10, false, jl.Long.valueOf(10.toLong)),
      (3, 0, true, null)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getLong(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[jl.Long](rs, column) == expectedValue)
    }
  }

  //#endregion read Long / java.lang.Long

  //#region read Float / java.lang.Float

  test("read not null float") {
    val testData = Table[Int, Float](
      ("column", "rsValue"),
      (1, 0),
      (2, 20)
    )

    forAll(testData) { (column, rsValue) =>
      val rs = stub[ResultSet]
      (rs.wasNull _).when().returns(false)
      (rs.getFloat(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[Float](rs, column) == rsValue)
    }
  }

  test("read null float") {
    val rs = stub[ResultSet]
    val column = 2
    val value: Float = 0
    val wasNull = true

    (rs.getFloat(_: Int)).when(column).returns(value)
    (rs.wasNull _).when().returns(wasNull)

    intercept[IllegalStateException](SqlRecordReader.readAs[Float](rs, column))
  }

  test("read optional float") {
    val testData = Table[Int, Float, Boolean, Option[Float]](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, Option(0.toFloat)),
      (2, 20, false, Option(20.toFloat)),
      (3, 0, true, None)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getFloat(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[Option[Float]](rs, column) == expectedValue)
    }
  }

  test("read ref float") {
    val testData = Table[Int, Float, Boolean, jl.Float](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, jl.Float.valueOf(0.toFloat)),
      (2, 10, false, jl.Float.valueOf(10.toFloat)),
      (3, 0, true, null)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getFloat(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[jl.Float](rs, column) == expectedValue)
    }
  }

  //#endregion read Float / java.lang.Float

  //#region read Double / java.lang.Double

  test("read not null double") {
    val testData = Table[Int, Double](
      ("column", "rsValue"),
      (1, 0),
      (2, 20)
    )

    forAll(testData) { (column, rsValue) =>
      val rs = stub[ResultSet]
      (rs.wasNull _).when().returns(false)
      (rs.getDouble(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[Double](rs, column) == rsValue)
    }
  }

  test("read null double") {
    val rs = stub[ResultSet]
    val column = 2
    val value: Double = 0
    val wasNull = true

    (rs.getDouble(_: Int)).when(column).returns(value)
    (rs.wasNull _).when().returns(wasNull)

    intercept[IllegalStateException](SqlRecordReader.readAs[Double](rs, column))
  }

  test("read optional double") {
    val testData = Table[Int, Double, Boolean, Option[Double]](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, Option(0.toDouble)),
      (2, 20, false, Option(20.toDouble)),
      (3, 0, true, None)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getDouble(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[Option[Double]](rs, column) == expectedValue)
    }
  }

  test("read ref double") {
    val testData = Table[Int, Double, Boolean, jl.Double](
      ("column", "rsValue", "wasNull", "expectedValue"),
      (1, 0, false, jl.Double.valueOf(0.toDouble)),
      (2, 10, false, jl.Double.valueOf(10.toDouble)),
      (3, 0, true, null)
    )

    forAll(testData) { (column, rsValue, wasNull, expectedValue) =>
      val rs = stub[ResultSet]
      (rs.getDouble(_: Int)).when(column).returns(rsValue)
      (rs.wasNull _).when().returns(wasNull)

      assert(SqlRecordReader.readAs[jl.Double](rs, column) == expectedValue)
    }
  }

  //#endregion read Double / java.lang.Double

  //#region read BigInt / java.math.BigInteger

  test("read number to java.math.BigInteger") {
    val testData = Table[Number, jm.BigInteger](
      ("rsValue", "expectedValue"),
      (10.toByte, jm.BigInteger.valueOf(10)),
      (10.toShort, jm.BigInteger.valueOf(10)),
      (10, jm.BigInteger.valueOf(10)),
      (10L, jm.BigInteger.valueOf(10)),
      (10.2f, jm.BigInteger.valueOf(10.2f.toLong)),
      (10.2, jm.BigInteger.valueOf(10.2.toLong)),
      (jm.BigInteger.valueOf(10L), jm.BigInteger.valueOf(10)),
      (jm.BigDecimal.valueOf(10.2), BigDecimal(10.2).toBigInt().bigInteger)
    )

    forAll(testData) { (rsValue, expectedValue) =>
      val column = 3
      val rs = stub[ResultSet]
      (rs.getObject(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[jm.BigInteger](rs, column) == expectedValue)
    }
  }

  test("read number to java.math.BigInt") {
    val testData = Table[Number, BigInt](
      ("rsValue", "expectedValue"),
      (10.toByte, BigInt(10)),
      (10.toShort, BigInt(10)),
      (10, BigInt(10)),
      (10L, BigInt(10)),
      (10.2f, BigInt(10.2f.toLong)),
      (10.2, BigInt(10.2.toLong)),
      (jm.BigInteger.valueOf(10L), BigInt(10)),
      (jm.BigDecimal.valueOf(10.2), BigDecimal(10.2).toBigInt())
    )

    forAll(testData) { (rsValue, expectedValue) =>
      val column = 3
      val rs = stub[ResultSet]
      (rs.getObject(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[BigInt](rs, column) == expectedValue)
    }
  }

  test("read non-number to BigInt / java.math.BigInt") {
    val column = 1

    val rs = stub[ResultSet]
    (rs.getObject(_: Int)).when(column).returns(new Object)

    intercept[IllegalStateException](SqlRecordReader.readAs[jm.BigInteger](rs, column))
    intercept[IllegalStateException](SqlRecordReader.readAs[BigInt](rs, column))
  }

  //#endregion read BigInt / java.math.BigInteger

  //#region read BigDecimal / java.math.BigDecimal

  test("read number to java.math.BigDecimal") {
    val testData = Table[Number, jm.BigDecimal](
      ("rsValue", "expectedValue"),
      (10.toByte, jm.BigDecimal.valueOf(10)),
      (10.toShort, jm.BigDecimal.valueOf(10)),
      (10, jm.BigDecimal.valueOf(10)),
      (10L, jm.BigDecimal.valueOf(10)),
      (10.2f, new jm.BigDecimal(10.2f.toString)),
      (10.2, new jm.BigDecimal(10.2.toString)),
      (jm.BigInteger.valueOf(10L), new jm.BigDecimal(jm.BigInteger.valueOf(10L))),
      (new jm.BigDecimal(10.2), new jm.BigDecimal(10.2))
    )

    forAll(testData) { (rsValue, expectedValue) =>
      val column = 3
      val rs = stub[ResultSet]
      (rs.getObject(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[jm.BigDecimal](rs, column) == expectedValue)
    }
  }

  test("read number to BigDecimal") {
    val testData = Table[Number, BigDecimal](
      ("rsValue", "expectedValue"),
      (10.toByte, BigDecimal(10)),
      (10.toShort, BigDecimal(10)),
      (10, BigDecimal(10)),
      (10L, BigDecimal(10)),
      (10.2f, BigDecimal(10.2f.toString)),
      (10.2, BigDecimal(10.2.toString)),
      (jm.BigInteger.valueOf(10L), BigDecimal(BigInt(10L))),
      (new jm.BigDecimal(10.2), BigDecimal(new jm.BigDecimal(10.2)))
    )

    forAll(testData) { (rsValue, expectedValue) =>
      val column = 3
      val rs = stub[ResultSet]
      (rs.getObject(_: Int)).when(column).returns(rsValue)

      assert(SqlRecordReader.readAs[BigDecimal](rs, column) == expectedValue)
    }
  }

  test("read non-number to BigInt / java.math.BigDecimal") {
    val column = 1

    val rs = stub[ResultSet]
    (rs.getObject(_: Int)).when(column).returns(new Object)

    intercept[IllegalStateException](SqlRecordReader.readAs[jm.BigDecimal](rs, column))
    intercept[IllegalStateException](SqlRecordReader.readAs[BigDecimal](rs, column))
  }

  //#endregion read BigDecimal / java.math.BigDecimal

  //#region read Option[AnyRef]

  test("read AnyRef option") {
    val testData = Table[
        // column
        Int,
        // wasNull
        Option[Boolean],
        // expectedValue
        Option[AnyRef],
        // getRsValueStubFn
        (ResultSet, Int) => Unit,
        // readFn
        (ResultSet, Int) => Option[AnyRef]](
      ("column", "wasNull", "expectedValue", "getRsValueStubFn", "readFn"),
      // java.lang.Boolean
      (
          1,
          Some(false),
          Some(jl.Boolean.valueOf(false)),
          (rs: ResultSet, c: Int) => (rs.getBoolean(_: Int)).when(c).returns(false),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Boolean]](rs, c)
      ),
      (
          2,
          Some(false),
          Some(jl.Boolean.valueOf(true)),
          (rs: ResultSet, c: Int) => (rs.getBoolean(_: Int)).when(c).returns(true),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Boolean]](rs, c)
      ),
      (
          3,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getBoolean(_: Int)).when(c).returns(false),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Boolean]](rs, c)
      ),
      // java.lang.Byte
      (
          1,
          Some(false),
          Some(jl.Byte.valueOf(0.toByte)),
          (rs: ResultSet, c: Int) => (rs.getByte(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Byte]](rs, c)
      ),
      (
          2,
          Some(false),
          Some(jl.Byte.valueOf(10.toByte)),
          (rs: ResultSet, c: Int) => (rs.getByte(_: Int)).when(c).returns(10),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Byte]](rs, c)
      ),
      (
          3,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getByte(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Byte]](rs, c)
      ),
      // java.lang.Short
      (
          1,
          Some(false),
          Some(jl.Short.valueOf(0.toShort)),
          (rs: ResultSet, c: Int) => (rs.getShort(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Short]](rs, c)
      ),
      (
          2,
          Some(false),
          Some(jl.Short.valueOf(10.toShort)),
          (rs: ResultSet, c: Int) => (rs.getShort(_: Int)).when(c).returns(10),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Short]](rs, c)
      ),
      (
          3,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getShort(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Short]](rs, c)
      ),
      // java.lang.Integer
      (
          1,
          Some(false),
          Some(jl.Integer.valueOf(0)),
          (rs: ResultSet, c: Int) => (rs.getInt(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Integer]](rs, c)
      ),
      (
          2,
          Some(false),
          Some(jl.Integer.valueOf(10)),
          (rs: ResultSet, c: Int) => (rs.getInt(_: Int)).when(c).returns(10),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Integer]](rs, c)
      ),
      (
          3,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getInt(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Integer]](rs, c)
      ),
      // java.lang.Long
      (
          1,
          Some(false),
          Some(jl.Long.valueOf(0.toLong)),
          (rs: ResultSet, c: Int) => (rs.getLong(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Long]](rs, c)
      ),
      (
          2,
          Some(false),
          Some(jl.Long.valueOf(10.toLong)),
          (rs: ResultSet, c: Int) => (rs.getLong(_: Int)).when(c).returns(10),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Long]](rs, c)
      ),
      (
          3,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getLong(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Long]](rs, c)
      ),
      // java.lang.Float
      (
          1,
          Some(false),
          Some(jl.Float.valueOf(0.toFloat)),
          (rs: ResultSet, c: Int) => (rs.getFloat(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Float]](rs, c)
      ),
      (
          2,
          Some(false),
          Some(jl.Float.valueOf(10.toFloat)),
          (rs: ResultSet, c: Int) => (rs.getFloat(_: Int)).when(c).returns(10),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Float]](rs, c)
      ),
      (
          3,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getFloat(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Float]](rs, c)
      ),
      // java.lang.Double
      (
          1,
          Some(false),
          Some(jl.Double.valueOf(0.toDouble)),
          (rs: ResultSet, c: Int) => (rs.getDouble(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Double]](rs, c)
      ),
      (
          2,
          Some(false),
          Some(jl.Double.valueOf(10.toDouble)),
          (rs: ResultSet, c: Int) => (rs.getDouble(_: Int)).when(c).returns(10),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Double]](rs, c)
      ),
      (
          3,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getDouble(_: Int)).when(c).returns(0),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jl.Double]](rs, c)
      ),
      // BigInt / java.math.BigInteger
      (
          1,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getObject(_: Int)).when(c).returns(null),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jm.BigInteger]](rs, c)
      ),
      (
          2,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getObject(_: Int)).when(c).returns(null),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[BigInt]](rs, c)
      ),
      (
          3,
          Some(false),
          Some(jm.BigInteger.valueOf(10)),
          (rs: ResultSet, c: Int) => (rs.getObject(_: Int)).when(c).returns(10.box),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jm.BigInteger]](rs, c)
      ),
      (
          4,
          Some(false),
          Some(BigInt(10)),
          (rs: ResultSet, c: Int) => (rs.getObject(_: Int)).when(c).returns(10.box),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[BigInt]](rs, c)
      ),
      // BigDecimal / java.math.BigDecimal
      (
          1,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getObject(_: Int)).when(c).returns(null),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jm.BigDecimal]](rs, c)
      ),
      (
          2,
          Some(true),
          None,
          (rs: ResultSet, c: Int) => (rs.getObject(_: Int)).when(c).returns(null),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[BigDecimal]](rs, c)
      ),
      (
          3,
          Some(false),
          Some(jm.BigDecimal.valueOf(10)),
          (rs: ResultSet, c: Int) => (rs.getObject(_: Int)).when(c).returns(10.box),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[jm.BigDecimal]](rs, c)
      ),
      (
          4,
          Some(false),
          Some(BigDecimal(10)),
          (rs: ResultSet, c: Int) => (rs.getObject(_: Int)).when(c).returns(10.box),
          (rs: ResultSet, c: Int) => SqlRecordReader.readAs[Option[BigDecimal]](rs, c)
      )
    )

    forAll(testData) { (column, wasNull, expectedValue, getRsValueStubFn, readFn) =>
      val rs = stub[ResultSet]
      getRsValueStubFn(rs, column)
      wasNull.foreach((rs.wasNull _).when().returns(_))

      assert(readFn(rs, column) == expectedValue)
    }
  }

  //#endregion read Option[AnyRef]
}
