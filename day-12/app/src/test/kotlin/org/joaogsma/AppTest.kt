package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInputVersion1() {
        assertEquals(31, runProblem1("example.txt"))
    }

    @Test fun testRealInputVersion1() {
        assertEquals(408, runProblem1("input.txt"))
    }

    @Test fun testExampleInputVersion2() {
        assertEquals(29, runProblem2("example.txt"))
    }

    @Test fun testRealInputVersion2() {
        assertEquals(399, runProblem2("input.txt"))
    }
}
