package tetris.tetrominoes

import tetris.Piece

/**
 * Created by papacharlie on 10/18/15.
 */
case class Z(x: Int) extends Piece {

  val shape0 = Seq((-1, 0), (0, 1), (-1, -1))
  val shape1 = Seq((-1, 0), (1, -1), (0, -1))

  var currentShape = shape0

  private var config = 0

  def rotate = {
    val k = new Z(x)
    k.currentShape = config match {
      case 0 => shape1
      case 1 => shape0
    }
    k.config += 1
    k.config %= 2
    k
  }

}