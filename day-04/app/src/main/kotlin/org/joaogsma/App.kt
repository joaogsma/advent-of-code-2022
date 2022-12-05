package org.joaogsma

fun main(args: Array<String>) {
    val filename: String = getFilename(args[0])
    val result: Int =
        when (val variation = args[1]) {
            "1" -> runProblem1(filename)
            "2" -> runProblem2(filename)
            else -> throw IllegalArgumentException("Unknown variation $variation")
        }
    println("Result is $result")
}

fun getFilename(input: String): String {
    return when(input) {
        "example" -> "example.txt"
        "input" -> "input.txt"
        else -> throw IllegalArgumentException("Unknown input $input")
    }
}

fun runProblem1(filename: String): Int =
    run(filename) { rangeA, rangeB -> rangeA.contains(rangeB) || rangeB.contains(rangeA) }

fun runProblem2(filename: String): Int =
    run(filename) { rangeA, rangeB -> rangeA.intersects(rangeB) }

fun run(filename: String, predicate: (IntRange, IntRange) -> Boolean): Int {
    val lines: List<String> = readResource(filename)
    return lines
        .map { readRange(it) }
        .count { (rangeA, rangeB) -> predicate(rangeA, rangeB) }
}

fun IntRange.contains(other: IntRange): Boolean = this.contains(other.first) && this.contains(other.last)
fun IntRange.intersects(other: IntRange): Boolean =
    this.contains(other.first) || this.contains(other.last) || other.contains(first) || other.contains(last)
