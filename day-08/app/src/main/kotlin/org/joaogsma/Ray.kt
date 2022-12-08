package org.joaogsma

data class Ray(val origin: Coordinates, val step: (Coordinates) -> Coordinates) {
    fun increment(): Ray = copy(origin = step(origin))

    companion object {
        fun upFrom(origin: Coordinates): Ray = Ray(origin) { it.copy(row = it.row - 1) }
        fun downFrom(origin: Coordinates): Ray = Ray(origin) { it.copy(row = it.row + 1) }
        fun leftFrom(origin: Coordinates): Ray = Ray(origin) { it.copy(col = it.col - 1) }
        fun rightFrom(origin: Coordinates): Ray = Ray(origin) { it.copy(col = it.col + 1) }
    }
}


