package tetris.tetrominoes

/**
 * Created by papacharlie on 10/30/15.
 */
trait Color

object Color {

  case class Red() extends Color
  case class Blue() extends Color
  case class Green() extends Color
  case class Yellow() extends Color
  case class Magenta() extends Color
  case class Cyan() extends Color
  case class Orange() extends Color
  case class Black() extends Color

}

