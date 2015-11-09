package tetris

import tetris.tetrominoes.Tetromino._
import tetris.Stack._

/**
 * Created by papacharlie on 11/7/15.
 */
class ContourRankTest {

  def testAThing(total:Int) = {
    for (contour <- 0 to total) {
      val stack = Contour.fromBase10(contour).map(_.toStack)
      val c = Contour.fromBase10(contour)
      (stack, c) match {
        case (Some(stack), Some(c)) => (0 until width).flatMap { x =>
          (0 until 4).flatMap { orientation =>
            pieces.map { piece =>
              val k = c + piece.copy(x, orientation) match {
                case Some(_) => Some(contour)
                case _ => None
              }
              val l = stack + piece.copy(x, orientation) match {
                case Some(s) if s.hasNoHoles => Some(contour)
                case _ => None
              }
              if (k != l) {
                println((stack + piece.copy(x, orientation)).get.contour)
                println(s"Disagree on $contour + ${piece.copy(x, orientation)}")
              }
              k == l
            }
          }
        }
        case (Some(_),_) | (_,Some(_)) => println(s"($stack,$c)")
        case _ =>
      }
    }
  }

}
