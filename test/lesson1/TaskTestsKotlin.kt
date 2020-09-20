package lesson1

import org.junit.jupiter.api.Tag
import java.io.File
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TaskTestsKotlin : AbstractTaskTests() {

    @Test
    @Tag("3")
    fun testSortTimes() {
        sortTimes { inputName, outputName -> sortTimes(inputName, outputName) }
    }

    @Test
    fun myTestSortTimes() {
        val file = File("qwerty.txt")
        val file1 = File("temp.txt")
        file.deleteOnExit()
        file1.deleteOnExit()
        file.writeText("13:12:06 AM")
        assertFailsWith<IllegalArgumentException> { sortTimes(file.name, file1.name) }
        file.writeText("-12:06:14 PM")
        assertFailsWith<IllegalArgumentException> { sortTimes(file.name, file1.name) }
        file.writeText("")
        assertFailsWith<IllegalArgumentException> { sortTimes(file.name, file1.name) }
        file.writeText("00:13:13 AM")
        assertFailsWith<IllegalArgumentException> { sortTimes(file.name, file1.name) }
    }

    @Test
    @Tag("4")
    fun testSortAddresses() {
        sortAddresses { inputName, outputName -> sortAddresses(inputName, outputName) }
    }

    @Test
    @Tag("4")
    fun testSortTemperatures() {
        sortTemperatures { inputName, outputName -> sortTemperatures(inputName, outputName) }
    }

    @Test
    fun myTestSortTemperatures() {
        val file = File("qwerty.txt")
        val file1 = File("temp.txt")
        file.deleteOnExit()
        file1.deleteOnExit()
        file.writeText("0.0\n0.0\n0.0")
        sortTemperatures(file.name, file1.name)
        assertFileContent(file1.name, "0.0\n0.0\n0.0")
        file.writeText("-0.0\n0.0\n0.0")
        sortTemperatures(file.name, file1.name)
        assertFileContent(file1.name, "0.0\n0.0\n0.0")
        file.writeText("0.0\n-273.0\n500.0")
        sortTemperatures(file.name, file1.name)
        assertFileContent(file1.name, "-273.0\n0.0\n500.0")
        file.writeText("")
        sortTemperatures(file.name, file1.name)
        assertFileContent(file1.name, "")
    }

    @Test
    @Tag("4")
    fun testSortSequence() {
        sortSequence { inputName, outputName -> sortSequence(inputName, outputName) }
    }

    @Test
    fun myTestSortSequence() {
        val file = File("qwerty.txt")
        val file1 = File("temp.txt")
        file.deleteOnExit()
        file1.deleteOnExit()
        file.writeText("")
        sortSequence(file.name, file1.name)
        assertFileContent(file1.name, "")
        file.writeText("0\n0\n1\n0\n1\n1")
        sortSequence(file.name, file1.name)
        assertFileContent(file1.name, "1\n1\n1\n0\n0\n0")
    }

    @Test
    @Tag("2")
    fun testMergeArrays() {
        mergeArrays { first, second -> mergeArrays(first, second) }
    }

    @Test
    fun myTestMergeArrays() {
        val second = arrayOf(null, -1)
        val first = arrayOf(1)
        mergeArrays(first, second)
        assertTrue { second.contentEquals(arrayOf(-1, 1)) }
    }
}
