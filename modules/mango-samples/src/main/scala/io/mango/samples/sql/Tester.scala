/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-28
 */
package io.mango.samples.sql

import scala.io.Source
import scala.util.Using

import javax.sql.DataSource

import com.typesafe.scalalogging.StrictLogging
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.mango.samples.sql.domain.{Author, AuthorRepo}
import io.mango.samples.sql.domain.Author.Id
import io.mango.samples.sql.persistent.AuthorJdbcRepo
import io.mango.sql.Implicits.All._
import io.mango.sql.support.{PersistentContext, ThreadLocalPersistentContext}
import org.h2.Driver

object Tester extends StrictLogging {
  private val DOCS_SQL_RESOURCE = "/docs.sql"

  private val DOCS_DB_URL = "jdbc:h2:mem:docs"

  def main(args: Array[String]): Unit = {
    val ds = new HikariDataSource(hikariConfig(DOCS_DB_URL))
    Using.resource(new ThreadLocalPersistentContext(ds, Some(_ => ds.close()))) { context =>
      createDb(ds)

      val authorRepo = new AuthorJdbcRepo(context)

      context.withinTx { _ =>
        createAuthors(authorRepo, context)
      }

      context.withConn { _ =>
        println(authorRepo.findAll())
        println(authorRepo.findById(1L))
        println(authorRepo.findByIds(Seq[Id](0L, 2L, 3L, 1000L)))
      }
    }
  }

  private def createAuthors(authorRepo: AuthorRepo, context: PersistentContext): Unit = {
    context.withinTx { _ =>
      authorRepo.add(
        Author(authorRepo.newId(), "Martin Odersky", Some("Martin Odersky is the creator of the Scala language."))
      )
      authorRepo.add(
        Author(
          authorRepo.newId(),
          "Lex Spoon",
          Some("Lex Spoon worked on Scala for two years at EPFL and is now a software engineer at Square, Inc."),
        )
      )
      authorRepo.add(Author(authorRepo.newId(), "Bill Venners", Some("Bill Venners is president of Artima, Inc.")))
      authorRepo.add(Author(authorRepo.newId(), "Christian Baxter", None))
    }
  }

  private def hikariConfig(jdbcUrl: String): HikariConfig = {
    val config = new HikariConfig()
    config.setDriverClassName(classOf[Driver].getName)
    config.setJdbcUrl(jdbcUrl)
    config
  }

  private def loadDocsDbSql: String = {
    Using.resource(Source.fromFile(getClass.getResource(DOCS_SQL_RESOURCE).getFile)) { source =>
      source.mkString
    }
  }

  private def createDb(ds: DataSource): Unit = {
    Using.Manager { use =>
      val conn = use(ds.getConnection)
      conn.withinTx { _ =>
        val statement = use(conn.createStatement())
        statement.execute(loadDocsDbSql)
      }
    }.get
  }
}
