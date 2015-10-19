package tetris.tetrominoes

/**
 * Created by papacharlie on 10/18/15.
 */
case class J(x: Int = 0, rotation: Int = 0) extends Tetromino {

  val shape0 = Seq((0, 1), (0, -1), (-1, -1))
  val shape1 = Seq((-1, 0), (-1, 1), (1, 0))
  val shape2 = Seq((0, 1), (1, 1), (0, -1))
  val shape3 = Seq((-1, 0), (1, 0), (1, -1))

  val currentShape = rotation match {
    case 0 => shape0
    case 1 => shape1
    case 2 => shape2
    case 3 => shape3
  }

  def rotate: L = L(x, (rotation + 1) % 4)

  def allRotations: Seq[L] = (0 to 3).map(new L(x, _))

  def copy(x: Int = 0, rotation: Int = 0): J = new J(x, rotation)

}