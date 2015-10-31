package tetris

import tetris.tetrominoes._
import Utils._

object Main extends App {

  def bunchaTs = {
    println("Try to stack all rotations of T at x = 3")
    new Stack() ++ T(3).allRotations match {
      case Some(stack) => println(stack)
      case _ =>
    }
  }

  //  bunchaTs

  def testClearRows = {
    println("Show that rows clear properly when required (not tested).")
    println("Also test that contour hashes to same contour")
    new Stack() ++ Seq(new J(1, 1), new J(2, 3), new L(1, 3), new O(4), new L(7, 3), new O(6)) match {
      case Some(stack) => {
        println(stack)
        println(new Contour(stack))
        println("Add I on the right")
        println((stack + new I(9)).get)
      }
      case _ =>
    }
  }

  //  testClearRows

  def showColors = {
    applyPieces(new Stack, Seq(new I(0), new J(2), new L(3),
      new O(5),
      new T(8, 3),
      new Z(8),
      new S(6, 1)
    )) |> printStacks
    if (args.nonEmpty) println(args(0))
    scala.io.StdIn.readLine()
  }

  showColors

}