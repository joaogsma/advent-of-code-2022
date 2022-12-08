package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInputVersion1() {
        assertEquals(21, runProblem1("example.txt"))
    }

    @Test fun testRealInputVersion1() {
        assertEquals(1829, runProblem1("input.txt"))
    }

    @Test fun testExampleInputVersion2() {
        assertEquals(8, runProblem2("example.txt"))
    }

    @Test fun testRealInputVersion2() {
        assertEquals(291840, runProblem2("input.txt"))
    }
}
