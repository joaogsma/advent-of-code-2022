/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package org.joaogsma

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AppTest {
    @Test fun testExampleInputProblem1() {
        assertEquals(157, runProblem1("example.txt"))
    }

    @Test fun testRealInputProblem1() {
        assertEquals(7428, runProblem1("input.txt"))
    }

    @Test fun testExampleInputProblem2() {
        assertEquals(70, runProblem2("example.txt"))
    }

    @Test fun testRealInputProblem2() {
        assertEquals(2650, runProblem2("input.txt"))
    }
}