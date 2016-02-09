package tetris.tetrominoes

/**
 * Created by papacharlie on 10/30/15.
 */
trait Color {
  def console: String
  def toJSColor: String

}

object Color {

  case class Red() extends Color {
    def console = Console.RED
    def toJSColor = "red"
  }

  case class Blue() extends Color {
    def console = Console.BLUE
    def toJSColor = "blue"
  }

  case class Green() extends Color {
    def console = Console.GREEN
    def toJSColor = "green"
  }

  case class Yellow() extends Color {
    def console = Console.BLUE.replace("34", "93")
    def toJSColor = "yellow"
  }

  case class Magenta() extends Color {
    def console = Console.MAGENTA
    def toJSColor = "magenta"
  }

  case class Cyan() extends Color {
    def console = Console.CYAN
    def toJSColor = "cyan"
  }

  case class Orange() extends Color {
    def console = Console.BLUE.replace("34","37")
    def toJSColor = "orange"
  }

  case class Black() extends Color {
    def console = Console.BLACK
    def toJSColor = "black"
  }

}

