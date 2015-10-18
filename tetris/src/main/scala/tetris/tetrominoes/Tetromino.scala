package tetris.tetrominoes

import tetris.Stack.{Square, height, width}

/**
 * Created by papacharlie on 10/17/15.
 */
abstract class Tetromino {

  def x: Int

  if (x < 0 || x > width) {
    throw new IllegalArgumentException("Piece is out of board bounds! (%d)".format(x))
  }

  def rotate: Tetromino

  protected def currentShape: Seq[Square]

  def fits: Boolean = currentShape.forall { case (posx, _) => posx + x > 0 && posx + x < width }

  def getSquares(y: Int): Seq[Square] = {
    (currentShape :+(0, 0)).map { case (dx, dy) => (x + dx, y + dy) }
  }

}