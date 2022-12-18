package org.joaogsma

abstract class Navigator(
    protected val valves: Map<Valve, List<Valve>>,
    protected val start: Valve,
    protected val totalMinutes: Int
) {
    protected val distances = findDistances(valves)

    abstract fun findGreatestPressureRelease(): Int

    private fun findDistances(valves: Map<Valve, List<Valve>>): Map<Pair<Valve, Valve>, Int> {
        return valves
            .flatMap { (valve, _) ->
                val distances: Map<Valve, Int> = findDistances(valve, valves)
                distances.map { (target, distance) -> (valve to target) to distance }
            }
            .toMap()
    }

    private fun findDistances(origin: Valve, valves: Map<Valve, List<Valve>>): Map<Valve, Int> {
        val shortestPaths: MutableMap<Valve, Int> = mutableMapOf(origin to 0)
        valves[origin]!!.forEach { shortestPaths[it] = 1 }
        val visitedNodes: MutableSet<Valve> = mutableSetOf(origin)
        val queue: MutableList<Valve> = valves[origin]!!.toMutableList()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (visitedNodes.contains(current))
                continue
            visitedNodes.add(current)
            val neighbours: List<Valve> = valves[current]!!.filterNot(visitedNodes::contains)
            for (neighbour in neighbours) {
                val tentativeDistance = shortestPaths[current]!! + 1
                val currentDistance = shortestPaths[neighbour] ?: Int.MAX_VALUE
                if (tentativeDistance < currentDistance)
                    shortestPaths[neighbour] = tentativeDistance
                queue.add(neighbour)
            }
        }
        return shortestPaths.toMap()
    }

    protected fun isOutOfTime(order: Collection<Valve>): Boolean {
        val timeMoving =
            (listOf(start) + order).windowed(2).sumOf { (a, b) -> distances[a to b]!! }
        val timeToReleaseValves = order.size
        return timeMoving + timeToReleaseValves >= totalMinutes
    }

    protected fun simulateNavigationSequence(queue: List<Valve>): Int  = simulateNavigationSequence(start, queue, totalMinutes)

    private fun simulateNavigationSequence(current: Valve, queue: List<Valve>, remainingMinutes: Int): Int {
        if (remainingMinutes <= 0)
            return 0
        val pressureContribution = remainingMinutes * current.flowRate
        if (queue.isEmpty())
            return pressureContribution
        val remainingMinWhenNextIsOpen = remainingMinutes - distances[current to queue.first()]!! - 1
        return (pressureContribution
                + simulateNavigationSequence(queue.first(), queue.drop(1), remainingMinWhenNextIsOpen))
    }
}
