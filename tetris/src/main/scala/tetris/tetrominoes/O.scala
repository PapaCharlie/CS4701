package tetris.tetrominoes

/**
 * Created by papacharlie on 10/18/15.
 */
case class O(x: Int) extends Tetromino {

  val currentShape = Seq((0, 1), (1, 1), (1, 0))

  def rotate: O = this

  def allRotations: Seq[O] = Seq(new O(x))

}