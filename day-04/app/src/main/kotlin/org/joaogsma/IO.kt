package org.joaogsma

fun readResource(name: String): List<String> {
    when (val inputStream = object {}::class.java.getResourceAsStream(name)) {
        null -> throw IllegalArgumentException("File not found")
        else -> return inputStream.bufferedReader().readLines()
    }
}

fun readRange(line: String): Pair<IntRange, IntRange> {
    val ranges: List<IntRange> =
        line
            .split(',')
            .map { rangeStr ->
                val (begin: Int, end: Int) = rangeStr.split('-').map(String::toInt)
                begin..end
            }
    return Pair(ranges[0], ranges[1])
}
