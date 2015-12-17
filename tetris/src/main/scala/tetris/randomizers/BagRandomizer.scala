package tetris.randomizers

import java.util.Random

import tetris.tetrominoes.Tetromino.pieces
import tetris.tetrominoes._

import scala.collection.mutable.Queue

/**
 * Created by papacharlie on 10/18/15.
 */
class BagRandomizer extends Randomizer {

  private val rng = new Random()

  val bag: Array[Tetromino] = pieces.flatMap(piece => (1 to 5).map(_ => piece.copy)).toArray

  var history: Seq[Tetromino] = Seq()

  private val toCome: Queue[Tetromino] = Queue()

  private def pickAndEnqueue() = {
    val n = rng.nextInt(35)
    val letter = bag(n)
    history = letter +: history
    bag(n) = history.last
    history = history.slice(0, 35)
    toCome.enqueue(letter)
  }

  def next(): Tetromino = {
    pickAndEnqueue()
    toCome.dequeue()
  }

  /**
   * Non-destructive
   */
  def preview(n: Int = 1): IndexedSeq[Tetromino] = {
    if (toCome.length >= n) {
      toCome.slice(0, n).toIndexedSeq
    } else {
      while (toCome.length != n) {
        pickAndEnqueue()
      }
      toCome.slice(0, n).toIndexedSeq
    }
  }

}