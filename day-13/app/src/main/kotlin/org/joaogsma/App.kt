package org.joaogsma

import java.util.*

fun main(args: Array<String>) {
    val result: Int =
        when (val version = args[1]) {
            "1" -> runProblem1(args[0])
            "2" -> runProblem2(args[0])
            else -> throw IllegalArgumentException("Unknown version $version")
        }
    println("Result is $result")
}

fun runProblem1(filename: String): Int {
    val lines: List<String> = readResource(filename)
    return lines
        .windowed(size = 2, step = 3)
        .mapIndexed { index, (left, right) -> if (parse(left) < parse(right)) index + 1 else 0 }
        .sum()
}

fun runProblem2(filename: String): Int {
    val lines: List<String> = readResource(filename)
    val dividerPackets =
        listOf(
            ListPacketData.of(ListPacketData.of(NumberPacketData(2))),
            ListPacketData.of(ListPacketData.of(NumberPacketData(6))))
    val sortedPackets = (dividerPackets + lines.filter(String::isNotEmpty).map { parse(it) }).sorted()
    return sortedPackets.indices
        .filter { sortedPackets[it] in dividerPackets }
        .map { it + 1 }
        .reduce(Int::times)
}

fun parse(line: String): PacketData {
    val listStack: Stack<MutableList<PacketData>> = Stack()
    var currentNumber = ""

    for (char in line) {
        if (char in '0'..'9') {
            currentNumber += char
            continue
        }
        if (char == '[') {
            listStack.push(mutableListOf())
            continue
        }
        if (currentNumber.isNotEmpty()) {
            listStack.peek().add(NumberPacketData(currentNumber.toInt()))
            currentNumber = ""
        }
        if (char == ',')
            continue
        val list = ListPacketData(listStack.pop().toList())
        if (listStack.isEmpty())
            return list
        listStack.peek().add(list)
    }
    throw IllegalArgumentException("Could not parse line $line")
}
