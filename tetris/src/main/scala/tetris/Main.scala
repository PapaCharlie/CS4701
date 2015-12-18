package tetris

import Utils._
import tetris.randomizers.TGMRandomizer
import tetris.strategies.{MinimaxGame, RankedGame}
import tetris.strategies.Strategy.GameLostException
import tetris.tetrominoes.Color.Red
import tetris.tetrominoes._
import tetris.tetrominoes.Tetromino._

object Main extends App {

  mkdirp(arrays)
  mkdirp(maps)

  lazy val ranks = ContourRank.loadRanks

  args.headOption.getOrElse("playRanked") match {
    case "computeMap" => ContourRank.computeMap()
    case "runIterations" => ContourRank.runIterations(6)
    case "tetrominoes" => {
      for (p <- pieces.flatMap(_.allRotations)) {
        printTetromino(p)
      }
    }
    case "showRowClear" => {
      val s = (new Stack ++ Seq(J(4), S(2, 1), Z(1), O(8), O(2), L(5), I(1, 1))).get
      println(s)
      println((s +(I(7), false)).get)
      println((s + I(7)).get)
    }
    case "loadRanks" => ContourRank.loadRanks
    case "testContour" => {
      val c = (new Stack).contour
      println((c + T(4)).get.toStack)
      val zerp = (c + T(4)).get
      println((zerp + T(3, 3)).get.toStack)
    }
    case "testRandomizer" => {
      val rng = new TGMRandomizer()
      //      println(rng.bag.mkString("[",",","]"))
      val stats = Array.ofDim[Int](pieces.length)
      for (_ <- 0 until 2000) {
        stats(toID(rng.next())) += 1
      }
      println(stats.mkString("[", ",", "]"))
      println(stats.indices.map(n => fromID(n.toByte)))
    }
    case "playRanked" => {
      import tetrominoes.{S, Z}
      var continue = true
      while (continue) {
        System.gc()
        val game = new RankedGame
        game.generator.preview(1).head match {
          case S(_, _) | Z(_, _) => game.generator.next
          case _ =>
        }
        var turn = 0
        try {
          while (true) {
            println(turn)
            game.play()
            waitToPrint()
            clearScreen()
            println(game.currentStack)
            turn += 1
          }
        } catch {
          case GameLostException(msg) => {
            clearScreen()
            waitToPrint()
            println(game.currentStack.toLoserStack())
            println(new Red().console + "GAME OVER!" + Console.RESET)
            println(msg)
            println(s"$turn turns played.")
          }
        }
        print("Press enter to play again, or q to quit:")
        var message = scala.io.StdIn.readLine().stripLineEnd
        while (message != "q" && message != "") {
          print("Press enter to play again, or q to quit:")
          message = scala.io.StdIn.readLine()
        }
        if (message == "q") {
          continue = false
        }
      }
    }
    case "rankedStats" => {
      val averageTime = Array.ofDim[Double](11)
      for (h <- 5 to 15) {
        println(s"Getting height $h")
        val times = for (n <- 0 until 10) yield {
          val game = new RankedGame(4, h)
          var count = 0
          try {
            while (true) {
              game.play()
              count += 1
            }
          } catch {
            case GameLostException(_) =>
          }
          count
        }
        averageTime(h - 5) = times.sum.toDouble / 10.0
        println(s"Average time for h = $h: ${averageTime(h - 5)}")
      }
      println(averageTime.mkString("[", ",", "]"))
    }
    case "playMiniMax" => {
      import tetrominoes.{S, Z}
      while (true) {
        System.gc()
        val game = new MinimaxGame
        game.generator.preview(1).head match {
          case S(_, _) | Z(_, _) => game.generator.next
          case _ =>
        }
        try {
          var turn = 0
          while (true) {
            println(turn)
            game.play()
            waitToPrint()
            clearScreen()
            println(game.currentStack)
            turn += 1
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
