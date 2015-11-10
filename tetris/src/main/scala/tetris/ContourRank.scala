package tetris

import Utils._
import org.apache.spark.broadcast.Broadcast
import tetris.Stack.width
import tetris.Utils._
import tetris.tetrominoes.Tetromino
import tetris.tetrominoes.Tetromino.pieces
import scala.collection.mutable.HashMap

import scala.math.max


/**
 * Created by papacharlie on 10/30/15.
 */
class ContourRank(iterations: Int = 2) {

  import ContourRank._

  val ranks: Array[Int] = Array.fill[Int](contours)(1)

  def serialCompute(): HashMap[Int, Seq[Int]] = {
    readStackMap match {
      case Some(m) => {
        val map: HashMap[Int, Seq[Int]] = new HashMap()
        m.foreach { case (i, seq) => map += i -> seq }
        map
      }
      case None => {
        val map: HashMap[Int, Seq[Int]] = new HashMap()
        for (contour <- 0 to contours) {
          map += contour -> serialMap(contour)
        }
        map
      } |-> { map => saveStackMap(map.toMap) }
    }
  }

}

object ContourRank {

  val contours: Int = 43046721 + 1

  def mapWithStack(bRanks: Broadcast[Array[Int]])(contour: Int): Seq[(Int, Int)] = {
    Contour.fromBase10(contour).map(_.toStack) match {
      case Some(stack) => (0 until width).flatMap { x =>
        (0 to 4).flatMap { orientation =>
          pieces.flatMap { piece =>
            stack + piece.copy(x, orientation) match {
              case Some(s) if s.hasNoHoles => Some(contour, bRanks.value(s.contour.toBase10))
              case _ => None
            }
          }
        }
      }
      case _ => Seq()
    }
  }

  def mapWithContour(bRanks: Broadcast[Array[Int]])(contour: Int): Seq[(Int, Int)] = {
    Contour.fromBase10(contour) match {
      case Some(c) => (0 until width).flatMap { x =>
        (0 to 4).flatMap { orientation =>
          pieces.flatMap { piece =>
            c + piece.copy(x, orientation) match {
              case Some(c) => Some(contour, bRanks.value(c.toBase10))
              case _ => None
            }
          }
        }
      }
      case _ => Seq()
    }
  }

  def mapWithContour(contour: Int): Seq[(Int, Int)] = {
    Contour.fromBase10(contour) match {
      case Some(c) => (0 until width).flatMap { x =>
        (0 to 4).flatMap { orientation =>
          pieces.flatMap { piece =>
            c + piece.copy(x, orientation) match {
              case Some(c) => Some(contour, c.toBase10)
              case _ => None
            }
          }
        }
      }
      case _ => Seq()
    }
  }

  def serialMap(contour: Int): Seq[Int] = {
    Contour.fromBase10(contour) match {
      case Some(c) => (0 until width).flatMap { x =>
        (0 to 4).flatMap { orientation =>
          pieces.flatMap { piece =>
            (c + piece.copy(x, orientation)).map(_.toBase10)
          }
        }
      }
      case _ => Seq()
    }
  }
}