package tetris

import tetris.tetrominoes._

object Main extends App {

  var s: Option[Stack] = Some(new Stack())

  s = s.get ++ Seq(new T(1), new O(3))

  s match {
    case Some(stack) => println(stack)
    case _ => println("T(1) then O(3) don't fit")
  }
  println(s.get.hasNoHoles)

}
