/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2020-03-29
 */
package io.mango.samples.sql.domain

trait AuthorRepo {
  def newId(): Author.Id

  def findById(id: Author.Id): Option[Author]

  def findByIds(ids: Iterable[Author.Id]): Iterable[Author]

  def findAll(): Iterable[Author]

  def add(author: Author): Unit

  def update(author: Author): Unit

  def remove(id: Author.Id): Unit
}
