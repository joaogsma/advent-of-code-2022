package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInputVersion1() {
        assertEquals(1651, runProblem1("example.txt"))
    }

    @Test fun testRealInputVersion1() {
        assertEquals(1474, runProblem1("input.txt"))
    }

    @Test fun testExampleInputVersion2() {
        assertEquals(1707, runProblem2("example.txt"))
    }

    @Test fun testRealInputVersion2() {
        assertEquals(2100, runProblem2("input.txt"))
    }
}
