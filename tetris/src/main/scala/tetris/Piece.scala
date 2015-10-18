package tetris

import Stack.{height, width}

/**
 * Created by papacharlie on 10/17/15.
 */
abstract class Piece {

  def x: Int

  if (x < 0 || x > width) {
    throw new IllegalArgumentException("Piece is out of board bounds! (%d)".format(x))
  }

  def rotate: Piece

  def currentShape: Seq[(Int, Int)]

  def fits: Boolean = currentShape.forall { case (posx, _) => posx + x > 0 && posx + x < width }

}