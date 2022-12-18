package org.joaogsma

class SingleNavigator(
    valves: Map<Valve, List<Valve>>,
    start: Valve,
    totalMinutes: Int
): Navigator(valves, start, totalMinutes) {
    override fun findGreatestPressureRelease(): Int {
        val possibleReleaseSequences =
            permutations(valves.keys.filter { it.flowRate > 0 }, this::isOutOfTime)
        return possibleReleaseSequences.maxOf(this::simulateNavigationSequence)
    }
}
