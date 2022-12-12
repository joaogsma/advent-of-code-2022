package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInputVersion1() {
        assertEquals(10605L, runProblem1("example.txt"))
    }

    @Test fun testRealInputVersion1() {
        assertEquals(108240L, runProblem1("input.txt"))
    }

    @Test fun testExampleInputVersion2() {
        assertEquals(2713310158L, runProblem2("example.txt"))
    }

    @Test fun testRealInputVersion2() {
        assertEquals(25712998901L, runProblem2("input.txt"))
    }
}
