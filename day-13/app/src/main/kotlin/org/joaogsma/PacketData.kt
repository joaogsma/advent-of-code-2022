package org.joaogsma

sealed class PacketData: Comparable<PacketData> {
    abstract override operator fun compareTo(other: PacketData): Int
}

data class NumberPacketData(val value: Int): PacketData() {
    override operator fun compareTo(other: PacketData): Int {
        return when (other) {
            is NumberPacketData -> value.compareTo(other.value)
            is ListPacketData -> ListPacketData.of(this).compareTo(other)
        }
    }

    override fun toString(): String = value.toString()
}

data class ListPacketData(val value: List<PacketData>): PacketData() {
    override operator fun compareTo(other: PacketData): Int {
        return when (other) {
            is NumberPacketData -> compareTo(of(other))
            is ListPacketData -> {
                for ((left, right) in value.zip(other.value)) {
                    val comparison = left.compareTo(right)
                    if (comparison != 0)
                        return comparison
                }
                if (value.size < other.value.size) -1 else if (value.size > other.value.size) 1 else 0
            }
        }
    }

    override fun toString(): String = "[${value.joinToString(separator = ",")}]"

    companion object {
        fun of(vararg values: PacketData): ListPacketData = ListPacketData(values.toList())
    }
}

