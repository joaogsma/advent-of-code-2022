package org.joaogsma

class SignalStrengthMonitor(private val readRegisterX: () -> Int): ClockListener {
    private var cycle = 0
    private val signalStrengths: MutableList<Int> = mutableListOf()

    override fun runCycle() {
        cycle++
        if (cycle in 20..220 step 40)
            signalStrengths.add(cycle * readRegisterX())
    }

    fun getStrengthSum(): Int = signalStrengths.take(6).sum()
}
