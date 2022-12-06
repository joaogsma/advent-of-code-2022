package org.joaogsma

import java.util.Stack

fun main(args: Array<String>) {
    val filename: String = getFilename(args[0])
    val result: String =
        when (val version = args[1]) {
            "1" -> runProblem1(filename)
            "2" -> runProblem2(filename)
            else -> throw IllegalArgumentException("Unknown variation $version")
        }
    println("Result is $result")
}

fun runProblem1(filename: String): String {
    return run(filename) { quantity, from, to -> SingleCrateCraneCommand(quantity, from, to) }
}

fun runProblem2(filename: String): String {
    return run(filename) { quantity, from, to -> MultiCrateCraneCommand(quantity, from, to) }
}

fun run(filename: String, craneCommandFactory: CraneCommandFactory): String {
    val lines: List<String> = readResource(filename)
    val crateLines: List<String> = lines.takeWhile { it.isNotBlank() }.dropLast(1)
    val commandLines: List<String> = lines.drop(crateLines.size + 2)

    val stacks: List<Stack<Char>> = createStacks(crateLines)
    val craneCommands: List<CraneCommand> = commandLines.map { parseCraneCommand(it, craneCommandFactory) }

    craneCommands.forEach { it.run(stacks) }

    return stacks.joinToString(separator = "") { if (it.isEmpty()) "-" else it.peek().toString() }
}

fun parseCrateLine(line: String): List<Char> {
    return line
        .windowed(3, 4)
        .map { it[1] }
}

fun parseCraneCommand(line: String, craneCommandFactory: CraneCommandFactory): CraneCommand {
    val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
    val matchResult =
        regex.matchEntire(line) ?: throw IllegalArgumentException("Line $line is not a valid crane command")
    val (quantity, from, to) = matchResult.groupValues.drop(1).map { it.toInt() }
    return craneCommandFactory(quantity, from - 1, to - 1)
}

fun createStacks(crateLines: Collection<String>): List<Stack<Char>> {
    val parsedCrateRow: List<List<Char>> = crateLines.map { parseCrateLine(it) }
    val nStacks: Int = parsedCrateRow.maxOf { it.size }
    return parsedCrateRow.foldRight(List(nStacks) { Stack<Char>() }) { crateLine, acc ->
        crateLine.forEachIndexed { idx, crateName ->
            if (crateName == ' ') return@forEachIndexed
            acc[idx].push(crateName)
        }
        acc
    }
}
