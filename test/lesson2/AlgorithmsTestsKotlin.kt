package lesson2

import org.junit.jupiter.api.Tag
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AlgorithmsTestsKotlin : AbstractAlgorithmsTests() {
    @Test
    @Tag("2")
    fun testOptimizeBuyAndSell() {
        optimizeBuyAndSell { optimizeBuyAndSell(it) }
    }

    @Test
    fun myTestOptimizeBuyAndSell() {
        val file = File("qwerty.txt")
        file.deleteOnExit()
        file.writeText("2\n-3")
        assertEquals(Pair(1, 2), optimizeBuyAndSell(file.name))
        file.writeText("1")
        assertFailsWith<IllegalArgumentException> { optimizeBuyAndSell(file.name) }
    }

    @Test
    @Tag("2")
    fun testJosephTask() {
        josephTask { menNumber, choiceInterval -> josephTask(menNumber, choiceInterval) }
    }

    @Test
    @Tag("4")
    fun testLongestCommonSubstring() {
        longestCommonSubstring { first, second -> longestCommonSubstring(first, second) }
    }

    @Test
    @Tag("3")
    fun testCalcPrimesNumber() {
        calcPrimesNumber { calcPrimesNumber(it) }
    }
}