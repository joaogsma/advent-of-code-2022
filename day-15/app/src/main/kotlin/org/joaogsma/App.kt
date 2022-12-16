package org.joaogsma

import kotlin.math.abs

fun main(args: Array<String>) {
    val (filename, version) = args
    val result: Long =
        when (version) {
            "1" -> runProblem1(filename, args[2].toInt())
            "2" -> runProblem2(filename, args[2].toInt())
            else -> throw IllegalArgumentException("Unknown version $version")
        }
    println("Result is $result")
}

fun runProblem1(filename: String, targetLine: Int): Long {
    val lines: List<String> = readResource(filename)
    val sensors: List<Sensor> = lines.map { parse(it) }
    val coveredAreas: List<L1Sphere> = sensors.map(Sensor::toDiamond)
    val minX: Int = sensors.minOf { it.toDiamond().left.x }
    val maxX: Int = sensors.maxOf { it.toDiamond().right.x }
    return (minX..maxX)
        .map { Point(it, -1 * targetLine) }
        .filter { current -> sensors.none { it.nearestBeacon == current } }
        .count { current -> coveredAreas.any { it.contains(current) } }
        .toLong()
}

fun runProblem2(filename: String, maxAbsCoordinate: Int): Long {
    val lines: List<String> = readResource(filename)
    val sensors: List<Sensor> = lines.map { parse(it) }
    val coveredAreas: List<L1Sphere> = sensors.map(Sensor::toDiamond)
    val beacon = findUncoveredPoint(Point(0, 0), coveredAreas, maxAbsCoordinate)
    return beacon.x * 4000000L - beacon.y
}

fun parse(line: String): Sensor {
    val (position, nearestBeacon) =
        line
            .removePrefix("Sensor at ")
            .split(": closest beacon is at ")
            .map { pointStr ->
                val (x, y) = pointStr.split(", ").map { it.drop(2).toInt() }
                Point(x, -1 * y)
            }
    return Sensor(position, nearestBeacon)
}

tailrec fun findUncoveredPoint(current: Point, coveredAreas: Collection<L1Sphere>, maxAbsCoordinate: Int): Point {
    if (current.y < -maxAbsCoordinate)
        throw RuntimeException("All points are covered")
    if (current.x > maxAbsCoordinate)
        return findUncoveredPoint(Point(0, current.y - 1), coveredAreas, maxAbsCoordinate)
    val intersectingArea: L1Sphere = coveredAreas.find { it.contains(current) } ?: return current
    val next: Point = crossLeftToRight(current, intersectingArea)
    return findUncoveredPoint(next, coveredAreas, maxAbsCoordinate)
}

fun crossLeftToRight(p: Point, d: L1Sphere): Point {
    val isOnEdge = p.l1DistanceTo(d.center) == d.radius
    if (isOnEdge && p.x >= d.center.x)
        return p + Vector.right()
    if (isOnEdge) {
        val offset = d.diameter - 2 * abs(p.y - d.center.y) + 1
        return p + Vector.right(offset)
    }
    val offset: Int = d.radius - p.l1DistanceTo(d.center)
    val pointOnEdge = p + if (d.center.x > p.x) Vector.left(offset) else Vector.right(offset)
    return crossLeftToRight(pointOnEdge, d)
}
