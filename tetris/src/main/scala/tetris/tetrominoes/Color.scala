package tetris.tetrominoes

/**
 * Created by papacharlie on 10/30/15.
 */
trait Color {
  def console: String
}

object Color {

  case class Red() extends Color {
    def console = Console.RED
  }

  case class Blue() extends Color {
    def console = Console.BLUE
  }

  case class Green() extends Color {
    def console = Console.GREEN
  }

  case class Yellow() extends Color {
    def console = Console.BLUE.replace("34", "93")
  }

  case class Magenta() extends Color {
    def console = Console.MAGENTA
  }

  case class Cyan() extends Color {
    def console = Console.CYAN
  }

  case class Orange() extends Color {
    def console = Console.BLUE.replace("34","37")
  }

  case class Black() extends Color {
    def console = Console.BLACK
  }

}

