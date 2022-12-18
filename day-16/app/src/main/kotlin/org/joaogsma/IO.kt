package org.joaogsma

import kotlin.math.abs

fun readResource(name: String): List<String> {
    when (val inputStream = object {}::class.java.getResourceAsStream(name)) {
        null -> throw IllegalArgumentException("File not found")
        else -> return inputStream.bufferedReader().readLines()
    }
}
