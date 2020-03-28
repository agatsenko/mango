/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.samples.sql.persistent

import java.sql.ResultSet

import io.mango.samples.sql.domain.Author
import io.mango.sql.sqlf
import io.mango.sql.Implicits.ResultSetExt._

private[persistent] object Authors {
  val TABLE = sqlf("authors")

  def rowMap(rs: ResultSet): Author = {
    import Columns._

    Author(
      rs.get[Long](ID.toString),
      rs.get[String](NAME.toString),
      rs.get[Option[String]](DESCRIPTION.toString),
    )
  }

  object Columns {
    val ID = sqlf("id")
    val NAME = sqlf("name")
    val DESCRIPTION = sqlf("description")
  }
}
