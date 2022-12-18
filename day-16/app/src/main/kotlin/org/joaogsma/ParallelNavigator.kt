package org.joaogsma

import kotlin.math.round

class ParallelNavigator(
    valves: Map<Valve, List<Valve>>,
    start: Valve,
    totalMinutes: Int
): Navigator(valves, start, totalMinutes) {
    override fun findGreatestPressureRelease(): Int {
        val valvesWithPositiveFlow = valves.keys.filter { it.flowRate > 0 }
        val bestSingleReleaseSeq =
            permutations(valvesWithPositiveFlow, this::isOutOfTime).maxByOrNull(this::simulateNavigationSequence)!!

        val possibleSplitSequences = splitPermutations(bestSingleReleaseSeq)
        var i = 0
        val result =
            possibleSplitSequences.maxOf { (seq1, seq2) ->
                if (++i % 100 == 0)
                    print("\r${(round(100 * i.toDouble() / possibleSplitSequences.size)).toInt()} %")
                val remainingValves1 = valvesWithPositiveFlow.toSet() - seq2.toSet()
                val remainingValves2 = valvesWithPositiveFlow.toSet() - seq1.toSet()

                val possibleNavigationSequences1 =
                    permutations(remainingValves1) { isOutOfTime(addMissingRequiredValves(seq1, it)) }
                val possibleNavigationSequences2 =
                    permutations(remainingValves2) { isOutOfTime(addMissingRequiredValves(seq2, it)) }

                val pressureReleased1: Int =
                    possibleNavigationSequences1.maxOf(this::simulateNavigationSequence)
                val pressureReleased2: Int =
                    possibleNavigationSequences2.maxOf(this::simulateNavigationSequence)
                (pressureReleased1 + pressureReleased2)
            }
        println("\r100 %")
        return result
    }

    private fun addMissingRequiredValves(required: Collection<Valve>, permutation: List<Valve>): List<Valve> {
        val bestRequiredValveOrder = mutableListOf(permutation.last())
        val remainingRequiredValves: MutableSet<Valve> = required.toMutableSet()
        remainingRequiredValves.removeAll(permutation.toSet())
        while (remainingRequiredValves.isNotEmpty()) {
            val e = remainingRequiredValves.minByOrNull { distances[bestRequiredValveOrder.last() to it]!! }!!
            bestRequiredValveOrder.add(e)
            remainingRequiredValves.remove(e)
        }
        return permutation + bestRequiredValveOrder.drop(1)
    }
}

