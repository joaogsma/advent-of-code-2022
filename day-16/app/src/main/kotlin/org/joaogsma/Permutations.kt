package org.joaogsma

fun <T> permutations(elements: Collection<T>, earlyStop: (List<T>) -> Boolean = { false }): Set<List<T>> {
    if (elements.isEmpty())
        return emptySet()

    fun permutationsRec(previous: List<T>, remaining: List<T>): List<List<T>> {
        if (remaining.isEmpty())
            return listOf(emptyList())
        if (previous.isNotEmpty() && earlyStop(previous))
            return listOf(emptyList())
        return remaining.flatMap { e ->
            permutationsRec(previous + e, remaining - e).map { listOf(e) + it }
        }
    }

    return permutationsRec(emptyList(), elements.toList()).toSet()
}

fun <T> splitPermutations(elements: Collection<T>, earlyStop: (List<T>) -> Boolean = { false }): Set<Pair<List<T>, List<T>>> {
    if (elements.isEmpty())
        return emptySet()
    val result: MutableSet<Pair<List<T>, List<T>>> = mutableSetOf()

    fun splitPermutationsRec(previous: List<T>, remaining: List<T>) {
        if (remaining.size == 1)
            return
        if (previous.isNotEmpty() && earlyStop(previous)) {
            permutations(remaining, earlyStop)
                .map { Pair(previous, it) }
                .forEach(result::add)
            return
        }
        for (e in remaining) {
            val split = Pair(previous + e, remaining - e)
            result.add(split)
            splitPermutationsRec(split.first, split.second)
        }
    }

    splitPermutationsRec(emptyList(), elements.toList())
    return result.toSet()
}

