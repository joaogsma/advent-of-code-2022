package org.joaogsma

fun main(args: Array<String>) {
    val (filename, version) = args
    val result: Int =
        when (version) {
            "1" -> runProblem1(filename)
            "2" -> runProblem2(filename)
            else -> throw IllegalArgumentException("Unknown version $version")
        }
    println("Result is $result")
}

fun runProblem1(filename: String): Int {
    val lines: List<String> = readResource(filename)
    val valves: Map<Valve, List<Valve>> = parse(lines)
    val start: Valve = valves.keys.find { it.name == "AA" }!!
    val totalMinutes = 30
    return SingleNavigator(valves, start, totalMinutes).findGreatestPressureRelease()
}

fun runProblem2(filename: String): Int {
    val lines: List<String> = readResource(filename)
    val valves: Map<Valve, List<Valve>> = parse(lines)
    val start: Valve = valves.keys.find { it.name == "AA" }!!
    val totalMinutes = 26
    return ParallelNavigator(valves, start, totalMinutes).findGreatestPressureRelease()
}

fun parse(lines: List<String>): Map<Valve, List<Valve>> {
    val valves: MutableMap<Valve, List<String>> = mutableMapOf()
    val regex =
        "Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? ((?:\\w+, )*\\w+)".toRegex()

    for (line in lines) {
        val matchResult =
            regex.matchEntire(line) ?: throw IllegalArgumentException("Could not parse input $line")
        val name = matchResult.groupValues[1]
        val flowRate = matchResult.groupValues[2].toInt()
        val nextValves = matchResult.groupValues[3].split(", ")
        valves[Valve(name, flowRate)] = nextValves
    }

    return valves.mapValues { (_, nextValves) ->
        nextValves.map { nextValveName -> valves.keys.find { it.name == nextValveName }!! }
    }
}

