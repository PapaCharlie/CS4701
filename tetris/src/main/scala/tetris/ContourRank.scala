package tetris

import java.util.Calendar

import tetris.tetrominoes.Tetromino

//import Utils._

import org.apache.spark.broadcast.Broadcast
import tetris.Stack.width
import tetris.Utils._
import tetris.tetrominoes.Tetromino._
import scala.collection.mutable.HashMap
import scala.math.abs

/**
 * Created by papacharlie on 10/30/15.
 */
class ContourRank(iterations: Int = 2) {

  import ContourRank._

  lazy val ranks: Array[Array[Int]] = Array.fill[Int](2, contours)(1)

  def propagateRanks(iteration: Int): Unit = {
    for (part <- 0 until parts) {
      println(s"${Calendar.getInstance.getTime.toString}: Starting part ${part + 1} of $parts")
      val stackMap = loadHashMapInt(rankMapFilename, Some(part)).get // Already checked for existence
      System.gc()
      for (contour <- (part * (contours / parts)) to ((part + 1) * (contours / parts))) {
        if (stackMap.contains(contour)) {
          ranks(iteration % 2)(contour) = stackMap(contour).map(ranks(abs(iteration - 1) % 2)(_)).sum
        }
      }
      println(s"${Calendar.getInstance.getTime.toString}: Finished part ${part + 1}")
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
          println(s"${Calendar.getInstance.getTime.toString}: Starting iteration ${iteration + 1} of $iterations")
          propagateRanks(iteration)
          println(s"${Calendar.getInstance.getTime.toString}: Finished iteration ${iteration + 1} of $iterations")
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

  def computeMap(part: Int): Unit = {
    if (!partExists(rankMapFilename, part)) {
      System.gc()
      val map: HashMap[(Int, Byte), Seq[Int]] = new HashMap()
      println(s"${Calendar.getInstance.getTime.toString}: Starting part ${part + 1} of $parts")
      for (contour <- (part * (contours / parts)) until ((part + 1) * (contours / parts))) {
        for (p <- pieces) {
          map += (contour, getID(p)) -> serialMap(contour, p)
        }
      }
      Utils.savePartedHashMapIntByte(rankMapFilename, map, part)
//      map.retain((_, _) => false)
      println(s"${Calendar.getInstance.getTime.toString}: Finished part ${part + 1}")
    }
  }

  def serialMap(contour: Int, piece: Tetromino): Seq[Int] = {
    Contour.fromBase10(contour) match {
      case Some(c) => (0 to width).flatMap { x =>
        (0 to 4).flatMap { orientation =>
          (c + piece.copy(x, orientation)).map(_.toBase10)
        }
      }
      case _ => Seq()
    }
  }
}
