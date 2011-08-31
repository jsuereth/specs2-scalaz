package org.specs2.internal.scalaz

trait FirstOption[A] extends NewType[Option[A]]

trait FirstOptions {
  implicit def FirstOptionTo[A](a: Option[A]): FirstOption[A] = new FirstOption[A] {
    val value = a
  }
}
