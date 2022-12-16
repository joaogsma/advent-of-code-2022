package org.joaogsma

import kotlin.math.abs

fun readResource(name: String): List<String> {
    when (val inputStream = object {}::class.java.getResourceAsStream(name)) {
        null -> throw IllegalArgumentException("File not found")
        else -> return inputStream.bufferedReader().readLines()
    }
}

fun draw(sensors: List<Sensor>) {
    val coveredAreas: List<L1Sphere> = sensors.map(Sensor::toDiamond)
    var header = "    "
    for (x in 0 .. 20) {
        header += " ${if (x < 10) "0" else ""}$x"
    }
    println(header)
    println(header.map { _ -> '-' }.joinToString(""))
    for (y in 0 downTo -20) {
        print("${if (abs(y) < 10) "-0" else "-"}${abs(y)}|")
        for (x in 0 .. 20) {
            print(' ')
            val current = Point(x, y)
            if (sensors.map(Sensor::position).contains(current)) {
                print(" S")
                continue
            }
            if (sensors.map(Sensor::nearestBeacon).contains(current)) {
                print(" B")
                continue
            }
            val count = coveredAreas.count { it.contains(current) }
            val value = (if (count < 10) " " else "") + "$count"
            print(value)
        }
        println()
    }
}
