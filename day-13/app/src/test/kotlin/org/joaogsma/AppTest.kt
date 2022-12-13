package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test fun testExampleInputVersion1() { assertEquals(13, runProblem1("example.txt")) }

    @Test fun testRealInputVersion1() { assertEquals(5252, runProblem1("input.txt")) }

    @Test fun testExampleInputVersion2() { assertEquals(140, runProblem2("example.txt")) }

    @Test fun testRealInputVersion2() { assertEquals(20592, runProblem2("input.txt")) }
}
