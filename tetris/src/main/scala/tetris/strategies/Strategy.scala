package tetris.strategies

import tetris.randomizers.TGMRandomizer
import tetris.Stack

/**
 * Created by papacharlie on 12/15/15.
 */
abstract class Strategy {

  val generator = new TGMRandomizer

  var currentStack = new Stack

  def play(): Unit

}

object Strategy {
  case class GameLostException(message: String) extends Exception(message)
}