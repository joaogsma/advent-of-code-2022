package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInputVersion1() {
        assertEquals(24, runProblem1("example.txt"))
    }

    @Test fun testRealInputVersion1() {
        assertEquals(755, runProblem1("input.txt"))
    }

    @Test fun testExampleInputVersion2() {
        assertEquals(93, runProblem2("example.txt"))
    }

    @Test fun testRealInputVersion2() {
        assertEquals(29805, runProblem2("input.txt"))
    }
}
