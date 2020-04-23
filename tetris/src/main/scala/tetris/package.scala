package object tetris {
  implicit class Pipe[T](val t: T) extends AnyVal {
    def |->(fun: T => Unit): T = {
      fun(t)
      t
    }

    def |>[U](fun: T => U): U = fun(t)
  }
}
