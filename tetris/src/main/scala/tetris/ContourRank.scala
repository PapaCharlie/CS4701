package tetris

import java.io._

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import org.apache.commons.io.IOUtils
import tetris.tetrominoes.Tetromino
import tetris.tetrominoes.Tetromino.pieces
import tetris.Stack.width
import tetris.Utils._

import scala.math.max
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults.{pickleOps, unpickleOps}
import scala.pickling.Defaults.{stringPickler, intPickler, refUnpickler, nullPickler}

/**
 * Created by papacharlie on 10/30/15.
 */
class ContourRank(iterations: Int = 100) {

  private implicit val intArrayPickler = Pickler.generate[Array[Double]]
  private implicit val intArrayUnpickler = Unpickler.generate[Array[Double]]

  private val rankArrayFilename = "rank_array.arr"

  val contours: Int = 43046721

  var ranks: Array[Array[Double]] = Array.ofDim[Double](2, contours)

  def saveArray(array: Array[Double], filename: String): Unit = {
    val bos = new BufferedOutputStream(new FileOutputStream(filename))
    Stream.continually(bos.write(array.pickle.value))
    bos.close()
  }

  def saveRankArray(array: Array[Double]): Unit = {
    saveArray(array, rankArrayFilename)
  }

  def readDoubleArray(filename: String): Option[Array[Double]] = {
    if (new File(rankArrayFilename).exists()) {
      Some(IOUtils.toByteArray(new FileInputStream(rankArrayFilename)).unpickle[Array[Double]])
    } else {
      None
    }
  }

  def readRankArray: Option[Array[Double]] = {
    readDoubleArray(rankArrayFilename)
  }

  def rank(iteration: Int, contour: Int): Double = {
    val stack = Stack.fromContour(contour)
    pieces.map(rankPiece(iteration, stack, _)).sum / pieces.length
  }

  def rankPiece(iteration: Int, stack: Stack, piece: Tetromino): Double = {
    (0 to 3).map(rankOrientation(iteration, stack, piece, _)).max
  }

  def rankOrientation(iteration: Int, stack: Stack, piece: Tetromino, orientation: Int): Double = {
    (0 to width)
      .flatMap(stack + piece.copy(_, orientation))
      .filter(_.hasNoHoles)
      .map(x => ranks((iteration - 1) % 2)(x.contour.fromBase9))
      .foldLeft(0.0)(max)
  }

  def compute = {
    for (iteration <- 1 to iterations) {
      for (contour <- 0 to contours) {
        ranks(iteration % 2)(contour) = rank(iteration, contour)
      }
    }
  }

}
