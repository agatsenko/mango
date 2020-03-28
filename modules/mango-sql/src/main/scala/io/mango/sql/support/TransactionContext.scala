/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.sql.support

import java.sql.Connection

trait TransactionContext {
  def withinTx[R](isolationLevel: Int)(action: Connection => R): R

  def withinTx[R](action: Connection => R): R
}
