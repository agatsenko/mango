/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.common.resource

trait CloseableResource extends AutoCloseable {
  def isClosed: Boolean

  final def isOpen: Boolean = !isClosed
}
