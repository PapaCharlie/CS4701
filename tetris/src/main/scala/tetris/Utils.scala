package tetris

import java.io.{FileInputStream, File, FileOutputStream, BufferedOutputStream}

import org.apache.commons.io.IOUtils
import org.apache.spark.{SparkConf, SparkContext}
import tetris.tetrominoes.Tetromino

import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults.{pickleOps, unpickleOps}
import scala.pickling.Defaults.{stringPickler, intPickler, refUnpickler, nullPickler}

/**
 * Created by papacharlie on 10/31/15.
 */
object Utils extends {

  private implicit val intArrayPickler = Pickler.generate[Array[Double]]
  private implicit val intArrayUnpickler = Unpickler.generate[Array[Double]]

  def executeInSpark[T](fun: SparkContext => T): T = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("tetris")
    val sc = new SparkContext(conf)
    val res = fun(sc)
    sc.stop()
    res
  }

  def clearScreen() = print("\u001b[H\u001b[2J")

  def waitToPrint() = Thread.sleep(250)

  def printStacks(stacks: Iterable[Stack]) = {
    // Assumes foreach runs in order (not actually guaranteed)
    stacks.foreach { stack =>
      clearScreen()
      println(stack)
      waitToPrint()
    }
  }

  def applyPieces(stack: Stack, pieces: Iterable[Tetromino]): Seq[Stack] = {
    pieces.foldLeft(Seq(stack)) { case (stacks, piece) =>
      stacks :+ (stacks.last + piece).getOrElse(stacks.last)
    }
  }

  implicit class Pipe[T](val t: T) extends AnyVal {
    def |>[U](fun: T => U): U = fun(t)
  }

  private val rankArrayFilename = "rank_array.arr"

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


}
