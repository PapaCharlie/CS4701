package tetris

import java.io._
import org.apache.commons.io.IOUtils

import scala.pickling._         // This imports names only
import scala.pickling.binary._    // Imports PickleFormat
import scala.pickling.static._  // Avoid runtime pickler

// Import pickle ops
import scala.pickling.Defaults.{ pickleOps, unpickleOps }
// Alternatively import pickle function
// import scala.pickling.functions._

// Import picklers for specific types
import scala.pickling.Defaults.{ stringPickler, intPickler, refUnpickler, nullPickler }


import tetris.tetrominoes.Tetromino

/**
 * Created by papacharlie on 10/30/15.
 */
class ContourRank {

  private implicit val intArrayPickler = Pickler.generate[Array[Int]]
  private implicit val intArrayUnpickler = Unpickler.generate[Array[Int]]
  private val rankArrayFilename = "rank_array.arr"

  var ranks: Array[Array[Int]] = Array()

  def saveArray(array: Array[Int], filename: String): Unit = {
    val bos = new BufferedOutputStream(new FileOutputStream(filename))
    Stream.continually(bos.write( array.pickle.value ))
    bos.close()
  }

  def saveRankArray(array: Array[Int]): Unit = {
    saveArray(array, rankArrayFilename)
  }

  def readIntArray(filename: String): Option[Array[Int]] = {
    if (new File(rankArrayFilename).exists()) {
      Some(IOUtils.toByteArray(new FileInputStream(rankArrayFilename)).unpickle[Array[Int]])
    } else {
      None
    }
  }

  def readRankArray: Option[Array[Int]] = {
    readIntArray(rankArrayFilename)
  }


  def rank(iteration: Int, stack: Stack) = {

  }

  def rankPiece(iteration: Int, stack: Stack, piece: Tetromino) = {

  }

  def rankOrientation(iteration: Int, stack: Stack, piece: Tetromino, orientation: Int) = {

  }

}
