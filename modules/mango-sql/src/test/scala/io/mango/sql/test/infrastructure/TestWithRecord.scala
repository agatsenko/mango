/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2018-09-12
 */
package io.mango.sql.test.infrastructure

import scala.util.Using

import java.sql.Connection

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.h2.jdbcx.JdbcDataSource
import org.scalatest.{BeforeAndAfter, Suite}

trait TestWithRecord extends BeforeAndAfter {
  this: Suite =>

  def connUrl: String
  var connPool: HikariDataSource = _

  before {
    connPool = createConnPool
    createRecordTable()
  }

  after {
    if (connPool != null) {
      try {
        connPool.close()
      }
      finally {
        connPool = null
      }
    }
  }

  def createConnPool: HikariDataSource = {
    val nativeDataSource = new JdbcDataSource()
    nativeDataSource.setURL(connUrl)

    val conf = new HikariConfig()
    conf.setDataSource(nativeDataSource)
    conf.setAutoCommit(true)
    conf.setMinimumIdle(5)
    conf.setMaximumPoolSize(10)

    new HikariDataSource(conf)
  }

  def createRecordTable(): Unit = Using.resource(connPool.getConnection())(Record.createTable)

  def testWithinConn(action: Connection => Unit): Unit = {
    Using.resource(connPool.getConnection) { c =>
      action(c)
    }
  }
}
