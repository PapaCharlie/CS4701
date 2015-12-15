package tetris

import Stack.width
import Utils.{clearScreen, waitToPrint}
import tetris.tetrominoes.{Tetromino, S, Z}

/**
 * Created by papacharlie on 11/18/15.
 */
class RankedGame(peek: Int = 2) {

  val generator = new Randomizer

  var currentStack = new Stack

  val ranks = ContourRank.loadRanks

  def play() = {
    def getBest(stack: Stack, pieces: List[Tetromino]): Option[Tetromino] = {
      pieces match {
        case Seq() => None
        case piece :: List() => {
          var maxRank = (piece, -1)
          (0 until width + 1).foreach { x =>
            (0 until 4).foreach { r =>
              val newPiece = piece.copy(x, r)
              (stack + newPiece).map(_.contour.toBase10) match {
                case Some(c) if ranks(c) >= maxRank._2 => maxRank = (newPiece, ranks(c))
                case _ =>
              }
            }
          }
          Some(maxRank._1)
        }
        case piece::tl => {
          var maxRank = (piece, -1)
          (0 until width + 1).foreach { x =>
            (0 until 4).foreach { r =>
              val newPiece = piece.copy(x, r)
              (stack + newPiece).map(getBest(_, tl).map(stack + _)) match {
                case x =>
              }
            }
          }
          Some(maxRank._1)
        }
      }
    }

    val pieces = generator.preview(peek)
    println(pieces)
    val currentContour = currentStack.contour
    val piece= pieces.head
    var maxRank = (pieces.head, -1)
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
      case None => throw new Exception("YOU DON GOOFED")
    }
  }

}
