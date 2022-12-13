package org.joaogsma

class HeightMap private constructor(data: List<Int>, rows: Int, cols: Int): Grid<Int>(data, rows, cols) {
    fun reachableFrom(origin: Coordinates): List<Coordinates> {
        return origin.neighbours()
            .filter { (row, col) -> row in 0 until rows && col in 0 until cols }
            .filter { get(it) <= get(origin) + 1 }
    }

    fun findAllHeightZeroCoordinates(): List<Coordinates> {
        return data.indices
            .filter { data[it] == 0 }
            .map { index -> Coordinates(index / cols, index % cols) }
    }

    class Builder: Grid.Builder<Int>() {
        override fun addRow(row: Collection<Int>): Builder {
            super.addRow(row)
            return this
        }

        override fun build(): HeightMap = HeightMap(data.toList(), maxRow, maxCol)
    }

    companion object {
        fun builder(): Builder = Builder()
    }
}

open class Grid<T> protected constructor(protected val data: List<T>, val rows: Int, val cols: Int) {
    fun get(row: Int, col: Int): T {
        validateCoordinates(row, col)
        return data[row * cols + col]
    }

    fun get(value: Coordinates): T = get(value.row, value.col)

    private fun validateCoordinates(row: Int, col: Int) {
        if (row !in 0 until rows || col !in 0 until cols)
            throw IllegalArgumentException("Invalid coordinates ($row, $col) for grid of size ${rows}x$cols")
    }

    open class Builder<T> {
        protected val data: MutableList<T> = mutableListOf()
        protected var maxRow: Int = 0
        protected var maxCol: Int = 0

        open fun addRow(row: Collection<T>): Builder<T> {
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

        open fun build(): Grid<T> = Grid(data.toList(), maxRow, maxCol)
    }

    companion object {
        fun <T> builder(): Builder<T> = Builder()
    }
}
