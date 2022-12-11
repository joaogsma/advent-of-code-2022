package org.joaogsma

class Clock(private val listeners: List<ClockListener>) {
    fun tick() = listeners.forEach { it.runCycle() }
}
