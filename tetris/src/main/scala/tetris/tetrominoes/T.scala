package tetris.tetrominoes

/**
 * Created by papacharlie on 10/18/15.
 */
case class T(x: Int, rotation: Int = 0) extends Tetromino {

  val shape0 = Seq((0, 1), (1, 0), (0, -1))
  val shape1 = Seq((-1, 0), (1, 0), (0, -1))
  val shape2 = Seq((-1, 0), (0, 1), (0, -1))
  val shape3 = Seq((-1, 0), (0, 1), (1, 0))

  var currentShape = rotation match {
    case 0 => shape1
    case 1 => shape2
    case 2 => shape3
    case 3 => shape0
  }

  def rotate: T = T(x, (rotation + 1) % 4)

  def allRotations: Seq[T] = (0 to 3).map(new T(x, _))

}