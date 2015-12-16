package tetris

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.ByteBuffer

import org.apache.commons.io.IOUtils
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._
import scala.collection.mutable.HashMap

/**
 * Created by papacharlie on 10/31/15.
 */
object Utils {

  implicit val workers : Option[Int] = None

  def executeInSpark[T](fun: SparkContext => T)(implicit workers: Option[Int] = None): T = {
    val conf = workers match {
      case Some(i) => new SparkConf().setMaster(s"local[$i]").setAppName("tetris")
      case _ => new SparkConf().setMaster("local[*]").setAppName("tetris")
    }
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

  def mkdirp(dirname: String) = {
    if (!new File(dirname).isDirectory) {
      new File(dirname).mkdir()
    }
  }

  val arrays = "ranks"
  val maps = "maps"
  val rankArrayFilename = "ranks/rank_array.arr"
  val rankMapFilename = "maps/rank_map.map"

  def intToByteArray(n: Int): String = {
    (3 to 0 by -1).map(p => ((n >> (p * 8)) & 255).toChar).mkString
  }

  def doubleToByteArray(x: Double) = {
    val l = java.lang.Double.doubleToLongBits(x)
    ByteBuffer.allocate(8).putLong(l).array()
  }

  def byteArrayToDouble(x: Array[Byte]) = {
    ByteBuffer.wrap(x).getDouble
  }

  def byteArrayToInt(seq: Iterable[Byte]): Int = {
    seq.foldLeft(0) { case (i, c) => i << 8 | c }
  }

  def biggestPart(filename: String): Option[String] = {
    val f = filename.split(File.separator)
    f.take(f.length - 1).mkString(File.separator) match {
      case "" => new File(".").list.filter(_.contains(f.last)).sorted.lastOption
      case dir => new File(dir).list.filter(_.contains(f.last)).sorted.lastOption.map(dir + File.separator + _)
    }
  }

  def saveArrayInt(filename: String, arr: Array[Int], iteration: Option[Int] = None): Unit = {
    def save(filename: String) = {
      val file = new FileOutputStream(filename)
      arr.foreach(n => IOUtils.write(intToByteArray(n), file))
      file.close()
    }
    iteration match {
      case Some(i) => save(s"$filename.$i")
      case _ => save(filename)
    }
  }

  def saveArrayDouble(filename: String, arr: Array[Double], iteration: Option[Int] = None): Unit = {
    def save(filename: String) = {
      val file = new FileOutputStream(filename)
      arr.foreach(n => IOUtils.write(doubleToByteArray(n).reverse, file))
      file.close()
    }
    iteration match {
      case Some(i) => save(s"$filename.$i")
      case _ => save(filename)
    }
  }

  def loadArrayInt(filename: String, iteration: Option[Int] = None, size: Int = ContourRank.contours): Option[Array[Int]] = {
    def load(filename: String): Option[Array[Int]] = {
      if (new File(filename).exists()) {
        val arr = Array.fill[Int](size)(0)
        val file = new FileInputStream(filename)
        for (n <- 0 until size) {
          arr(n) = byteArrayToInt((3 to 0 by -1).map(_ => file.read().toByte))
        }
        Some(arr)
      } else {
        None
      }
    }
    (iteration, biggestPart(filename)) match {
      case (Some(i), _) => load(s"$filename.$i")
      case (_, Some(s)) => load(s)
      case _ => load(filename)
    }
  }

  def loadArrayDouble(filename: String, iteration: Option[Int] = None, size: Int = ContourRank.contours): Option[Array[Double]] = {
    def load(filename: String): Option[Array[Double]] = {
      if (new File(filename).exists()) {
        val arr = Array.fill[Double](size)(0.0)
        val buf = Array.fill[Byte](8)(0)
        val file = new FileInputStream(filename)
        for (n <- 0 until size) {
          (7 to 0 by -1).foreach(i => buf(i) = file.read().toByte)
          arr(n) = byteArrayToDouble(buf)
        }
        Some(arr)
      } else {
        None
      }
    }
    (iteration, biggestPart(filename)) match {
      case (Some(i), _) => load(s"$filename.$i")
      case (_, Some(s)) => load(s)
      case _ => load(filename)
    }
  }

  def iterationExists(filename: String, iteration: Int) = new File(s"$filename.$iteration").exists()

  def partExists(filename: String, part: Int) = new File(s"$filename.$part").exists()

  def savePartedHashMapInt(filename: String, map: HashMap[Int, Seq[Int]], part: Int): Unit = {
    val file = new FileOutputStream(s"$filename.$part")
    map.foreach { case (c, seq) =>
      IOUtils.write(s"$c,${seq.mkString(",")}\n", file)
    }
    file.close()
  }

  def savePartedHashMapIntByte(filename: String, map: HashMap[(Int, Byte), Seq[Int]], part: Int): Unit = {
    val file = new FileOutputStream(s"$filename.$part")
    map.foreach { case ((c, b), seq) =>
      IOUtils.write(s"$c,$b,${seq.mkString(",")}\n", file)
    }
    file.close()
  }

  def loadHashMapInt(filename: String, part: Option[Int]): Option[HashMap[Int, Seq[Int]]] = {
    def load(filename: String): Option[HashMap[Int, Seq[Int]]] = {
      val map: HashMap[Int, Seq[Int]] = new HashMap()
      if (new File(filename).exists()) {
        val lines = IOUtils.readLines(new FileInputStream(filename))
        lines.map { line =>
          val nums = line.split(",")
          map += Integer.parseInt(nums.head) -> nums.tail.map(Integer.parseInt)
        }
      }
      if (map.isEmpty) None else Some(map)
    }
    part match {
      case Some(p) => load(s"$filename.$p")
      case _ => load(filename)
    }
  }

  def loadHashMapIntByte(filename: String, part: Option[Int]): Option[HashMap[(Int, Byte), Seq[Int]]] = {
    def load(filename: String): Option[HashMap[(Int, Byte), Seq[Int]]] = {
      val map: HashMap[(Int, Byte), Seq[Int]] = new HashMap()
      if (new File(filename).exists()) {
        val lines = IOUtils.readLines(new FileInputStream(filename))
        lines.map { line =>
          val nums = line.split(",")
          map += (Integer.parseInt(nums.head), Integer.parseInt(nums.tail.head).toByte) -> nums.tail.tail.map(Integer.parseInt)
        }
      }
      if (map.isEmpty) None else Some(map)
    }
    part match {
      case Some(p) => load(s"$filename.$p")
      case _ => load(filename)
    }
  }

}
