package tetris.strategies

import tetris.randomizers.TGMRandomizer
import tetris.Stack

abstract class Strategy {
  val generator: TGMRandomizer = new TGMRandomizer
  var currentStack: Stack = new Stack
  def play(): Unit
}

object Strategy {
  case class GameLostException(message: String) extends Exception(message)
}