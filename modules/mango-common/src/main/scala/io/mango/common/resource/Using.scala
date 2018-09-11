/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.common.resource

import scala.util.Try

import io.mango.common.util.TryExt

object Using {
  def using[TResource <: AutoCloseable, TResult](resource: TResource)(action: TResource => TResult): TResult = {
    Try(action(resource)).eventually { _ =>
      if (resource != null) {
        resource.close()
      }
    }.get
  }
}