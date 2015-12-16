package tetris

import Utils._
import tetris.strategies.RankedGame
import tetris.strategies.Strategy.GameLostException
import tetris.tetrominoes._
import tetris.tetrominoes.Tetromino._

object Main extends App {

  mkdirp(arrays)
  mkdirp(maps)

  lazy val ranks = ContourRank.loadRanks
//    lazy val ranks = Array.fill[Int](ContourRank.contours)(10)

  args.headOption.getOrElse("playRanked") match {
    case "computeMap" => ContourRank.computeMap()
    case "runIterations" => ContourRank.runIterations(4)
    case "tetrominoes" => {
      for (p <- pieces.flatMap(_.allRotations)) {
        printTetromino(p)
      }
    }
    case "showRowClear" => {
      val s = (new Stack ++ Seq(J(4), S(2,1), Z(1), O(8), O(2), L(5), I(1,1))).get
      println(s)
      println((s + (I(7), false)).get)
      println((s + I(7)).get)
    }
    case "loadRanks" => ContourRank.loadRanks
    case "playRanked" => {
      import tetrominoes.{S, Z}
      while (true) {
        System.gc()
        val game = new RankedGame
        game.generator.preview(1).head match {
          case S(_, _) | Z(_, _) => game.generator.next
          case _ =>
        }
        try{
          for (turn <- 0 to 15) {
            println(turn)
            game.play()
            waitToPrint()
            clearScreen()
            println(game.currentStack)
          }
        } catch {
          case GameLostException(msg) => println(msg)
        }
        scala.io.StdIn.readLine()
      }
    }
    case _ => println("Unknown game mode")
  }

}
