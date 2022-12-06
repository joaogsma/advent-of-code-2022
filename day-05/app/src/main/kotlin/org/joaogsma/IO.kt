package org.joaogsma

import java.util.*

fun readResource(name: String): List<String> {
    when (val inputStream = object {}::class.java.getResourceAsStream(name)) {
        null -> throw IllegalArgumentException("File not found")
        else -> return inputStream.bufferedReader().readLines()
    }
}

fun getFilename(input: String): String {
    return when(input) {
        "example" -> "example.txt"
        "input" -> "input.txt"
        else -> throw IllegalArgumentException("Unknown input $input")
    }
}

fun drawStacks(stacks: List<Stack<Char>>): String {
    val maxHeight: Int = stacks.maxOf { it.size }
    val paddedStacks: List<List<String>> =
        stacks.map { stack ->
            val crates = stack.toList().map { "[$it]" }
            val padding = List(maxHeight - stack.size) { "   " }
            crates + padding
        }
    val resultWithoutColNumbers =
        (maxHeight - 1 downTo 0).joinToString(separator = "\n") { row ->
            paddedStacks.joinToString(separator = " ") { it[row] }
        }
    return (resultWithoutColNumbers
            + "\n"
            + paddedStacks.indices
        .joinToString(separator = "   ", prefix = " ", postfix = " ") { it.toString() })
}

