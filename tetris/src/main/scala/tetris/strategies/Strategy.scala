package tetris.strategies

import tetris.{Stack, Randomizer}

/**
 * Created by papacharlie on 12/15/15.
 */
abstract class Strategy {

  val generator = new Randomizer

  var currentStack = new Stack

  def play()

}

object Strategy {
  case class GameLostException(message: String) extends Exception(message)
}