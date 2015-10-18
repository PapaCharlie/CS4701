package tetris.tetrominoes

import tetris.Piece

/**
 * Created by papacharlie on 10/18/15.
 */
case class Z(x: Int, rotation: Int = 0) extends Piece {

  val shape0 = Seq((-1, 0), (0, 1), (-1, -1))
  val shape1 = Seq((-1, 0), (1, -1), (0, -1))

  var currentShape = rotation match {
    case 0 => shape1
    case 1 => shape0
  }

  def rotate = Z(x, (rotation + 1) % 2)

}