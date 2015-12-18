package tetris.strategies

import tetris.Stack._
import tetris.strategies.Strategy.GameLostException
import tetris.tetrominoes.{I, Tetromino}
import tetris.tetrominoes.Tetromino._
import tetris.{Contour, Main}
import tetris.Utils._

import scala.util.Random.{nextBoolean, nextInt, shuffle}

/**
 * Created by papacharlie on 11/18/15.
 */
class RankedGame(depth: Int = 4, maxHeight: Int = 9) extends Strategy {

  private val ranks = Main.ranks

  private def getBest(contour: Contour, upcoming: Seq[Tetromino]): Option[Tetromino] = {
    def getBestRank(contour: Contour, upcoming: Seq[Tetromino]): Option[Float] = {
      upcoming match {
        case Seq(hd) => {
          val options = for (x <- 0 to width; r <- 0 until 4) yield {
            val newPiece = hd.copy(x, r)
            contour + newPiece match {
              //              case Some(c) if ranks(c.toBase10) > 0 => Some(ranks(c.toBase10))
              case Some(c) => Some(ranks(c.toBase10))
              case _ => None
            }
          }
          val validOptions = options.flatten
          if (validOptions.nonEmpty) {
            Some(validOptions.max)
          } else {
            None
          }
        }
        case Seq(hd, tl@_*) => {
          val options = for (x <- 0 to width; r <- 0 until 4) yield {
            val newPiece = hd.copy(x, r)
            contour + newPiece match {
              //              case Some(c) if ranks(c.toBase10) > 0 => getBestRank(c, tl)
              case Some(c) => getBestRank(c, tl)
              case _ => None
            }
          }
          val validOptions = options.flatten
          if (validOptions.nonEmpty) {
            Some(validOptions.max)
          } else {
            None
          }
        }
        case Seq() => Some(ranks(contour.toBase10))
      }
    }
    val piece = upcoming.head
    val options = for (x <- 0 to width; r <- 0 until 4) yield {
      val newPiece = piece.copy(x, r)
      contour + newPiece match {
        case Some(c) => getBestRank(c, upcoming.tail).map((newPiece, _))
        case _ => None
      }
    }
    val validOptions = options.flatten
    if (validOptions.nonEmpty) {
      Some(validOptions.maxBy(_._2)._1)
    } else {
      None
    }
  }


  def play(): Unit = {
    generator.preview(1).head match {
      case _: I if currentStack.stackHeight > maxHeight => {
        var placed = false
        for (x <- 0 to width) {
          currentStack + new I(x) match {
            case Some(s) if s.stackHeight < currentStack.stackHeight => {
              currentStack = s
              placed = true
            }
            case _ =>
          }
        }
        if (!placed) {
          currentStack + new I(currentStack.lowestColumn) match {
            case Some(s) => currentStack = s
            case _ =>
          }
        }
      }
      case next => {
        getBest(currentStack.contour, generator.preview(depth)) match {
          case Some(t) => {
            val temp = currentStack + t
            if (temp.isDefined) {
              currentStack = temp.get
            } else {
              throw new GameLostException("Stack reached top of board.")
            }
          }
          case _ => throw new GameLostException(s"Could not place ${getName(next)} on stack, or loss predicted $depth moves away.")
        }
      }
    }
    generator.next()
  }

}
