/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-12
  */
package io.mango.sql.test.infrastructure

import java.sql.Connection

import io.mango.common.resource.using

case class Record(id: Long, str: Option[String], int: Option[Int])

object Record {
  val TABLE = "test_table"
  val ID_CLM = "id"
  val STR_CLM = "str_val"
  val INT_CLM = "int_clm"

  def createRecords(seq: RecordSeq = RecordSeq()): Seq[Record] = {
    Seq(
      Record(seq.next(), Some("one"), Some(1)),
      Record(seq.next(), Some("two"), None),
      Record(seq.next(), None, Some(3)),
      Record(seq.next(), None, None)
    )
  }

  def createTable(conn: Connection): Unit = {
    val sql =
      s"""
        create table if not exists $TABLE (
         $ID_CLM bigint not null,
         $STR_CLM varchar(50),
         $INT_CLM int,

         constraint pk_$TABLE primary key ($ID_CLM)
        );
        delete from $TABLE;
      """
    using (conn.prepareStatement(sql)) { ps =>
      ps.execute()
    }
  }

  def insertRecord(conn: Connection, r: Record): Unit = {
    val sql =
      s"""
        insert into $TABLE ($ID_CLM, $STR_CLM, $INT_CLM) values (
          ${r.id}, ${r.str.map("'" + _ + "'").orNull}, ${r.int.orNull}
        )
      """
    using(conn.prepareStatement(sql)) { ps =>
      ps.execute()
    }
  }

  def selAllRecords(conn: Connection): Seq[Record] = {
    val sql = s"select $ID_CLM, $STR_CLM, $INT_CLM from $TABLE"
    using(conn.prepareStatement(sql)) { ps =>
      val seqBuilder = Seq.newBuilder[Record]
      using (ps.executeQuery()) { rs =>
        while(rs.next()) {
          seqBuilder += Record(
            rs.getLong(ID_CLM),
            Option(rs.getString(STR_CLM)),
            Option(rs.getObject(INT_CLM, classOf[Integer])).map(_.intValue())
          )
        }
      }
      seqBuilder.result()
    }
  }

  final class RecordSeq {
    private var seq = 0L

    def next(): Long = {
      seq += 1
      seq
    }
  }

  object RecordSeq {
    def apply(): RecordSeq = new RecordSeq
  }
}
