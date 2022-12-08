package org.joaogsma

class Grid<T> private constructor(private val data: List<T>, val rows: Int, val cols: Int) {
    fun get(row: Int, col: Int): T {
        validateCoordinates(row, col)
        return data[row * cols + col]
    }

    fun get(value: Coordinates): T = get(value.row, value.col)

    private fun validateCoordinates(row: Int, col: Int) {
        if (row !in 0 until rows || col !in 0 until cols)
            throw IllegalArgumentException("Invalid coordinates ($row, $col) for grid of size ${rows}x$cols")
    }

    companion object {
        fun <T> builder(): Builder<T> = Builder()

        class Builder<T> {
            private val data: MutableList<T> = mutableListOf()
            private var maxRow: Int = 0
            private var maxCol: Int = 0

            fun addRow(row: Collection<T>): Builder<T> {
                if (row.isEmpty())
                    throw IllegalArgumentException("Empty row")
                if (maxRow > 0 && row.size != maxCol)
                    throw IllegalArgumentException("Cannot add row of size ${row.size} to a grid with $maxCol columns")
                data.addAll(row)
                if (maxCol == 0)
                    maxCol = row.size
                maxRow++
                return this
            }

            fun build(): Grid<T> = Grid(data.toList(), maxRow, maxCol)
        }
    }
}

