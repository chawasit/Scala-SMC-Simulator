package com.github.chawasit.smc.simulator

class SimulatorException(msg: String) extends Exception(msg)

class MemoryAddressOutOfBoundException(address: Int)
  extends SimulatorException(s"Memory address $address out of bounds.")

class RegisterAddressOutOfBoundException(address: Int)
  extends SimulatorException(s"Register address $address out of bounds.")

class ProgramCounterOutOfBoundException(address: Int)
  extends SimulatorException(s"Program Counter $address out of bounds.")

class UnknownOpcodeException(instruction: Int)
  extends SimulatorException(s"Unknown Opcode from instruction $instruction.")
