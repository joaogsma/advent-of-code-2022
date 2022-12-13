package org.joaogsma

data class Coordinates(val row: Int, val col: Int)

fun Coordinates.neighbours(): List<Coordinates> =
    listOf(Coordinates(row - 1, col), Coordinates(row, col + 1), Coordinates(row + 1, col), Coordinates(row, col - 1))
