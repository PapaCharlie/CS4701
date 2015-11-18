package tetris

import scala.collection.JavaConversions._

import org.apache.spark.{SparkConf, SparkContext}
import scala.io.Source
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


import scala.collection.mutable.HashMap

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

  implicit class Pipe[T](val t: T) extends AnyVal {
    def |->(fun: T => Unit): T = {
      fun(t)
      t
    }

    def |>[U](fun: T => U): U = fun(t)
  }

  val rankArrayFilename = "rank_array.arr"
  val rankMapFilename = "rank_map.map"

  def saveHashMap(filename: String, map: HashMap[Int, Seq[Int]]): Unit = {
    val file = new FileOutputStream(filename)
    map.foreach { case (c, seq) =>
      IOUtils.write(s"$c,${seq.mkString(",")}\n", file)
    }
  }

  def savePartedHashMap(filename: String, map: HashMap[Int, Seq[Int]], part: Int): Unit = {
    saveHashMap(s"$filename.$part", map)
  }

  def readHashMap(filename: String): Option[HashMap[Int, Seq[Int]]] = {
    if (new File(filename).exists()) {
      val lines = IOUtils.readLines(new FileInputStream(filename))
      val map: HashMap[Int, Seq[Int]] = new HashMap()
      lines.map { line =>
        val nums = line.split(",")
        map += Integer.parseInt(nums.head) -> nums.tail.map(Integer.parseInt)
      }
      if (map.isEmpty) None else Some(map)
    } else {
      None
    }
  }

  def readPartedHashMap(filename: String, parts: Int): Option[HashMap[Int, Seq[Int]]] = {
    val map: HashMap[Int, Seq[Int]] = new HashMap()
    def loadMaps(part: Int): Unit = {
      if (new File(s"$filename.$part").exists()) {
        val lines = IOUtils.readLines(new FileInputStream(filename + "." + part.toString))
        lines.map { line =>
          val nums = line.split(",")
          map += Integer.parseInt(nums.head) -> nums.tail.map(Integer.parseInt)
        }
        loadMaps(part + 1)
      }
    }
    loadMaps(0)
    if (map.isEmpty) None else Some(map)
  }
}
