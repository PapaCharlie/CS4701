package tetris

import java.util.Calendar

//import Utils._

import org.apache.spark.broadcast.Broadcast
import tetris.Stack.width
import tetris.Utils._
import tetris.tetrominoes.Tetromino.pieces
import scala.collection.mutable.HashMap
import scala.math.abs

/**
 * Created by papacharlie on 10/30/15.
 */
class ContourRank(iterations: Int = 2) {

  import ContourRank._

  lazy val ranks: Array[Array[Int]] = Array.fill[Int](2, contours)(1)

  def computeMap(): Unit = {
    for (part <- 0 until parts) {
      val map: HashMap[Int, Seq[Int]] = new HashMap()
      if (!partExists(rankMapFilename, part)) {
        System.gc()
        println(s"${Calendar.getInstance.getTime.toString}: Starting part $part of ${parts - 1}")
        for (contour <- (part * (contours / parts)) until ((part + 1) * (contours / parts))) {
          map += contour -> serialMap(contour)
        }
        Utils.savePartedHashMap(rankMapFilename, map, part)
        map.retain((_, _) => false)
        println(s"${Calendar.getInstance.getTime.toString}: Finished part $part of ${parts -1 }")
      }
    }
  }

  def propagateRanks(iteration: Int): Unit = {
    for (part <- 0 until parts) {
      println(s"${Calendar.getInstance.getTime.toString}: Starting part $part")
      val map = loadHashMap(rankMapFilename, Some(part)).get // Already checked for existence
      System.gc()
      for (contour <- (part * (contours / parts)) to ((part + 1) * (contours / parts))) {
        ranks(iteration % 2)(contour) = map(contour).map(ranks(abs(iteration - 1) % 2)(_)).sum
      }
      println(s"${Calendar.getInstance.getTime.toString}: Finished part $part")
    }
    saveArray(rankArrayFilename, ranks(iteration % 2), Some(iteration))
  }

  def runIterations(): Unit = {
    if (!(0 until parts).map(iterationExists(rankMapFilename, _)).forall(identity)) {
      throw new Exception("Saved mapping is incomplete! (not enough parts)")
    }
    for (iteration <- 0 until iterations) {
      loadArray(rankArrayFilename, Some(iteration)) match {
        case Some(arr) => ranks(iteration % 2) = arr
        case _ => {
          println(s"${Calendar.getInstance.getTime.toString}: Starting iteration $iteration of $iterations")
          propagateRanks(iteration)
          println(s"${Calendar.getInstance.getTime.toString}: Finished iteration $iteration of $iterations")
        }
      }
    }
  }

}

object ContourRank {

  val parts = 500
  val contours: Int = 43046721 + 1

  def loadRanks: Array[Int] = {
    loadArray(rankArrayFilename) match {
      case Some(arr) => arr
      case _ => throw new Exception(s"Could not find rank array file at $rankArrayFilename!")
    }
  }

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
      case Some(c) => (0 to width).flatMap { x =>
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
