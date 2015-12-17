package tetris.randomizers

import tetris.tetrominoes.Tetromino

/**
 * Created by papacharlie on 12/17/15.
 */
abstract class Randomizer {

  def preview(n: Int): IndexedSeq[Tetromino]

  def next(): Tetromino

}
