package tetris.randomizers

import java.util.Random

import tetris.tetrominoes.Tetromino._
import tetris.tetrominoes._

import scala.collection.mutable.Queue
import scala.util.Random.shuffle

class TGMRandomizer extends Randomizer {
  private val rng = new Random()

  private val tetrominoes = pieces.length

  private val starters = Seq(new I, new T, new L, new J)

  private val toCome: Queue[Byte] = Queue(toID(starters(rng.nextInt(starters.length))))

  val history: Queue[Byte] = Queue(shuffle(Seq(new Z, new Z, new S, new S).map(toID)): _*)

  private def pickNext(tries: Int = 6): Byte = {
    val next = toID(pieces(rng.nextInt(tetrominoes)))
    (history.contains(next), tries) match {
      case (false, _) => next
      case (true, 0) => next
      case _ => pickNext(tries - 1)
    }
  }

  private def pickAndEnqueue(): Unit = {
    val next = pickNext()
    toCome.enqueue(next)
    history.dequeue()
    history.enqueue(next)
  }

  def next(): Tetromino = {
    pickAndEnqueue()
    fromID(toCome.dequeue())
  }

  def preview(n: Int = 1): IndexedSeq[Tetromino] = {
    if (toCome.length >= n) {
      toCome.slice(0, n).map(fromID).toIndexedSeq
    } else {
      while (toCome.length != n) {
        pickAndEnqueue()
      }
      toCome.slice(0, n).map(fromID).toIndexedSeq
    }
  }
}
