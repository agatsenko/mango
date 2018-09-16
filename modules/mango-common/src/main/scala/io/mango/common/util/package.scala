/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-10
  */
package io.mango.common

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

    def toEither: Either[Throwable, T] = tr match {
      case Success(v) => Right(v)
      case Failure(ex) => Left(ex)
    }
  }

  implicit class EitherExt[+L, +R](val either: Either[L, R]) extends AnyVal {
    def toTry(implicit ev: L <:< Throwable): Try[R] = either match {
      case Right(b) => Success(b)
      case Left(a) => Failure(a)
    }
  }

  implicit class OptionEx[+T](val opt: Option[T]) extends AnyVal {
    def getOrThrow[E <: Throwable](f: => E): T = {
      if (opt.isEmpty) {
        throw f
      }
      opt.get
    }
  }
}
