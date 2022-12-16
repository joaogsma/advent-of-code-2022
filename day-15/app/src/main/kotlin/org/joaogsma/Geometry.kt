package org.joaogsma

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    operator fun plus(vector: Vector): Point = Point(x + vector.x, y + vector.y)
    operator fun minus(vector: Vector): Point = plus(-vector)
    operator fun minus(other: Point): Vector = Vector(x - other.x, y - other.y)

    fun l1DistanceTo(other: Point): Int = abs(x - other.x) + abs(y - other.y)
}

data class Vector(val x: Int, val y: Int) {
    operator fun unaryPlus(): Vector = this
    operator fun unaryMinus(): Vector = Vector(-x, -y)
    operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y)
    operator fun minus(other: Vector): Vector = plus(-other)
    operator fun times(n: Int): Vector = Vector(x * n, y * n)
    operator fun div(n: Int): Vector = Vector(x / n, y / n)

    companion object {
        fun right(magnitude: Int = 1): Vector = Vector(magnitude, 0)
        fun left(magnitude: Int = 1): Vector = -right(magnitude)
        fun up(magnitude: Int = 1): Vector = Vector(0, magnitude)
        fun down(magnitude: Int = 1): Vector = -up(magnitude)
    }
}

data class L1Sphere(val center: Point, val radius: Int) {
    val diameter: Int = 2 * radius
    val top: Point = center + Vector.up(radius)
    val down: Point = center + Vector.down(radius)
    val left: Point = center + Vector.left(radius)
    val right: Point = center + Vector.right(radius)

    fun contains(p: Point): Boolean = center.l1DistanceTo(p) <= center.l1DistanceTo(right)
}
