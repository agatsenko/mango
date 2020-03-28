/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.sql

import scala.language.implicitConversions

import java.sql.Connection

trait ConnectionProvider {
  def getConnection: Connection
}

object ConnectionProvider {
  implicit def toConnection(provider: ConnectionProvider): Connection = provider.getConnection
}
