package org.joaogsma

fun readResource(name: String): List<String> {
    when (val inputStream = FoodCounter::class.java.getResourceAsStream(name)) {
        null -> throw IllegalArgumentException("File not found")
        else -> return inputStream.bufferedReader().readLines()
    }
}

fun getFilename(input: String): String {
    return when(input) {
        "example" -> "example.txt"
        "input" -> "input.txt"
        else -> throw IllegalArgumentException("Unknown input $input")
    }
}

fun run(filename: String, window: Int): Int {
    val lines: List<String> = readResource(filename)
    val foodCounter = FoodCounter()
    val currentInventory: MutableSet<Int> = mutableSetOf()
    for (line in lines) {
        if (line.isNotBlank()) {
            currentInventory.add(line.toInt())
            continue
        }
        foodCounter.addInventory(currentInventory.toSet())
        currentInventory.clear()
    }
    if (currentInventory.isNotEmpty())
        foodCounter.addInventory(currentInventory.toSet())
    return foodCounter.findCaloricSumOfTopInventories(window)
}

fun main(args: Array<String>) {
    val filename: String = getFilename(args[0])
    val window: Int = args[1].toInt()
    val result: Int = run(filename, window)
    println("Result is $result")
}
