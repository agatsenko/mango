/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.samples.sql.domain

import io.mango.common.util.Check

case class Author(id: Author.Id, name: String, description: Option[String]) extends Entity[Author.Id] {
  Check.argNotNull(id, "id")
  Check.argNotEmpty(name, "name")
  Check.argNotNull(description, "description")
}

object Author {
  type Id = java.lang.Long
}
