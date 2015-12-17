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
class RankedGame(peek: Int = 4, levels: Int = 4) extends Strategy {

  private val ranks = Main.ranks

  private def getBest(contour: Contour, depth: Int = 0, upcoming: IndexedSeq[Tetromino] = generator.preview(peek)): (Option[Float], Option[Tetromino]) = {
    if (depth >= levels) {
      (Some(ranks(contour.toBase10)), None)
    } else {
      val toCheck = if (depth < upcoming.length) {
        Seq(upcoming(depth))
      } else {
        shuffle(pieces)
      }
      val pieceOptions = for (piece <- toCheck) yield {
        val options = for (x <- 0 to width; r <- 0 until 4) yield {
          val newPiece = piece.copy(x, r)
          if (newPiece.getSquares(0).forall(_._1 < width)) {
            contour + newPiece match {
              case Some(c) => Some(newPiece, getBest(c, depth + 1)._1)
              case _ => None
            }
          } else {
            None
          }
        }.flatMap { case (p, Some(ra)) => Some(p, ra) case _ => None }
        val positions = options.flatten
        if (positions.nonEmpty) {
          val best = positions.maxBy(_._2)
          val allBest = positions.filter(_._2 == best._2)
          println(best)
          println(allBest.length)
          (Some(best._2), Some(allBest(nextInt(allBest.length))._1))
        } else {
          (None, None)
        }
      }
      val possibleMoves = pieceOptions.flatMap { case (Some(x), Some(y)) => Some(x, y) case _ => None }
      println(possibleMoves)
      val validMoves = possibleMoves.filter(t => toID(t._2) == toID(generator.preview(1).head))
      println(validMoves)
      if (validMoves.nonEmpty) {
        val move = validMoves(nextInt(validMoves.length))
        (Some(move._1), Some(move._2))
      } else {
        (None, None)
      }
    }
  }

  def play(): Unit = {
    generator.preview(1).head match {
      case _: I if currentStack.stackHeight > 12 => {
        for (x <- 0 to width) {
          currentStack + new I(x) match {
            case Some(s) if s.stackHeight < currentStack.stackHeight => {
              currentStack = s
              return
            }
            case _ =>
          }
        }
      }
      case next =>
        getBest(currentStack.contour) match {
          case (Some(_), Some(hd)) => {
            val temp = currentStack + hd
            if (temp.isDefined) {
              currentStack = temp.get
            } else {
              throw new GameLostException("Stack reached top of board.")
            }
          }
          case _ => throw new GameLostException(s"Could not place ${getName(next)} on stack.")
        }
    }
    generator.next()
  }

}
