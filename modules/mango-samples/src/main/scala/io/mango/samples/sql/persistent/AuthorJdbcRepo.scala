/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.samples.sql.persistent

import com.typesafe.scalalogging.StrictLogging
import io.mango.common.util.Check
import io.mango.samples.sql.domain.{Author, AuthorRepo}
import io.mango.sql.Implicits.All._
import io.mango.sql.support.PersistentContext

class AuthorJdbcRepo(private val context: PersistentContext) extends AuthorRepo with StrictLogging {
  import Authors._
  import Authors.Columns._

  override def newId(): Author.Id = context.withConn { implicit conn =>
    val qry = sql"select nextval('authors_seq')"
    logger.info(s"newId: $qry")
    qry.execScalar[Long].get
  }

  override def findById(id: Author.Id): Option[Author] = context.withConn { implicit conn =>
    Check.argNotNull(id, "id")
    val qry = sql"select * from $TABLE where $ID = $id"
    logger.info(s"findById: $qry")
    qry.execOneRow(rowMap)
  }

  override def findByIds(ids: Iterable[Author.Id]): Iterable[Author] = context.withConn { implicit conn =>
    val qry = sql"select * from $TABLE where $ID in ($ids)"
    logger.info(s"findByIds: $qry")
    qry.execRows(rowMap)
  }

  override def findAll(): Iterable[Author] = context.withConn { implicit conn =>
    val qry = sql"select * from $TABLE"
    logger.info(s"findAll: $qry")
    qry.execRows[Author, Iterable](rowMap)
  }

  override def add(author: Author): Unit = context.withConn { implicit conn =>
    Check.argNotNull(author, "author")
    val qry = sql"insert into $TABLE values (${author.id}, ${author.name}, ${author.description})"
    logger.info(s"add: $qry")
    qry.execUpdate()
  }

  override def update(author: Author): Unit = context.withConn { implicit conn =>
    val qry =
      sql"""
           | update $TABLE
           |   set $NAME = ${author.name},
           |       $DESCRIPTION = ${author.description}
           |   where $ID = ${author.id}
       """
    logger.info(s"update: $qry")
    qry.execUpdate()
  }

  override def remove(id: Author.Id): Unit = context.withConn { implicit conn =>
    val qry = sql"delete from $TABLE where $ID = $id"
    logger.info(s"remove: $qry")
    qry.execUpdate()
  }
}
