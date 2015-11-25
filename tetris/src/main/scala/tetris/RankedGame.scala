package tetris

import Stack.width
import Utils.{clearScreen, waitToPrint}
import tetris.tetrominoes.{Tetromino, S, Z}

/**
 * Created by papacharlie on 11/18/15.
 */
class RankedGame {

  val generator = new Randomizer

  var currentStack = new Stack

  val ranks = ContourRank.loadRanks

  def play() = {
    val piece = generator.next
    val currentContour = currentStack.contour
    var maxRank = (piece, -1)
    (0 until width + 1).foreach { x =>
      (0 until 4).foreach { r =>
        val newPiece = piece.copy(x, r)
        (currentContour + newPiece).map(_.toBase10) match {
          case Some(c) if ranks(c) >= maxRank._2 => maxRank = (newPiece, ranks(c))
          case _ =>
        }
      }
    }
    currentStack + maxRank._1 match {
      case Some(s) => {
        waitToPrint()
        clearScreen()
        currentStack = s
        println(currentStack)
      }
      case None => throw new Exception("YOU DON GOOFED MOTHERFUCKER")
    }
  }

}
