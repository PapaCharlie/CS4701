package tetris

import tetris.tetrominoes._

object Main extends App {

  val s = new Stack() ++ Seq(new T(1), new O(3))

  s match {
    case Some(stack) => println(stack)
    case _ => println("T(1) then O(3) don't fit")
  }
  println(s.get.hasNoHoles)

  new Stack() ++ T(3).allRotations match {
    case Some(stack) => println(stack)
    case _ =>
  }

}
