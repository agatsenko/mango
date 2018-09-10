/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.common

package object resource {
  def using[TResource <: AutoCloseable, TResult](resource: TResource)(action: TResource => TResult): TResult = {
    Using.using(resource)(action)
  }
}
