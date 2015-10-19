package tetris.tetrominoes

/**
 * Created by papacharlie on 10/19/15.
 */
case class I(x: Int = 0, rotation: Int = 0) extends Tetromino {

  val shape0 = Seq((0, -1), (0, 1), (0, 2))
  val shape1 = Seq((-1, 0), (1, 0), (2, 0))

  var currentShape = rotation match {
    case 0 => shape0
    case 1 => shape1
  }

  def rotate = S(x, (rotation + 1) % 2)

  def allRotations: Seq[S] = (0 to 1).map(new S(x, _))

  def copy(x: Int = 0, rotation: Int = 0): S = new S(x, rotation)

}