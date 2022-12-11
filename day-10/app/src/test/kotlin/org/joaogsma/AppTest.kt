package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInput() {
        var consoleResult = ""
        val result = run("example.txt") { consoleResult += it }
        assertEquals(13140, result)
        assertEquals(
            "##..##..##..##..##..##..##..##..##..##..\n"
                    + "###...###...###...###...###...###...###.\n"
                    + "####....####....####....####....####....\n"
                    + "#####.....#####.....#####.....#####.....\n"
                    + "######......######......######......####\n"
                    + "#######.......#######.......#######.....\n",
            consoleResult
        )
    }

    @Test fun testRealInput() {
        var consoleResult = ""
        val result = run("input.txt") { consoleResult += it }
        assertEquals(16880, result)
        assertEquals(
            "###..#..#..##..####..##....##.###..###..\n"
                    + "#..#.#.#..#..#....#.#..#....#.#..#.#..#.\n"
                    + "#..#.##...#..#...#..#..#....#.###..#..#.\n"
                    + "###..#.#..####..#...####....#.#..#.###..\n"
                    + "#.#..#.#..#..#.#....#..#.#..#.#..#.#.#..\n"
                    + "#..#.#..#.#..#.####.#..#..##..###..#..#.\n",
            consoleResult)
    }
}
