package org.joaogsma

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

fun parseOpponentShape(char: Char): Shape {
    return when(char) {
        'A' -> Shape.ROCK
        'B' -> Shape.PAPER
        'C' -> Shape.SCISSORS
        else -> throw IllegalArgumentException("Unknown shape $char")
    }
}

fun parseOwnShape(char: Char, opponent: Shape): Shape {
    return when(char) {
        'X' -> findWhatLosesAgainst(opponent)
        'Y' -> opponent
        'Z' -> findWhatWinsAgainst(opponent)
        else -> throw IllegalArgumentException("Unknown shape $char")
    }
}

fun computeScore(opponent: Shape, own: Shape): Int =
    own.points + if (own.beats(opponent)) 6 else if (opponent.beats(own)) 0 else 3

fun run(filename: String): Int {
    val lines: List<String> = readResource(filename)
    return lines.sumOf { line ->
        val opponent: Shape = parseOpponentShape(line[0])
        val own: Shape = parseOwnShape(line[2], opponent)
        computeScore(opponent, own)
    }
}

fun main(args: Array<String>) {
    val filename: String = getFilename(args[0])
    val result: Int = run(filename)
    println("Result is $result")
}
