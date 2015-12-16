package tetris.strategies

import tetris.Stack._
import tetris.strategies.Strategy.GameLostException
import tetris.tetrominoes.Tetromino
import tetris.tetrominoes.Tetromino._
import tetris.{Contour, Main, Randomizer, Stack}

import scala.util.Random.{nextBoolean, shuffle}

/**
 * Created by papacharlie on 11/18/15.
 */
class RankedGame(peek: Int = 4, levels: Int = 5) extends Strategy {

  val ranks = Main.ranks

  def getBest(contour: Contour, depth: Int = 0, upcoming: IndexedSeq[Tetromino] = generator.preview(peek)): Option[(Int, Seq[Tetromino])] = {
    if (depth >= levels) {
      Some((ranks(contour.toBase10), Seq()))
    } else {
      val toCheck = if (depth < upcoming.length) {
        Seq(upcoming(depth))
      } else {
        shuffle(pieces)
      }
      var bestSeq: Seq[Tetromino] = Seq()
      var bestRank = 0
      toCheck.foreach { piece =>
        (0 until width + 1).foreach { x =>
          if (piece.getSquares(0).forall { case (x, _) => x < width }) {
            (0 until 4).foreach { r =>
              val newPiece = piece.copy(x, r)
              contour + newPiece match {
                case Some(c) => {
                  getBest(c, depth + 1) match {
                    case Some((rank, seq)) if rank >= bestRank => {
                      if (nextBoolean()) {
                        bestSeq = newPiece +: seq
                        bestRank = rank
                      }
                    }
                    case _ =>
                  }
                }
                case _ => None
              }
            }
          }
        }
      }
      Some((bestRank, bestSeq))
    }
  }

  def play() = {
    val next = generator.preview(1).head
    getBest(currentStack.contour) match {
      case Some((_, hd :: _)) => {
        currentStack = (currentStack + hd).get
        generator.next()
      }
      case _ => throw new GameLostException(s"Could not place $next on stack")
    }


  }

}
