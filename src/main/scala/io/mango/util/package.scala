/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango

import scala.reflect.ClassTag
import scala.util.{Failure, Left, Right, Success, Try}

package object util {
  implicit class TryExt[+T](val tr: Try[T]) extends AnyVal {
    def eventually[TIgnore](action: Try[T] => TIgnore): Try[T] = Try(action(tr)) match {
      case Failure(ex) => tr match {
        case Success(_) => Failure(ex)
        case Failure(srcEx) =>
          ex.addSuppressed(srcEx)
          Failure(ex)
      }
      case _ => tr
    }

    def convertFailure[E <: Throwable : ClassTag](convert: Throwable => E): Try[T] = {
      tr match {
        case Success(_) => tr
        case Failure(srcEx) => srcEx match {
          case _: E => tr
          case _ => Try[T] {
            throw convert(srcEx)
          }
        }
      }
    }
  }

  implicit class EitherExt[+A, +B](val either: Either[A, B]) extends AnyVal {
    def asTry(implicit ev: A <:< Throwable): Try[B] = either match {
      case Right(b) => Success(b)
      case Left(a) => Failure(a)
    }
  }
}
