package tetris

import java.io.{DataOutputStream, File, FileInputStream, FileOutputStream}
import java.nio.channels.FileChannel.MapMode._

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

  def waitToPrint() = Thread.sleep(100)

  def printStacks(stacks: Iterable[Stack]) = {
    // Assumes foreach runs in order (not actually guaranteed)
    stacks.foreach { stack =>
      clearScreen()
      println(stack)
      waitToPrint()
    }
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

  def biggestPart(filename: String): Option[String] = {
    val f = filename.split(File.separatorChar)
    f.take(f.length - 1).mkString(File.separator) match {
      case "" => new File(".").list.filter(_.contains(f.last)).sorted.lastOption
      case dir => new File(dir).list.filter(_.contains(f.last)).sorted.lastOption.map(dir + File.separator + _)
    }
  }

  def saveArrayInt(filename: String, arr: Array[Int], iteration: Option[Int] = None): Unit = {
    def save(filename: String) = {
      val file = new DataOutputStream(new FileOutputStream(filename))
      for (n <- arr.indices) {
        file.writeInt(arr(n))
        if (n % 128 == 0) { // per block size
          file.flush()
        }
      }
      file.flush()
      file.close()
    }
    iteration match {
      case Some(i) => save(s"$filename.$i")
      case _ => save(filename)
    }
  }

  def saveArrayDouble(filename: String, arr: Array[Float], iteration: Option[Int] = None): Unit = {
    def save(filename: String) = {
    val file = new DataOutputStream(new FileOutputStream(filename))
      for (n <- arr.indices) {
        file.writeFloat(arr(n))
        if (n % 128 == 0) { // per block size
          file.flush()
        }
      }
      file.flush()
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
        val file = new File(filename)
        val fileSize = file.length
        val stream = new FileInputStream(file)
        val buffer = stream.getChannel.map(READ_ONLY, 0, fileSize)
        for (n <- arr.indices) {
          arr(n) = buffer.getInt()
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

  def loadArrayDouble(filename: String, iteration: Option[Int] = None, size: Int = ContourRank.contours): Option[Array[Float]] = {
    def load(filename: String): Option[Array[Float]] = {
      if (new File(filename).exists()) {
        val arr = Array.fill[Float](size)(0.0.toFloat)
        val file = new File(filename)
        val fileSize = file.length
        val stream = new FileInputStream(file)
        val buffer = stream.getChannel.map(READ_ONLY, 0, fileSize)
        for (n <- arr.indices) {
          arr(n) = buffer.getFloat()
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
