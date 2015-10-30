package tetris

import tetris.tetrominoes._

object Main extends App {

  def bunchaTs = {
    println("Try to stack all rotations of T at x = 3")
    new Stack() ++ T(3).allRotations match {
      case Some(stack) => println(stack)
      case _ =>
    }
  }

  def testClearRows = {
    println("Show that rows clear properly when required (not tested).")
    println("Also test that contour hashes to same contour as that dude's :p")
    new Stack() ++ Seq(new J(1, 0), new J(2, 3), new L(1, 3), new O(4), new L(7, 3),  new O(6)) match {
      case Some(stack) => {
        println(stack)
        println(new Contour(stack))
        println("Add I on the right")
        println((stack + new I(9)).get)
      }
      case _ =>
    }
  }

  bunchaTs
  testClearRows

}