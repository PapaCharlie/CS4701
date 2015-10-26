package tetris

import tetris.tetrominoes._
import java.util.Random

import scala.collection.mutable.Queue

//import scala.collection.mutable.Array

/**
 * Created by papacharlie on 10/18/15.
 */
class Randomizer {

  private val rng = new Random()

  private val pieces: Seq[Tetromino] = Array(new J, new L, new O, new S, new T, new Z, new I)

  var bag: Array[Tetromino] = pieces.flatMap(piece => (1 to 5).map(_ => piece.copy)).toArray

  var history: Seq[Tetromino] = Seq()

  private val toCome: Queue[Tetromino] = Queue()

  private def pickAndEnqueue = {
    val n = rng.nextInt(35)
    val letter = bag(n)
    history :+= letter
    bag(n) = history.head
    toCome.enqueue(letter)
  }

  def next: Tetromino = {
    pickAndEnqueue
    toCome.dequeue
  }

  /**
   * Non-destructive
   */
  def preview(n: Int = 1): Seq[Tetromino] = {
    if (toCome.length >= n) {
      toCome.slice(0, n).toSeq
    } else {
      while (toCome.length != n) {
        pickAndEnqueue
      }
      toCome.slice(0, n).toSeq
    }
  }

}