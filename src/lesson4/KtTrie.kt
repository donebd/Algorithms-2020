package lesson4

import java.util.*

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        var current: String = ""
        val children: MutableMap<Char, Node> = linkedMapOf()
    }

    private var root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                newChild.current = "${current.current}$char"
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }//Время O(log(N))//больше веток -> больше основание log -> меньше время

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator(): MutableIterator<String> = TrieIterator()

    inner class TrieIterator internal constructor() : MutableIterator<String> {

        private var current: String? = null
        private val stackElement = Stack<String>()

        init {
            fillStack(root)
        }

        private fun fillStack(node: Node?) {
            node?.children?.map {
                if (it.value.children.contains(0.toChar())) stackElement.push(it.value.current)
                fillStack(it.value)
            }
        }// Время O(N)

        override fun hasNext(): Boolean {
            return stackElement.isNotEmpty()
        }// Время O(1)

        override fun next(): String {
            if (stackElement.isEmpty()) throw IllegalStateException()
            current = stackElement.pop()
            return current!!
        }// Время O(1)

        override fun remove() {
            if (current == null) throw IllegalStateException()
            else {
                remove(current)
                current = null
            }
        }// Время O(реализация remove)

    }

}