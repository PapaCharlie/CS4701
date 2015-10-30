package tetris

import tetris.Stack.{Square, RichSquare, height, width}
import tetris.tetrominoes.Tetromino

/**
 * Created by papacharlie on 10/17/15.
 */
class Stack(val pieces: Array[Array[Boolean]] = Array.ofDim[Boolean](width + 1, height + 1)) {

  val lost: Boolean = (0 until width).map { x =>
    pieces(x)(height) || pieces(x)(height - 1)
  }.reduce((b1, b2) => b1 || b2)

  private def clearRows(arr: Array[Array[Boolean]], y: Int = 0): Array[Array[Boolean]] = {
    def moveBackAddFalse(a: Array[Boolean]): Array[Boolean] = {
      a.slice(0, y) ++ a.slice(y + 1, height + 1) :+ false
    }
    if (y == height) {
      arr
    } else {
      if ((0 to width).map(arr(_)(y)).forall(identity)) {
        val a = arr.map(moveBackAddFalse)
        clearRows(a, y)
      } else {
        clearRows(arr, y + 1)
      }
    }
  }

  def highestY(x: Int): Int = pieces(x).lastIndexOf(true)

  def +(p: Tetromino): Option[Stack] = {
    def fitPiece(y: Int): Option[Seq[Square]] = {
      if (y <= height) {
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

    fitPiece(highestY(p.x)) match {
      case Some(squares) => {
        val newPieces = squares.foldLeft(pieces) { case (pieces, (x, y)) =>
          pieces(x)(y) = true
          pieces
        }
        Some(new Stack(clearRows(newPieces)))
      }
      case _ => None
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
    ((height - 2) to 0 by -1).map { y =>
      (0 to width).map { x =>
        if (pieces(x)(y)) {
          0x25AE.toChar
        } else {
          " "
        }
      }.mkString("|", "", "|")
    }.mkString("-" * (width + 3) + "\n", "\n", "\n" + "-" * (width + 3))
  }
}

object Stack {

  type Square = (Int, Int)

  implicit class RichSquare(val s: Square) extends AnyVal {
    def fits = (s._1 >= 0 && s._1 <= width) && (s._2 >= 0 && s._2 <= height)
  }

  val height = 21

  val width = 9

}