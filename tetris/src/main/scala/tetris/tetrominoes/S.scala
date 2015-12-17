package tetris.tetrominoes

import tetris.tetrominoes.Color.Green

/**
 * Shape:
 * x
 * ox  ox
 *  x xx
 */
case class S(x: Int = 0, rotation: Int = 0) extends Tetromino {

  val shape0 = Seq((0, 1), (1, 0), (1, -1))
  val shape1 = Seq((1, 0), (0, -1), (-1, -1))

  var currentShape = rotation % 2 match {
    case 0 => shape0
    case 1 => shape1
  }

  def rotate = S(x, (rotation + 1) % 2)

  def allRotations: Seq[S] = (0 until 2).map(new S(x, _))

  def copy(x: Int = 0, rotation: Int = 0): S = new S(x, rotation)

  def color = new Green

}