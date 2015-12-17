package tetris.tetrominoes

import tetris.tetrominoes.Color.Cyan

/**
 * Shapes:
 * x
 * x
 * o  xoxx
 * x
 */
case class I(x: Int = 0, rotation: Int = 0) extends Tetromino {

  val shape0 = Seq((0, 1), (0, 2), (0, -1))
  val shape1 = Seq((-1, 0), (1, 0), (2, 0))

  var currentShape = rotation % 2 match {
    case 0 => shape0
    case 1 => shape1
  }

  def rotate = I(x, (rotation + 1) % 2)

  def allRotations: Seq[I] = (0 until 2).map(new I(x, _))

  def copy(x: Int = 0, rotation: Int = 0): I = new I(x, rotation)

  def color = new Cyan

}