package org.joaogsma

class CPU(instructions: Collection<Instruction>, private val endCallback: () -> Unit):
    ClockListener {
    var registerX: Int = 1
        private set
    private var remainingInstructionCycles = instructions.first().requiredCycles
    private val instructions: MutableList<Instruction> = instructions.toMutableList()

    override fun runCycle() {
        val peeked = instructions.first()
        if (--remainingInstructionCycles > 0)
            return
        updateRegister(instructions.removeFirst())
        if (instructions.isEmpty()) {
            endCallback()
            return
        }
        remainingInstructionCycles = instructions.first().requiredCycles
    }

    private fun updateRegister(finishedInstruction: Instruction) {
        if (finishedInstruction is AddToRegister)
            registerX += finishedInstruction.value
    }
}
