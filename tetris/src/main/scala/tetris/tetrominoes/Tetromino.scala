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

  val pieces: Seq[Tetromino] = Seq(new J, new L, new O, new S, new T, new Z, new I)

  def printTetromino(p: Tetromino): Unit = {
    var squares = p.currentShape :+ (0,0)
    squares = squares.map{case (x, y) => (x - squares.minBy(_._1)._1, y - squares.minBy(_._2)._2)}
    val layers = squares.map(_._2).distinct.sorted.reverse
    for (y <- layers) {
      for (x <- 0 to squares.filter(_._2 == y).max._1) {
        if (squares.contains((x,y))) {
          print(p.color.console + 0x25AE.toChar + Console.RESET)
        } else {
          print(" ")
        }
      }
      println()
    }
  }

  def getName(p: Tetromino): String = p match {
    case _: I => "I"
    case _: J => "J"
    case _: L => "L"
    case _: O => "O"
    case _: S => "S"
    case _: Z => "Z"
    case _: T => "T"
  }

  def toID(p: Tetromino): Byte = p match {
    case _: I => 0
    case _: J => 1
    case _: L => 2
    case _: O => 3
    case _: S => 4
    case _: Z => 5
    case _: T => 6
  }

  def fromID(id: Byte): Tetromino = id match {
    case 0 => new I
    case 1 => new J
    case 2 => new L
    case 3 => new O
    case 4 => new S
    case 5 => new Z
    case 6 => new T
  }
  
}