package tetris

import Utils._

object Main extends App {

  mkdirp(arrays)
  mkdirp(maps)

  args.headOption.getOrElse("playRanked") match {
    case "computeMap" => ContourRank.computeMap()
    case "runIterations" => new ContourRank(4).runIterations()
    case "loadRanks" => ContourRank.loadRanks
    case "playRanked" => {
      import tetrominoes.{S, Z}
      val game = new RankedGame
      game.generator.preview(1).head match {
        case S(_, _) | Z(_, _) => game.generator.next
        case _ =>
      }
      for (_ <- 0 to 15) {
        game.play()
      }
    }
    case _ => println("Unknown game mode")
  }
  //  scala.io.StdIn.readLine()

}
