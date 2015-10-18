package tetris

import tetris.tetrominoes._

object Main extends App {

  var s = new Stack()

  s ++ Seq(new T(1), new O(3)) match {
    case Some(stack) => println(stack)
    case None => println("T(1) then O(3) don't fit")
  }

}
