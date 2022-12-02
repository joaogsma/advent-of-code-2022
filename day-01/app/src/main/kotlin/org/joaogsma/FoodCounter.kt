package org.joaogsma

class FoodCounter {
    private val inventories: MutableList<Set<Int>> = mutableListOf()

    fun addInventory(value: Set<Int>) = inventories.add(value)

    fun findCaloricSumOfTopInventories(window: Int): Int {
        return inventories
            .map { it.sum() }
            .sortedDescending()
            .take(window)
            .sum()
    }

}
