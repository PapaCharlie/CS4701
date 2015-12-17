package tetris.strategies

import tetris.Stack

/**
 * Created by papacharlie on 12/15/15.
 */
class MinimaxGame(peek: Int = 2, depth: Int = 5) extends Strategy {

  def play() = {}

//  private def

//  def play() = {
//    val pieces = generator.preview(peek)
//    println(pieces)
//    val currentContour = currentStack.contour
//    val piece= pieces.head
//    var maxRank = (pieces.head, -1)
//    (0 until width + 1).foreach { x =>
//      (0 until 4).foreach { r =>
//        val newPiece = piece.copy(x, r)
//        (currentContour + newPiece).map(_.toBase10) match {
//          case Some(c) if ranks(c) >= maxRank._2 => maxRank = (newPiece, ranks(c))
//          case _ =>
//        }
//      }
//    }
//    currentStack + maxRank._1 match {
//      case Some(s) => {
//        waitToPrint()
//        clearScreen()
//        currentStack = s
//        println(currentStack)
//      }
//      case None => throw new Exception("YOU DON GOOFED")
//    }
//  }

}
