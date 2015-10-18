package tetris.tetrominoes

import tetris.Piece

/**
 * Created by papacharlie on 10/18/15.
 */
case class T(x: Int) extends Piece {

  val shape0 = Seq((0, 1), (1, 0), (0, -1))
  val shape1 = Seq((-1, 0), (1, 0), (0, -1))
  val shape2 = Seq((-1, 0), (0, 1), (0, -1))
  val shape3 = Seq((-1, 0), (0, 1), (1, 0))

  var currentShape = shape0

  private var config = 0

  def rotate: T = {
    val k = new T(x)
    k.currentShape = config match {
      case 0 => shape1
      case 1 => shape2
      case 2 => shape3
      case 3 => shape0
    }
    k.config += 1
    k.config %= 4
    k
  }

}