package com.github.chawasit.smc.simulator

trait TryWith {
  def TryWith[T](guards: Guard*)(block: => T): T = {
    guards foreach {_.execute()}

    block
  }

  case class Guard(condition: Boolean, exception: Throwable) {
    def execute(): Unit = if (!condition) throw exception
  }
}
