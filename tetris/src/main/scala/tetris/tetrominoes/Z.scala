package tetris.tetrominoes

import tetris.tetrominoes.Color.Red

/**
 * Created by papacharlie on 10/18/15.
 */
case class Z(x: Int = 0, rotation: Int = 0) extends Tetromino {

  val shape0 = Seq((-1, 0), (0, 1), (-1, -1))
  val shape1 = Seq((-1, 0), (1, -1), (0, -1))

  var currentShape = rotation match {
    case 0 => shape0
    case 1 => shape1
  }

  def rotate = Z(x, (rotation + 1) % 2)

  def allRotations: Seq[Z] = (0 to 1).map(new Z(x, _))

  def copy(x: Int = 0, rotation: Int = 0): Z = new Z(x, rotation)

  def color = new Red

}