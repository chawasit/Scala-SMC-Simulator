package com.github.chawasit.smc.simulator

import java.io.{File, PrintWriter}

import scala.io.Source

object Main extends App {
  override def main(args: Array[String]): Unit = {
    val arguments = new ArgumentConfiguration(args)

    try {
      val input = readFile(arguments.input())
      val instructions = input map {
        _.toInt
      }
      val turingMachine = TuringMachine(instructions)

      val haltedTuringMachine = turingMachine run

      haltedTuringMachine printState()
    } catch {
      case e: SimulatorException =>
        println(s"[${Console.BLUE}Simulator${Console.RESET}] ${e.getMessage}")
      case e: Exception =>
        println(s"[${Console.RED}RunTime${Console.RESET}] $e")
    }
  }


  private def writeFile(output: String, data: String): Unit = {
    val writer = new PrintWriter(new File(output))
    writer.write(data)
    writer.close()
  }

  private def readFile(path: String): Array[String] =
    Source.fromFile(path)
      .getLines()
      .toArray
}


