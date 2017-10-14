package com.github.chawasit.smc.simulator

import org.rogach.scallop._

class ArgumentConfiguration(arguments: Seq[String])
  extends ScallopConf(arguments) {
  val input: ScallopOption[String] = trailArg[String](descr = "path to the machine code file")

  printedName = "Simulator"

  verify()
}
