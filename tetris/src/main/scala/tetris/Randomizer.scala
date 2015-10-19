package tetris

import tetris.tetrominoes._
import java.util.Random
//import scala.collection.mutable.Array

/**
 * Created by papacharlie on 10/18/15.
 */
class Randomizer {

  private val rng = new Random()

  private val pieces: Seq[Tetromino] = Array(new J, new L, new O, new S, new T, new Z)

  var bag: Array[Tetromino] = pieces.flatMap(piece => (1 to 5).map(_ => piece.copy)).toArray

  var history: Seq[Tetromino] = Seq()

  def next: Tetromino = {
    val n = rng.nextInt(35)
    val letter = bag(n)
    history :+= letter
    bag(n) = history.head
    letter
  }

}