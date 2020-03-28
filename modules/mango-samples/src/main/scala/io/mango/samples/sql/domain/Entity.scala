/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.samples.sql.domain

import java.util.Objects

trait Entity[TId] {
  def id: TId

  override def equals(obj: Any): Boolean = obj match {
    case objRef: AnyRef if this.eq(objRef) => true
    case ent: Entity[_] if ent != null && getClass == ent.getClass => id == ent.id
    case _ => false
  }

  override def hashCode(): Int = Objects.hashCode(id)
}
