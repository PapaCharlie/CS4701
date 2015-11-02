package tetris

import tetris.tetrominoes.Tetromino

/**
 * Created by papacharlie on 10/31/15.
 */
object Utils {

  def clearScreen = print("\u001b[H\u001b[2J")

  def printStacks(stacks: Iterable[Stack]) = {
    // Assumes foreach runs in order (not actually guaranteed)
    stacks.foreach { stack =>
      clearScreen
      println(stack)
      Thread.sleep(250)
    }
  }

  def applyPieces(stack: Stack, pieces: Iterable[Tetromino]): Seq[Stack] = {
    pieces.foldLeft(Seq(stack)) { case (stacks, piece) =>
      stacks :+ (stacks.last + piece).getOrElse(stacks.last)
    }
  }

  implicit class Pipe[T](val t: T) extends AnyVal {
    def |>[U](fun: T => U): U = fun(t)
  }

}
