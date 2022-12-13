package org.joaogsma

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
    val navigator = DijkstraNavigator(parseHeightMap(lines))
    return navigator.findDistance(findOrigin(lines), findDestination(lines))
        ?: throw IllegalArgumentException("Could not find destination")
}

fun runProblem2(filename: String): Int {
    val lines: List<String> = readResource(filename)
    val heightMap: HeightMap = parseHeightMap(lines)
    val navigator = DijkstraNavigator(heightMap)
    val destination: Coordinates = findDestination(lines)
    return heightMap.findAllHeightZeroCoordinates().minOf { navigator.findDistance(it, destination) ?: Int.MAX_VALUE }
}

fun findOrigin(lines: List<String>): Coordinates = find(lines, 'S')
fun findDestination(lines: List<String>): Coordinates = find(lines, 'E')

fun find(lines: List<String>, target: Char): Coordinates {
    lines.forEachIndexed { row, line ->
        line.forEachIndexed { col, char -> if (char == target) return Coordinates(row, col) } }
    throw IllegalArgumentException("Could not find origin")
}

fun parseHeightMap(lines: List<String>): HeightMap {
    val builder: HeightMap.Builder =
        lines
            .map { line ->
                line.map { char ->
                    when (char) {
                        'S' -> 0
                        'E' -> 25
                        else -> char - 'a'
                    }
                }
            }
            .fold(HeightMap.builder()) { builder, row -> builder.addRow(row) }
    return builder.build()
}

class DijkstraNavigator(private val heightMap: HeightMap) {
    fun findDistance(origin: Coordinates, destination: Coordinates): Int? {
        val shortestPaths: MutableMap<Coordinates, NavigationEntry> = mutableMapOf(origin to NavigationEntry(0, null))
        heightMap.reachableFrom(origin).forEach { shortestPaths[it] = NavigationEntry(1, origin) }
        val visitedNodes: MutableSet<Coordinates> = mutableSetOf(origin)
        val queue: MutableList<Coordinates> = heightMap.reachableFrom(origin).toMutableList()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (visitedNodes.contains(current))
                continue
            visitedNodes.add(current)
            if (current == destination)
                return shortestPaths[current]?.distance!!
            val neighbours: List<Coordinates> = heightMap.reachableFrom(current).filterNot(visitedNodes::contains)
            for (neighbour in neighbours) {
                val tentativeDistance = shortestPaths[current]?.distance!! + 1
                val currentDistance = shortestPaths[neighbour]?.distance ?: Int.MAX_VALUE
                if (tentativeDistance < currentDistance)
                    shortestPaths[neighbour] = NavigationEntry(tentativeDistance, current)
                queue.add(neighbour)
            }
        }
        return null
    }

    data class NavigationEntry(val distance: Int, val previous: Coordinates?)
}
