package tetris.tetrominoes

import tetris.tetrominoes.Color.Yellow

/**
 * Created by papacharlie on 10/18/15.
 */
case class O(x: Int = 0, rotation: Int = 0) extends Tetromino {

  val currentShape = Seq((0, 1), (1, 1), (1, 0))

  def rotate: O = this

  def allRotations: Seq[O] = Seq(new O(x))

  def copy(x: Int = 0, rotation: Int = 0): O = new O(x, rotation)

  def color = new Yellow

}