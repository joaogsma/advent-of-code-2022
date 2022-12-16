package org.joaogsma

data class Sensor(val position: Point, val nearestBeacon: Point) {
    fun toDiamond(): L1Sphere = L1Sphere(position, position.l1DistanceTo(nearestBeacon))
}

