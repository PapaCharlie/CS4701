package tetris

/**
 * Created by papacharlie on 10/17/15.
 */
class Stack(val pieces: Seq[Piece] = Seq(), val contour: Contour = new Contour()) {
  def +(p: Piece): Stack = {
    new Stack(pieces :+ p, contour + p)
  }
}

object Stack {

  val height = 19

  val width = 9

}