package tetris

import Stack.{width}
import math.{min,max}

/**
 * Created by papacharlie on 10/17/15.
 */
class Contour(stack: Stack) {

  private val contour: Int = {
    val heights = (0 to width - 1).map(stack.highestY)
    heights.tail.foldLeft((0, heights.head)) { case ((contour, last), next) =>
      val diff = max(min(next - last, 4), -4) + 4
      (contour * 10 + diff, next)
    }._1
  }

  override def hashCode = contour

  override def toString = contour.toString

}