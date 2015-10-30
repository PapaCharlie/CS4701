package tetris

import tetris.Stack.{Square, RichSquare, height, width, emptyStack}
import tetris.tetrominoes.Color.Black
import tetris.tetrominoes.{Color, Tetromino}

/**
 * Created by papacharlie on 10/17/15.
 */
class Stack(val pieces: Array[Array[(Boolean, Color)]] = emptyStack) {

  implicit def squareToBoolean(k: (Boolean, Color)): Boolean = k._1

  private def clearRows(arr: Array[Array[(Boolean, Color)]], y: Int = 0): Array[Array[(Boolean, Color)]] = {
    def moveBackAddFalse(a: Array[(Boolean, Color)]): Array[(Boolean, Color)] = {
      a.slice(0, y) ++ a.slice(y + 1, height + 3) :+ (false, new Black)
    }
    if (y == height) {
      arr
    } else {
      if ((0 to width).map(arr(_)(y)).forall(_._1)) {
        val a = arr.map(moveBackAddFalse)
        clearRows(a, y)
      } else {
        clearRows(arr, y + 1)
      }
    }
  }

  def highestY(x: Int): Int = pieces(x).lastIndexOf(true)

  val lost: Boolean = (0 until width).map { x =>
    pieces(x)(height) || pieces(x)(height - 1)
  }.reduce((b1, b2) => b1 || b2)

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

    fitPiece(highestY(p.x)) match {
      case Some(squares) => {
        val newPieces = squares.foldLeft(pieces) { case (stack, (x, y)) =>
          stack(x)(y) = (true, p.color)
          stack
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
        case (Some(stack), piece) => {
          stack + piece
        }
        case _ => None
      }
    }
  }

  def hasNoHoles: Boolean = {
    def twoTone(arr: Array[(Boolean, Color)]): Boolean = {
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
    def fits = (s._1 >= 0 && s._1 <= width) && (s._2 >= 0 && s._2 <= height + 2)
  }

  val height = 19

  val width = 9

  private val column: Array[(Boolean, Color)] = (0 to height).map { x => (false, new Black) }.toArray

  val emptyStack: Array[Array[(Boolean, Color)]] = (0 to width).map(x => column).toArray

}