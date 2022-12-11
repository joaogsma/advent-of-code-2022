package org.joaogsma

abstract class Instruction(val requiredCycles: Int)
class NoOp: Instruction(1)
class AddToRegister(val value: Int): Instruction(2)
