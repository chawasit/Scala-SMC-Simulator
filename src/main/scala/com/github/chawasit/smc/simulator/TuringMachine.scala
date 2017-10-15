package com.github.chawasit.smc.simulator

case class TuringMachine(programCounter: Int
                         , memory: Array[Int]
                         , instructionRegister: Int
                         , registers: Array[Int]
                         , instructionCount: Int)
  extends TryWith {
  def printState(): Unit = {
    println(
      s"""
         |@@@
         |state:
         |    pc $programCounter""".stripMargin)

    println("    memory:")
    List.range(0, instructionCount).foreach { address =>
      println(s"        mem[ $address ] ${memory(address)}")
    }

    println("    registers:")
    registers.zipWithIndex.foreach { case (value, address) =>
      println(s"        reg[ $address ] $value")
    }
    println("end start")
  }

  def run: TuringMachine = nextCycle


  private def nextCycle: TuringMachine = {
    printState()
    fetch decodeAndExecute
  }

  private def fetch: TuringMachine =
    copy(
      programCounter = programCounter + 1
      , instructionRegister = loadFromMemory(programCounter)
    )

  private def decodeAndExecute: TuringMachine = {
    opcode match {
      case 0 => add
      case 1 => nand
      case 2 => loadWord
      case 3 => storeWord
      case 4 => branchOnEqual
      case 5 => jumpAndLinkRegister
      case 6 => halt
      case 7 => noop
      case _ => throw new UnknownOpcodeException(instructionRegister)
    }
  }

  private def add: TuringMachine = {
    val sum = regA + regB

    storeToRegister(rd, sum) nextCycle
  }

  private def nand: TuringMachine = {
    val nand = ~(regA & regB)

    storeToRegister(rd, nand) nextCycle
  }

  private def loadWord: TuringMachine = {
    val address = regA + offset
    val value = loadFromMemory(address)

    storeToRegister(rt, value) nextCycle
  }

  private def storeWord: TuringMachine = {
    val address = regA + offset

    storeToMemory(address, regB) nextCycle
  }

  private def branchOnEqual: TuringMachine = {
    regA == regB match {
      case true => jumpToAddress(programCounter + offset) nextCycle
      case false => nextCycle
    }
  }

  private def jumpAndLinkRegister: TuringMachine = {
    val address = regA

    storeToRegister(rt, programCounter) jumpToAddress address nextCycle
  }

  private def halt: TuringMachine = this

  private def noop: TuringMachine = nextCycle

  private lazy val opcode = instructionRegister >>> 22
  private lazy val rs = (instructionRegister >>> 19) & 7
  private lazy val rt = (instructionRegister >>> 16) & 7
  private lazy val rd = instructionRegister & 0x7
  private lazy val offset = (instructionRegister << 16) >> 16

  private lazy val regA = loadFromRegister(rs)
  private lazy val regB = loadFromRegister(rt)

  private def currentInstruction: Int = memory(programCounter)

  private def increaseProgramCounter: TuringMachine = copy(programCounter = programCounter + 1)

  private def jumpToAddress(address: Int): TuringMachine = TryWith(
    Guard(isValidProgramCounter(address), new ProgramCounterOutOfBoundException(address))
  ) {
    copy(programCounter = address)
  }

  private def loadFromRegister(address: Int): Int = TryWith(
    Guard(isValidRegisterAddress(address), new RegisterAddressOutOfBoundException(address))
  ) {
    registers(address)
  }

  private def storeToRegister(address: Int, value: Int): TuringMachine = TryWith(
    Guard(isValidMemoryAddress(address), new MemoryAddressOutOfBoundException(address))
  ) {
    copy(registers = registers.updated(address, value))
  }

  private def loadFromMemory(address: Int): Int = TryWith(
    Guard(isValidMemoryAddress(address), new MemoryAddressOutOfBoundException(address))
  ) {
    memory(address)
  }

  private def storeToMemory(address: Int, value: Int): TuringMachine = TryWith(
    Guard(isValidMemoryAddress(address), new MemoryAddressOutOfBoundException(address))
  ) {
    copy(memory = memory.updated(address, value))
  }

  private def isValidMemoryAddress(address: Int): Boolean = 0 <= address && address < MAX_MEMORY_ADDRESS

  private def isValidRegisterAddress(address: Int): Boolean = 0 <= address && address < MAX_REGISTER_ADDRESS

  private def isValidProgramCounter(address: Int): Boolean = 0 <= address && address < instructionCount
}


object TuringMachine {
  def apply(instructions: Array[Int]): TuringMachine = {
    val memory = new Array[Int](MAX_MEMORY_ADDRESS)

    storeInstructions(memory, instructions)

    TuringMachine(
      programCounter = 0
      , memory = memory
      , instructionRegister = 0
      , registers = new Array[Int](MAX_REGISTER_ADDRESS)
      , instructionCount = instructions.length)
  }

  private def storeInstructions(memory: Array[Int], instructions: Array[Int]): Unit =
    instructions.zipWithIndex.foreach { case (instruction, address) =>
      println(s"memory[$address]=$instruction")
      memory(address) = instruction
    }
}
