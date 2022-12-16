package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInputVersion1() {
        assertEquals(26, runProblem1("example.txt", 10))
    }

    @Test fun testRealInputVersion1() {
        assertEquals(5508234, runProblem1("input.txt", 2000000))
    }

    @Test fun testExampleInputVersion2() {
        assertEquals(56000011, runProblem2("example.txt", 20))
    }

    @Test fun testRealInputVersion2() {
        assertEquals(10457634860779, runProblem2("input.txt", 4000000))
    }
}
