package lesson4

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KtTrieTest : AbstractTrieTest() {

    override fun create(): MutableSet<String> =
        KtTrie()

    @Test
    @Tag("Example")
    fun generalTest() {
        doGeneralTest()
    }

    @Test
    @Tag("7")
    fun iteratorTest() {
        doIteratorTest()
    }

    @Test
    fun myIteratorTest() {
        val trieSet = create()
        val list = listOf(
            "b",
            "fhbgfeeadbaghf",
            "eeefcbaabbcffc",
            "bdbfdee",
            "adhechgaehcddb",
            "cfah",
            "fadfgafh",
            "geefb",
            "cabgfagefaaade",
            "cgacgfbbcg",
            "eh",
            "ag",
            "baeb",
            "aagghgcdd"
        )
        val controlSet = mutableSetOf<String>()
        controlSet.addAll(list)
        for (element in controlSet) {
            trieSet += element
        }
        println("Control set: $controlSet")
        val trieIter = trieSet.iterator()
        println("Checking if the iterator traverses the entire set...")
        while (trieIter.hasNext()) {
            val tmp = trieIter.next()
            controlSet.remove(tmp)
        }
        assertTrue(
            controlSet.isEmpty(),
            "TrieIterator doesn't traverse the entire set."
        )
        assertFailsWith<IllegalStateException>("Something was supposedly returned after the elements ended") {
            trieIter.next()
        }
        println("All clear!")
    }

    @Test
    @Tag("8")
    fun iteratorRemoveTest() {
        doIteratorRemoveTest()
    }

}