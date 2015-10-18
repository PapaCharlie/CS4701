package tetris

import tetris.Stack.{Square, RichSquare, height, width}
import tetris.tetrominoes.Tetromino

/**
 * Created by papacharlie on 10/17/15.
 */
class Stack(val pieces: Array[Array[Boolean]] = Array.ofDim[Boolean](width + 1, height + 3)) {

  def highestY(x: Int): Int = pieces(x).lastIndexOf(true)

  def +(p: Tetromino): Option[Stack] = {

    def fitPiece(y: Int): Option[Seq[Square]] = {
      if (y <= height + 2) {
        val squares = p.getSquares(y)
        if (squares.forall(_.fits) && squares.forall { case (x, y) => !pieces(x)(y) }) {
          Some(squares)
        } else {
          fitPiece(y + 1)
        }
      } else {
        None
      }
    }

    fitPiece(highestY(p.x)).map { squares =>
      val newPieces = squares.foldLeft(pieces) { case (pieces, (x, y)) =>
        pieces(x)(y) = true
        pieces
      }
      new Stack(newPieces)
    }

  }

  def ++(iterable: Iterable[Tetromino]): Option[Stack] = {
    if (iterable.isEmpty) {
      Some(this)
    } else {
      iterable.foldLeft(Some(this): Option[Stack]) {
        case (Some(stack), piece) => stack + piece
        case _ => None
      }
    }
  }

  def hasNoHoles: Boolean = {

    def twoTone(arr: Array[Boolean]): Boolean = {
      val rest = arr.dropWhile(_ == arr(0))
      if (rest.isEmpty || rest.toSet.size == 1) {
        true
      } else {
        false
      }
    }
    pieces.map(twoTone).forall(identity)

  }

  override def toString = {
    (height to 0 by -1).map { y =>
      (0 to width).map { x =>
        if (pieces(x)(y)) {
          "1"
        } else {
          "0"
        }
      }.mkString("")
    }.mkString("\n")
  }
}

object Stack {

  type Square = (Int, Int)

  implicit class RichSquare(val s: Square) extends AnyVal {
    def fits = (s._1 >= 0 && s._1 <= width) && (s._2 >= 0 && s._2 <= height + 2)
  }

  val height = 19

  val width = 9

}