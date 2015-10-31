package tetris

import tetris.Stack._
import tetris.tetrominoes.Color.Black
import tetris.tetrominoes.{Color, Tetromino}

/**
 * Created by papacharlie on 10/17/15.
 */
class Stack(val pieces: IndexedSeq[IndexedSeq[(Boolean, Color)]] = emptyStack) {

  override def toString = {
    ((height - 2) to 0 by -1).map { y =>
      (0 to width).map { x =>
        if (pieces(x)(y)._1) {
          pieces(x)(y)._2.console + 0x25AE.toChar + Console.RESET
        } else {
          " "
        }
      }.mkString("|", "", "|")
    }.mkString("-" * (width + 3) + "\n", "\n", "\n" + "-" * (width + 3))
  }

  private def clearRows(stack: IndexedSeq[IndexedSeq[(Boolean, Color)]], y: Int = 0): IndexedSeq[IndexedSeq[(Boolean, Color)]] = {
    def moveBackAddFalse(seq: IndexedSeq[(Boolean, Color)]): IndexedSeq[(Boolean, Color)] = {
      seq.slice(0, y) ++ seq.slice(y + 1, height + 1) :+(false, new Black)
    }
    if (y == height) {
      stack
    } else {
      if ((0 to width).map(stack(_)(y)).forall(_._1)) {
        val a = stack.map(moveBackAddFalse)
        clearRows(a, y)
      } else {
        clearRows(stack, y + 1)
      }
    }
  }

  private def highestY(x: Int): Int = pieces(x).lastIndexOf(true)

  def +(p: Tetromino): Option[Stack] = {
    def fitPiece(y: Int): Option[Seq[Square]] = {
      if (y <= height) {
        val squares = p.getSquares(y)
        if (squares.forall(_.fits) && squares.forall { case (x, y) => !pieces(x)(y)._1 }) {
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
        val newPieces = clearRows(squares.foldLeft(pieces) { case (stack, (x, y)) =>
          stack.updated(x, stack(x).updated(y, (true, p.color)))
        })
        Some(new Stack(newPieces))
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
    def twoTone(arr: IndexedSeq[(Boolean, Color)]): Boolean = {
      val rest = arr.dropWhile(_._1 == arr.head._1)
      if (rest.isEmpty || rest.map(_._1).toSet.size == 1) {
        true
      } else {
        false
      }
    }
    pieces.map(twoTone).forall(identity)
  }

  def lost: Boolean = (0 to width).map { x =>
    pieces(x)(height)._1 || pieces(x)(height - 1)._1
  }.reduce((b1, b2) => b1 || b2)

  def stackHeight: Int = (0 to width).map(highestY(_)).max

}

object Stack {

  type Square = (Int, Int)

  implicit class RichSquare(val s: Square) extends AnyVal {
    def fits = (s._1 >= 0 && s._1 <= width) && (s._2 >= 0 && s._2 <= height)
  }

  val height = 21

  val width = 9

  def emptyStack: IndexedSeq[IndexedSeq[(Boolean, Color)]] = {
    val column: IndexedSeq[(Boolean, Color)] = (0 to height).map { x => (false, new Black) }
    (0 to width).map(x => column)
  }

}