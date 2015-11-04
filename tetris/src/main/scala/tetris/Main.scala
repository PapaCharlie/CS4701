package tetris

import tetris.tetrominoes._
import Utils._

object Main extends App {

  def bunchaTs() = {
    println("Try to stack all rotations of T at x = 3")
    new Stack() ++ T(3).allRotations match {
      case Some(stack) => println(stack)
      case _ =>
    }
  }

  def testClearRows() = {
    println("Show that rows clear properly when required (not tested).")
    println("Also test that contour hashes to same contour")
    new Stack() ++ Seq(new J(1, 1), new J(2, 3), new L(1, 3), new O(4), new L(7, 3), new O(6)) match {
      case Some(stack) => {
        println(stack)
        println(stack.contour)
        println("Add I on the right")
        println((stack + new I(9)).get)
      }
      case _ =>
    }
    println(Stack.fromContour(45244543))
    println(Stack.fromContour(16426544))
    println(Stack.fromContour(44200001))
  }

  def showColors() = {
    val pieces = Seq(new I(0), new J(2), new L(3), new O(5), new T(8, 3), new Z(8), new S(6, 1))
    applyPieces(new Stack, pieces) |> printStacks
    println((new Stack ++ pieces).get.contour)
  }

  args.headOption.getOrElse("colors") match {
    case "contour" => testClearRows()
    case "computeContour" => println(new ContourRank(4) {
      compute
      ranks(0)
    })
    case "rank" =>
    case "learning" =>
    case "colors" => showColors()
    case _ => println("Unknown game mode")
  }
  scala.io.StdIn.readLine()

}