package tetris

import tetris.tetrominoes._

object Main extends App {

  val s = new Stack() ++ Seq(new T(1), new O(3))

  s match {
    case Some(stack) => println(stack)
    case _ => println("T(1) then O(3) don't fit")
  }
  println(s.get.hasNoHoles)
  println(new Contour(s.head))

  new Stack() ++ T(3).allRotations match {
    case Some(stack) => println(stack)
    case _ =>
  }

  new Stack() ++ Seq(new J(1, 1), new J(2, 3), new L(1, 3), new O(4), new L(7, 3)) match {
    case Some(stack) => {
      println(stack)
      println(new Contour(stack))
    }
    case _ =>
  }

}
