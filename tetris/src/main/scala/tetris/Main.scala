package tetris

import Utils._
import tetris.strategies.RankedGame
import tetris.strategies.Strategy.GameLostException

object Main extends App {

  mkdirp(arrays)
  mkdirp(maps)

  lazy val ranks = ContourRank.loadRanks
  //  lazy val ranks = Array.fill[Int](ContourRank.contours)(10)

  args.headOption.getOrElse("playRanked") match {
    case "computeMap" => ContourRank.computeMap()
    //    case "runIterations" => ContourRank.runIterations(4)
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
