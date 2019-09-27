/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-27
 */
package io.mango.common.io

import scala.collection.mutable.ArrayBuffer

import java.io.{BufferedInputStream, BufferedOutputStream, ByteArrayInputStream, ByteArrayOutputStream}

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class StreamsTests extends FunSuite with TableDrivenPropertyChecks with Matchers {
  test("toBufferedInputStream(in, buffSize) should create a BufferedInputStream") {
    val data = Table[Array[Byte], Option[Int]](
      ("srcBytes", "buffSize"),
      (Array[Byte](1, 2, 3), None),
      (Array[Byte](1, 2, 3), Some(2)),
    )

    forAll(data) { (srcBytes, buffSize) =>
      val in = new ByteArrayInputStream(srcBytes)
      val buffredIn = Streams.toBufferedInputStream(in, buffSize)

      assert(buffredIn != null)
      assert(buffredIn.isInstanceOf[BufferedInputStream])

      val bytes = new ArrayBuffer[Byte](srcBytes.size)
      var b = -1
      while ( {
        b = buffredIn.read(); b > -1
      }) {
        bytes += b.toByte
      }
      assert(bytes.toArray sameElements srcBytes)
    }
  }

  test("toBufferedOutputStream(out, buffSize) should create a BufferedOutputStream") {
    val data = Table[Array[Byte], Option[Int]](
      ("srcBytes", "buffSize"),
      (Array[Byte](1, 2, 3), None),
      (Array[Byte](1, 2, 3), Some(2)),
    )

    forAll(data) { (srcBytes, buffSize) =>
      val out = new ByteArrayOutputStream(srcBytes.size)
      val bufferedOut = Streams.toBufferedOutputStream(out, buffSize)

      assert(bufferedOut != null)
      assert(bufferedOut.isInstanceOf[BufferedOutputStream])

      for (b <- srcBytes) {
        bufferedOut.write(b)
      }
      bufferedOut.flush()

      assert(out.toByteArray sameElements srcBytes)
    }
  }

  test("copy(in, out) should copy bytes from 'in' stream to 'out' stream") {
    val bytes = Array[Byte](1, 2, 3)
    val in = new ByteArrayInputStream(bytes)
    val out = new ByteArrayOutputStream(bytes.size)
    Streams.copy(in, out)
    assert(out.toByteArray sameElements bytes)
  }
}
