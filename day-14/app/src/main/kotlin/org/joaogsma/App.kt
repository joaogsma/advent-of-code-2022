package org.joaogsma

import kotlin.math.sign

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
    val rockCoordinates: Set<LineSegment> = lines.flatMap { parse(it) }.toSet()
    val rockBottom: Int = rockCoordinates.flatMap { ls -> listOf(ls.pointA, ls.pointB) }.minOf(Point::y)
    return simulate(Point(500, 0), rockCoordinates) { (_, y) -> y < rockBottom }.size
}

fun runProblem2(filename: String): Int {
    val lines: List<String> = readResource(filename)
    val sandOrigin = Point(500, 0)
    val rockCoordinates: Set<LineSegment> = lines.flatMap { parse(it) }.toSet()
    val rockBottom: Int = rockCoordinates.flatMap { ls -> listOf(ls.pointA, ls.pointB) }.minOf(Point::y)
    val floor = LineSegment(Point(Int.MIN_VALUE / 2, rockBottom - 2), Point(Int.MAX_VALUE / 2, rockBottom - 2))
    return simulate(sandOrigin, rockCoordinates + floor) { _ -> false }.size
}

fun simulate(
    origin: Point,
    rockObstacles: Set<LineSegment>,
    stopCondition: (current: Point) -> Boolean
): Set<Point> {
    return simulate(origin, setOf(), rockObstacles, stopCondition)
}

tailrec fun simulate(
    origin: Point,
    sandObstacles: Set<Point>,
    rockObstacles: Set<LineSegment>,
    stopCondition: (current: Point) -> Boolean
): Set<Point> {
    val newSandCoords: Point = moveSand(origin, sandObstacles, rockObstacles, stopCondition) ?: return sandObstacles
    return simulate(origin, sandObstacles + newSandCoords, rockObstacles, stopCondition)
}

tailrec fun moveSand(
    origin: Point,
    sandObstacles: Set<Point>,
    rockObstacles: Set<LineSegment>,
    stopCondition: (current: Point) -> Boolean
): Point? {
    val directions: List<Vector> =
        listOf(Vector.down(), Vector.down() + Vector.left(), Vector.down() + Vector.right(), Vector(0, 0))
    val newPoint: Point? =
        directions
            .map { origin + it }
            .find { p -> !sandObstacles.contains(p) && rockObstacles.none { ls -> ls.contains(p) } }
    if (newPoint == null || stopCondition(origin))
        return null
    if (newPoint == origin)
        return origin
    return moveSand(newPoint, sandObstacles, rockObstacles, stopCondition)
}

fun parse(line: String): Set<LineSegment> {
    val vertices: List<Point> =
        line
            .split(" -> ")
            .map { it.split(",").map(String::toInt) }
            .map { (x, y) -> Point(x, -1 * y) }
    return vertices
        .windowed(size=2, step=1)
        .map { (begin, end) -> LineSegment(begin, end) }
        .toSet()
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(vector: Vector): Point = Point(x + vector.x, y + vector.y)
    operator fun minus(vector: Vector): Point = plus(-vector)
    operator fun minus(other: Point): Vector = Vector(x - other.x, y - other.y)

    fun intersects(other: LineSegment): Boolean {
        if (this == other.pointA || this == other.pointB)
            return true
        val vectorToA = (other.pointA - this).normalize()
        val vectorToB = (other.pointB - this).normalize()
        val isOnLine = vectorToA == other.vector || vectorToA == other.vector * -1
        if (isOnLine && vectorToA != vectorToB)
            return true
        return false
    }
}

data class Vector(val x: Int, val y: Int) {
    operator fun unaryPlus(): Vector = this
    operator fun unaryMinus(): Vector = Vector(-x, -y)
    operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y)
    operator fun minus(other: Vector): Vector = plus(-other)
    operator fun times(n: Int): Vector = Vector(x * n, y * n)
    operator fun div(n: Int): Vector = Vector(x / n, y / n)

    fun normalize(): Vector {
        if (x == 0 && y == 0)
            throw RuntimeException("Cannot normalize a zero-magnitude vector")
        return Vector(sign(x.toDouble()).toInt(), sign(y.toDouble()).toInt())
    }

    companion object {
        fun right(magnitude: Int = 1): Vector = Vector(magnitude, 0)
        fun left(magnitude: Int = 1): Vector = -right(magnitude)
        fun up(magnitude: Int = 1): Vector = Vector(0, magnitude)
        fun down(magnitude: Int = 1): Vector = -up(magnitude)
    }
}

data class LineSegment(val pointA: Point, val pointB: Point) {
    val vector: Vector = (pointB - pointA).normalize()

    fun contains(p: Point) = p.intersects(this)
}

