package tetris

import org.scalacheck.Gen
import tetris.Stack._

/**
  * Created by papacharlie on 11/7/15.
 */
class StackTest extends UnitSpec {


  val rotations = Gen.choose(0,3)
  val positions = Gen.choose(0,width)



}