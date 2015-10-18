package tetris.tetrominoes

import tetris.Piece

/**
 * Created by papacharlie on 10/18/15.
 */
case class O(x: Int) extends Piece {

  val currentShape = Seq((0, 1), (1, 1), (1, 0))

  def rotate: O = this

}