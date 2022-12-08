package org.joaogsma

fun main(args: Array<String>) {
    val result: Int =
        when (val version = args[1]) {
            "1" -> runProblem1(args[0])
            "2" -> runProblem2(args[0])
            else -> throw IllegalArgumentException("Unknown variation $version")
        }
    println("Result is $result")
}

fun runProblem1(filename: String): Int {
    val lines: List<String> = readResource(filename)
    val trees: Grid<Int> =
        lines
            .fold(Grid.builder<Int>()) { builder, line -> builder.addRow(line.map { it.toString().toInt() }) }
            .build()
    val rays: List<Ray> =
        listOf(
            (0 until trees.cols).map { Coordinates(0, it) }.map(Ray::downFrom),
            (0 until trees.cols).map { Coordinates(trees.rows - 1, it) }.map(Ray::upFrom),
            (0 until trees.rows).map { Coordinates(it, 0) }.map(Ray::rightFrom),
            (0 until trees.rows).map { Coordinates(it, trees.cols - 1) }.map(Ray::leftFrom))
            .flatten()
    val visibleTrees: Set<Coordinates> = rays.map { ray -> rayTraceFindTreesTallerThan(trees, -1, ray) }.reduce { a, b -> a + b }
    return visibleTrees.size
}

fun runProblem2(filename: String): Int {
    val lines: List<String> = readResource(filename)
    val trees: Grid<Int> =
        lines
            .fold(Grid.builder<Int>()) { builder, line -> builder.addRow(line.map { it.toString().toInt() }) }
            .build()
    val coordinates: List<Coordinates> =
        (0 until trees.rows).flatMap { row -> (0 until trees.cols).map { col -> Coordinates(row, col) } }

    return coordinates.maxOfOrNull { origin ->
        listOf(Ray.upFrom(origin), Ray.downFrom(origin), Ray.leftFrom(origin), Ray.rightFrom(origin))
            .map(Ray::increment)
            .map { rayTraceFindTreesWithHeightUpTo(trees, trees.get(origin), it).size }
            .fold(1) { acc, count -> acc * count }
    }!!
}
