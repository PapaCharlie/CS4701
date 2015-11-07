package tetris

import Utils._
import org.apache.spark.broadcast.Broadcast
import tetris.Stack.width
import tetris.Utils._
import tetris.tetrominoes.Tetromino
import tetris.tetrominoes.Tetromino.pieces

import scala.math.max


/**
 * Created by papacharlie on 10/30/15.
 */
class ContourRank(iterations: Int = 2) {

  import ContourRank._

  val contours: Int = 43046721 + 1

  val ranks: Array[Int] = Array.fill[Int](contours)(1)

  def rank(iteration: Int, contour: Contour): Double = {
    def rankPiece(iteration: Int, stack: Stack, piece: Tetromino): Double = {
      def rankOrientation(iteration: Int, stack: Stack, piece: Tetromino, orientation: Int): Double = {
        (0 to width)
          .flatMap(stack + piece.copy(_, orientation))
          .filter(_.hasNoHoles)
          //          .map(x => ranks((iteration - 1) % 2)(x.contour.fromBase9))
          .map(_ => 1.0)
          .foldLeft(0.0)(max)
      }
      (0 to 3).map(rankOrientation(iteration, stack, piece, _)).max
    }
    val stack = contour.toStack
    pieces.map(rankPiece(iteration, stack, _)).sum / pieces.length
  }

  def compute() = executeInSpark { sc =>
    for (iteration <- 0 to iterations) {
      val bRanks = sc.broadcast(ranks)
      sc.parallelize(0 to 20)
        .flatMap(mapWithStack(bRanks))
        .reduceByKey(_ + _)
        .collect()
        .foreach { case (contour, newRank) => ranks(contour) = newRank }
      println(ranks.filter(_ != 1).mkString("[", ", ", "]"))
    }
  }
}

object ContourRank {

  def mapWithStack(bRanks: Broadcast[Array[Int]])(contour: Int): Seq[(Int, Int)] = {
    val k = Contour.fromBase10(contour).map(_.toStack)
    k match {
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

  def testAThing(total:Int) = {
    for (contour <- 0 to total) {
      val stack = Contour.fromBase10(contour).map(_.toStack)
      val c = Contour.fromBase10(contour)
      (stack, c) match {
        case (Some(stack), Some(c)) => (0 until width).flatMap { x =>
          (0 until 4).flatMap { orientation =>
            pieces.map { piece =>
              val k = c + piece.copy(x, orientation) match {
                case Some(_) => Some(contour)
                case _ => None
              }
              val l = stack + piece.copy(x, orientation) match {
                case Some(s) if s.hasNoHoles => Some(contour)
                case _ => None
              }
              if (k != l) {
                println((stack + piece.copy(x, orientation)).get.contour)
                println(s"Disagree on $contour + ${piece.copy(x, orientation)}")
              }
              k == l
            }
          }
        }
        case (Some(_),_) | (_,Some(_)) => println(s"($stack,$c)")
        case _ =>
      }
    }
  }
}