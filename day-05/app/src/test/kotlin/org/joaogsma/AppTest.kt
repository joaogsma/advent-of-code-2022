package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInputVersion1() {
        assertEquals("CMZ", runProblem1("example.txt"))
    }

    @Test fun testRealInputVersion1() {
        assertEquals("RNZLFZSJH", runProblem1("input.txt"))
    }

    @Test fun testExampleInputVersion2() {
        assertEquals("MCD", runProblem2("example.txt"))
    }

    @Test fun testRealInputVersion2() {
        assertEquals("CNSFCGJSM", runProblem2("input.txt"))
    }
}
