package tetris.strategies

import tetris.Stack._
import tetris.strategies.Strategy.GameLostException
import tetris.tetrominoes.{I, Tetromino}
import tetris.tetrominoes.Tetromino._
import tetris.{Contour, Main}

import scala.util.Random.{nextBoolean, shuffle}

/**
 * Created by papacharlie on 11/18/15.
 */
class RankedGame(peek: Int = 3, levels: Int = 3) extends Strategy {

  private val ranks = Main.ranks

  private def getBest(contour: Contour, depth: Int = 0, upcoming: IndexedSeq[Tetromino] = generator.preview(peek)): Option[(Int, Seq[Tetromino])] = {
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
                    case Some((rank, seq)) if rank > bestRank => {
                      bestSeq = newPiece +: seq
                      bestRank = rank
                    }
                    case Some((rank, seq)) if rank == bestRank => {
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
          case Some((_, hd :: _)) => {
            currentStack = (currentStack + hd).get
          }
          case _ => throw new GameLostException(s"Could not place $next on stack")
        }
    }
    generator.next()
  }

}
