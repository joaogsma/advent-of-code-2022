package org.joaogsma

import kotlin.math.abs

class CRT(
    private val rows: Int,
    private val cols: Int,
    private val readRegisterX: () -> Int,
    private val write: (String) -> Unit
): ClockListener {
    private var currentRow: Int = 0
    private var currentCol: Int = 0

    override fun runCycle() {
        val pixelValue: String = if (abs(readRegisterX() - currentCol) < 2) "#" else "."
        write(if (currentCol == cols - 1) pixelValue + "\n" else pixelValue)
        updatePixelIndex()
    }

    private fun updatePixelIndex() {
        currentCol = (currentCol + 1) % cols
        if (currentCol == 0)
            currentRow = (currentRow + 1) % rows
    }
}
