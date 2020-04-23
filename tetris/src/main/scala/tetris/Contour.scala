package tetris

import tetris.tetrominoes.{Tetromino, Color}
import tetris.tetrominoes.Color.Black
import tetris.Stack._

import scala.math._

/**
 * contour: a base9 int (stored in Scala's base 10 Integer type)
 */
case class Contour(contour: Int) {

  import Contour._

  def toBase10: Int = {
    Integer.parseInt(contour.toString, 9)
  }

  lazy val heights: IndexedSeq[Int] = {
    ((width - 2) to 0 by -1).map { n =>
      contour / pow(10, n.toDouble).toInt % 10 - 4
    } |> (0 +: _) |> { diffs =>
      (1 until width).foldLeft(IndexedSeq.fill(width)(0)) { case (hs, n) =>
        hs.map(_ - diffs(n)).updated(n, hs(n - 1))
      }
    } |> { heights =>
      val minHeight = heights.min
      heights.map(_ - minHeight) :+ 0
    }
  }

  def +(piece: Tetromino): Option[Contour] = {
    def fitPiece(y: Int): Option[Seq[Square]] = {
      if (y <= height) {
        val squares = piece.getSquares(y)
        if (squares.forall(_.fits) && squares.forall { case (x, y) => y > heights(x) }) {
          Some(squares)
        } else {
          fitPiece(y + 1)
        }
      } else {
        None
      }
    }
    if (piece.fits && piece.getSquares(0).forall { case (x, _) => x < width }) {
      fitPiece(heights(piece.x) + 1) match {
        case Some(squares) =>
          squares.sorted.foldLeft(Some(heights): Option[IndexedSeq[Int]]) {
            case (Some(hs), (x, y)) if hs(x) + 1 == y => {
              Some(hs.updated(x, y))
            }
            case x => {
              None
            }
          }.map(fromHeights)
        case _ => None
      }
    } else {
      None
    }
  }

  // Not recommended
  def toStack: Stack = {
    def createSeq(n: Int): IndexedSeq[(Boolean, Color)] = {
      IndexedSeq.fill(n)((true, new Black)) ++ IndexedSeq.fill(height + 1 - n)((false, new Black))
    }
    new Stack(heights.map(createSeq))
  }
}

object Contour {

  def fromBase10(i: Int): Option[Contour] = {
    val contour = Integer.parseInt(Integer.toString(i, 9))
    val fits = ((width - 2) to 0 by -1).map(h => contour / pow(10, h.toDouble).toInt % 10 - 4).foldLeft((true, 0)) {
      case ((true, h), d) if abs(h + d) <= height + 1 => (true, h + d)
      case _ => (false, 0)
    }._1
    if (fits) {
      Some(new Contour(contour))
    } else {
      None
    }
  }

  def fromHeights(heights: Seq[Int]): Contour = {
    heights.tail.take(width - 1).foldLeft((0, heights.head)) { case ((contour, last), next) =>
      val diff = max(min(next - last, 4), -4) + 4
      (contour * 10 + diff, next)
    }._1 |> { c =>
      new Contour(c)
    }
  }
}