package org.joaogsma

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

fun main(args: Array<String>) {
    val result: Int =
        when (val version = args[1]) {
            "1" -> runProblem1(args[0])
            "2" -> runProblem2(args[0])
            else -> throw IllegalArgumentException("Unknown variation $version")
        }
    println("Result is $result")
}

fun runProblem1(filename: String): Int  = run(filename, 2)

fun runProblem2(filename: String): Int  = run(filename, 10)

fun run(filename: String, ropeLength: Int): Int {
    val lines: List<String> = readResource(filename)
    val origin = Point(0, 0)
    val rope = Rope(origin, ropeLength)
    val tailPositions: MutableSet<Point> = mutableSetOf(origin)
    lines.map { parse(it) }.forEach { rope.moveHead(it) { tailPosition -> tailPositions.add(tailPosition) } }
    return tailPositions.size
}

fun parse(line: String): Vector {
    val (direction, distanceStr) = line.split(" ")
    val magnitude: Int = distanceStr.toInt()
    return when (direction) {
        "R" -> Vector.right(magnitude)
        "L" -> Vector.left(magnitude)
        "U" -> Vector.up(magnitude)
        "D" -> Vector.down(magnitude)
        else -> throw IllegalArgumentException("Could not parse direction $direction")
    }
}

class Rope(head: Point, length: Int) {
    init {
        if (length < 2)
            throw IllegalArgumentException("Rope length must be at least 2")
    }

    private val points: MutableList<Point> = MutableList(length) { head }

    fun moveHead(vector: Vector, tailMovementCallback: (Point) -> Unit) {
        if (!vector.isOrthogonalToAxis())
            throw IllegalArgumentException("Head cannot move diagonally")
//        println("Movement: $vector")
        vector.toOrthogonalNormalizedComponents().forEach { moveHeadStep(it, tailMovementCallback) }
//        println()
    }

    private fun moveHeadStep(vector: Vector, tailMovementCallback: (Point) -> Unit) {
        points[0] = points[0] + vector
        for (i in 1 until points.size) {
            val previous: Point = points[i - 1]
            val current: Point = points[i]
            if (previous.distanceLInf(current) <= 1)
                continue
            points[i] = current + (previous - current).normalize()
        }
        tailMovementCallback(points.last())
//        println("Head: $head    Tail: $tail")
    }
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(vector: Vector): Point = Point(x + vector.x, y + vector.y)
    operator fun minus(vector: Vector): Point = plus(-vector)
    operator fun minus(other: Point): Vector = Vector(x - other.x, y - other.y)

    fun distanceLInf(other: Point): Int = max(abs(x - other.x), abs(y - other.y))
}

data class Vector(val x: Int, val y: Int) {
    operator fun unaryPlus(): Vector = this
    operator fun unaryMinus(): Vector = Vector(-x, -y)
    operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y)
    operator fun minus(other: Vector): Vector = plus(-other)

    fun isOrthogonalToAxis(): Boolean = x == 0 || y == 0

    fun normalize(): Vector {
        if (x == 0 && y == 0)
            throw RuntimeException("Cannot normalize a zero-magnitude vector")
        return Vector(sign(x.toDouble()).toInt(), sign(y.toDouble()).toInt())
    }

    fun toOrthogonalNormalizedComponents(): List<Vector> {
        if (x > 0)
            return listOf(right()) + (this + left()).toOrthogonalNormalizedComponents()
        if (x < 0)
            return listOf(left()) + (this + right()).toOrthogonalNormalizedComponents()
        if (y > 0)
            return listOf(up()) + (this + down()).toOrthogonalNormalizedComponents()
        if (y < 0)
            return listOf(down()) + (this + up()).toOrthogonalNormalizedComponents()
        return listOf()
    }

    companion object {
        fun right(magnitude: Int = 1): Vector = Vector(magnitude, 0)
        fun left(magnitude: Int = 1): Vector = -right(magnitude)
        fun up(magnitude: Int = 1): Vector = Vector(0, magnitude)
        fun down(magnitude: Int = 1): Vector = -up(magnitude)
    }
}
