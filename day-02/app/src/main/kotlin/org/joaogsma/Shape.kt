package org.joaogsma

enum class Shape(val points: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);
}

fun findWhatWinsAgainst(shape: Shape): Shape {
    return when(shape) {
        Shape.ROCK -> Shape.PAPER
        Shape.PAPER -> Shape.SCISSORS
        Shape.SCISSORS -> Shape.ROCK
    }
}

fun findWhatLosesAgainst(shape: Shape): Shape = findWhatWinsAgainst(findWhatWinsAgainst(shape))

fun Shape.beats(other: Shape): Boolean {
    if (this == findWhatWinsAgainst(other)) return true
    return false
}
