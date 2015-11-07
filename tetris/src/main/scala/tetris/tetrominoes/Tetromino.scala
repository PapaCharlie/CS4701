package tetris.tetrominoes

import tetris.Stack.{Square, height, width}

/**
 * Created by papacharlie on 10/17/15.
 */
abstract class Tetromino {

  def x: Int

  def rotation: Int

  if (x < 0 || x > width) {
    throw new IllegalArgumentException("Piece is out of board bounds! (%d)".format(x))
  }

  def rotate: Tetromino

  protected def currentShape: Seq[Square]

  def fits: Boolean = currentShape.forall { case (posx, _) => posx + x >= 0 && posx + x <= width }

  def getSquares(y: Int): Seq[Square] = {
    (currentShape :+(0, 0)).map { case (dx, dy) => (x + dx, y + dy) }
  }

  def allRotations: Seq[Tetromino]

  def copy(x: Int, rotation: Int): Tetromino

  def copy: Tetromino = copy(0, 0)

  def color: Color

}

object Tetromino {
  val pieces: Seq[Tetromino] = Array(new J, new L, new O, new S, new T, new Z, new I)
}