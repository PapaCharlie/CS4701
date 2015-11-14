package tetris

import java.io.{FileInputStream, File, FileOutputStream, BufferedOutputStream}

import org.apache.commons.io.IOUtils
import org.apache.spark.{SparkConf, SparkContext}
import tetris.tetrominoes.Tetromino

import scala.collection.mutable.Map
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.Defaults._
import scala.pickling.static._
import scala.pickling.Defaults.{pickleOps, unpickleOps}
import scala.pickling.Defaults.{stringPickler, intPickler, refUnpickler, nullPickler}

/**
 * Created by papacharlie on 10/31/15.
 */
object Utils extends {

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
    def |->(fun: T => Unit): T = {
      fun(t)
      t
    }

    def |>[U](fun: T => U): U = fun(t)
  }

  val rankArrayFilename = "rank_array.arr"
  val rankMapFilename = "rank_map.map"

  def saveArray(array: Array[Int], filename: String): Unit = {
    val bos = new BufferedOutputStream(new FileOutputStream(filename))
    Stream.continually(bos.write(array.pickle.value))
    bos.close()
  }

  def saveRankArray(array: Array[Int]): Unit = {
    saveArray(array, rankArrayFilename)
  }

  def partialSaveMap(map: Map[Int, Seq[Int]], filename: String, part: Int) = {
    saveMap(map, s"$filename.$part")
  }

  def saveMap(map: Map[Int, Seq[Int]], filename: String): Unit = {
    val bos = new BufferedOutputStream(new FileOutputStream(filename))
    Stream.continually(bos.write(map.pickle.value))
    bos.close()
  }

  def saveStackMap(map: Map[Int, Seq[Int]]) = {
    saveMap(map, rankMapFilename)
  }

  def readIntArray(filename: String): Option[Array[Int]] = {
    if (new File(filename).exists()) {
      Some(IOUtils.toByteArray(new FileInputStream(filename)).unpickle[Array[Int]])
    } else {
      None
    }
  }

  def readRankArray: Option[Array[Int]] = {
    readIntArray(rankArrayFilename)
  }

  def readMap(filename: String): Option[Map[Int, Seq[Int]]] = {
    if (new File(filename).exists()) {
      Some(IOUtils.toByteArray(new FileInputStream(filename)).unpickle[Map[Int, Seq[Int]]])
    } else {
      None
    }
  }

  def readStackMap: Option[Map[Int, Seq[Int]]] = {
    readMap(rankMapFilename)
  }

  def readMapPart(filename: String, part: Int): Option[Map[Int, Seq[Int]]] = {
    val map: Map[Int, Seq[Int]] = Map()
    readMap(s"$filename.$part") match {
      case Some(m) => map ++= m
      case _ =>
    }
    if (map.isEmpty) {
      None
    } else {
      Some(map)
    }
  }

  // def readPartialMap(filename: String, parts: Int): Option[Map[Int, Seq[Int]]] = {
  //   val map: Map[Int, Seq[Int]] = Map()
  //   for (part <- 0 until parts) {
  //     readMap(s"$filename.$part") match {
  //       case Some(m) => map ++= m
  //       case _ =>
  //     }
  //   }
  //   if (map.isEmpty) {
  //     None
  //   } else {
  //     Some(map)
  //   }
  // }

}
