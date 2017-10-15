# Scala-SMC-Simulator
instruction level mips simulator

## Architecture
- 8 Registers, 32-bit
- word addresses
- memory available size is 65536 words
- bit 0 is LSB

## Register
- 0 constant 0
- 1 n input to function
- 2 r input to function
- 3 return value of function
- 4 local variable for function
- 5 stack pointer
- 6 temporary value
- 7 return address 

## Instruction Type

### R-Type Bits
| 31 - 25 | 24 - 22 | 21 - 19 | 18 - 16 | 15 - 3 | 2 - 0 |
|:---:|:---:|:---:|:---:|:---:|:---:|
| 0 | OPCODE | rs | rt | 0 | rd |

### I-Type Bits
| 31 - 25 | 24 - 22 | 21 - 19 | 18 - 16 | 15 - 0 |
|:---:|:---:|:---:|:---:|:---:|
| 0 | OPCODE | rs | rt | offset |

### J-Type Bits
| 31 - 25 | 24 - 22 | 21 - 19 | 18 - 16 | 15 - 0 |
|:---:|:---:|:---:|:---:|:---:|
| 0 | OPCODE | rs | rt | 0 |

### O-Type Bits
| 31 - 25 | 24 - 22 | 21 - 0 |
|:---:|:---:|:---:|
| 0 | OPCODE | 0 |

### F-Type Bits
| 31 - 0 |
|:---:|
| Integer |

## Instruction
| Assembly | Opcode in binary  | Action  |
|:---:|:---:|:---:|
| add (R-Type format) | 000 | rd = rs + rt  |
| nand (R-Type format) | 001 | rd = ~(rs & rt) |
| lw (I-Type format) | 010 | rt = memory[rs + offet] |
| sw (I-Type format) | 011 | memory[rs + offet] = rt |
| beq (I-Type format) | 100 | if(rs == rt) go to pc + 1 + offset |
| jalr (J-Type format) | 101 | rt = pc + 1; to go rs |
| halt (O-Type format) | 110 | halt program |
| noop (O-Type format) | 111 | do nothing |

