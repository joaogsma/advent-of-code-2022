package org.joaogsma

fun main(args: Array<String>) {
    val result: Long =
        when (val version = args[1]) {
            "1" -> runProblem1(args[0])
            "2" -> runProblem2(args[0])
            else -> throw IllegalArgumentException("Unknown version $version")
        }
    println("Sum of signal strengths was $result")
}

fun runProblem1(filename: String): Long {
    val lines: List<String> = readResource(filename)
    val monkeys: MutableList<Monkey> = mutableListOf()
    parseMonkeys(lines.iterator(), monkeys, mutableSetOf()) { worry -> worry / 3L }
    runGame(20, monkeys)
    return monkeys.map { it.itemsInspected }.sortedDescending().take(2).reduce(Long::times)
}

fun runProblem2(filename: String): Long {
    val lines: List<String> = readResource(filename)
    val monkeys: MutableList<Monkey> = mutableListOf()
    val testTargets: MutableSet<Long> = mutableSetOf()
    parseMonkeys(lines.iterator(), monkeys, testTargets) { worry -> worry % testTargets.reduce(Long::times) }
    runGame(10000, monkeys)
    return monkeys.map { it.itemsInspected }.sortedDescending().take(2).reduce(Long::times)
}

fun parseMonkeys(
    lines: Iterator<String>,
    destMonkey: MutableList<Monkey>,
    destTestTargets: MutableCollection<Long>,
    controlWorryLevels: (Long) -> Long
) {
    if (!lines.hasNext())
        return
    lines.next() // "Monkey X" line
    val startingItems: List<Long> = parseStartingItems(lines)
    val operation: (Long) -> Long = parseOperation(lines)
    val test: (Long) -> Monkey = parseNextMonkeyTest(lines, destMonkey::get, destTestTargets)
    destMonkey.add(Monkey(operation, test, controlWorryLevels))
    startingItems.forEach(destMonkey.last()::receiveItem)
    if (lines.hasNext())
        lines.next()  // Delimiter blank line
    parseMonkeys(lines, destMonkey, destTestTargets, controlWorryLevels)
}

fun parseStartingItems(lines: Iterator<String>): List<Long> {
    return lines.next()
        .trim()
        .removePrefix("Starting items: ")
        .split(", ")
        .map(String::toLong)
}

fun parseNextMonkeyTest(
    lines: Iterator<String>,
    getMonkeyByIndex: (Int) -> Monkey,
    testValueAcc: MutableCollection<Long>
): (Long) -> Monkey {
    val testValue: Long = lines.next().trim().removePrefix("Test: divisible by ").toLong()
    testValueAcc.add(testValue)
    val targetOnSuccess: Int = lines.next().trim().removePrefix("If true: throw to monkey ").toInt()
    val targetOnFailure: Int = lines.next().trim().removePrefix("If false: throw to monkey ").toInt()
    val getNextMonkeyIndex: (Long) -> Int =
        { worry -> if (worry % testValue == 0L) targetOnSuccess else targetOnFailure }
    return { worry -> getMonkeyByIndex(getNextMonkeyIndex(worry)) }
}

fun parseOperation(lines: Iterator<String>): (Long) -> Long {
    val operationLine: String = lines.next().trim().removePrefix("Operation: new = ")
    if ("*" in operationLine)
        return parseOperation(operationLine.split(" * "), Long::times)
    if ("+" in operationLine)
        return parseOperation(operationLine.split(" + "), Long::plus)
    throw IllegalArgumentException("Unrecognized operation on expression: $operationLine")
}

fun parseOperation(elements: List<String>, operation: (Long, Long) -> Long): (Long) -> Long {
    return elements
        .map { e ->
            when (e) {
                "old" -> { worry: Long -> worry }
                else -> { _ -> e.toLong() }
            }
        }
        .reduce { a, b -> { worry -> operation(a(worry), b(worry)) } }
}

tailrec fun runGame(rounds: Int, monkeys: List<Monkey>) {
    if (rounds == 0)
        return
    for (monkey in monkeys)
        while (monkey.hasItems())
            monkey.handleItem()
    runGame(rounds - 1, monkeys)
}

class Monkey(
    private val operation: (Long) -> Long,
    private val test: (Long) -> Monkey,
    private val controlWorryLevels: (Long) -> Long
) {
    private val items: MutableList<Long> = mutableListOf()
    var itemsInspected: Long = 0L
        private set

    fun receiveItem(worry: Long) { items.add(worry) }

    fun hasItems(): Boolean = items.isNotEmpty()

    fun handleItem() {
        itemsInspected++
        val newWorry: Long = inspectItem(items.removeFirst())
        test(newWorry).receiveItem(newWorry)
    }

    private fun inspectItem(worry: Long): Long = controlWorryLevels(operation(worry))
}
