package org.joaogsma

fun main(args: Array<String>) {
    val result: Int = run(args[0]) { print(it) }
    println("Sum of signal strengths was $result")
}

fun run(filename: String, write: (String) -> Unit): Int {
    val instructions: List<Instruction> = readResource(filename).map {parseInstruction(it)}

    var endProgram = false
    val cpu = CPU(instructions) { endProgram = true }
    val signalStrengthMonitor = SignalStrengthMonitor { cpu.registerX }
    val crt = CRT(6, 40, { cpu.registerX }, write)
    val clock = Clock(listOf(signalStrengthMonitor, crt, cpu))

    while (!endProgram)
        clock.tick()
    return signalStrengthMonitor.getStrengthSum()
}

fun parseInstruction(line: String): Instruction {
    if (line == "noop") {
        return NoOp()
    }
    val value: Int = line.removePrefix("addx ").toInt()
    return AddToRegister(value)
}
