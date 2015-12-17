package tetris.strategies

import tetris.Stack._
import tetris.strategies.Strategy.GameLostException
import tetris.tetrominoes.{I, Tetromino}
import tetris.tetrominoes.Tetromino._
import tetris.{Contour, Main}



import scala.util.Random
/**
 * Created by papacharlie on 12/15/15.
 */
class MinimaxGame(peek: Int = 2, depth: Int = 5) extends Strategy {

//  def play() = {}

//  private def
  private val rand = new Random()
  def play() = {
    //    val pieces = generator.preview(peek)
    //    println(pieces)
    ////    val currentContour = currentStack.contour
    //    val piece= pieces.head
    ////    var maxRank = (pieces.head, -1)
    ////    (0 until width + 1).foreach { x =>
    ////      (0 until 4).foreach { r =>
    ////        val newPiece = piece.copy(x, r)
    ////        (currentContour + newPiece).map(_.toBase10) match {
    ////          case Some(c) if ranks(c) >= maxRank._2 => maxRank = (newPiece, ranks(c))
    ////          case _ =>
    ////        }
    ////      }
    ////    }
    //    generator.preview(1).head match {
    //      case _: piece if currentStack.stackHeight >=0 {
    ////        val piece = pieces.head
    //        currentStack = currentStack + piece
    //        return
    //      }
    //        return
    //    }
    //    generator.next()
    //
    //
    //  }
    generator.preview(1).head match {
      case _ : I =>
        if (currentStack.stackHeight > 15) {
          currentStack + new I(10) match {
            case Some(s) => currentStack = s
            case _ => throw new GameLostException(s"Could not place next on stack")
          }
        }
      case next : Tetromino =>

      ////        val newPiece = piece.copy(x, r)
        currentStack + next.copy(rand.nextInt(4),rand.nextInt(width)) match {
        case Some(s) => currentStack = s
        case _ => //throw new GameLostException(s"Could not place next on stack")
      }
    }
    if (currentStack.stackHeight > 15)  {
      throw new GameLostException(s"YOU LOSE")
    }

    generator.next()

  }
}
