/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.sql.support

import java.sql.Connection

import io.mango.sql.ConnectionProvider

trait PersistentContext extends ConnectionProvider with TransactionContext with AutoCloseable {
  def withConn[R](action: Connection => R): R
}
