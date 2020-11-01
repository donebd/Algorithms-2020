package lesson4

import java.util.*

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
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
        return remove(current)
    }//Время O(log(N))//больше веток -> больше основание log -> меньше время

    private fun remove(element: Node): Boolean {
        if (element.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator(): MutableIterator<String> = TrieIterator()

    inner class TrieIterator internal constructor() : MutableIterator<String> {

        private var current: Pair<String, Node>? = null
        private val stackElement = Stack<Pair<String, Node>>()
        private val visited = mutableListOf<Map.Entry<Char, Node>>()

        init {
            findNext(root, "")
        }

        private fun findNext(node: Node?, string: String): Boolean {// returns the presence of unvisited nodes
            val next = node!!.children.filter { it !in visited && it.key != 0.toChar() }
                .entries// не сильно затратная операция потому что visited, содержит только верхние посещенные элементы
            if (next.isEmpty() && node.children.keys.contains(0.toChar())) {
                stackElement.push(Pair(string, node))
                return false
            }
            if (next.isNotEmpty()) {
                if (!findNext(next.first().value, string + next.first().key)) {
                    next.first().value.children.map { visited.remove(it) }//если все дети посещаны, убираем их и помещаем родителя
                    visited.add(next.first())
                }
                next.map { if (it !in visited) return true }// не все дети посещаны
            }
            if (node.children.contains(0.toChar())) stackElement.push(Pair(string, node))
            return false
        }// Время O(A), где А = кол-во символов / кол-во элементов

        override fun hasNext(): Boolean {
            return stackElement.isNotEmpty()
        }// Время O(1)

        override fun next(): String {
            if (stackElement.isEmpty()) throw IllegalStateException()
            current = stackElement.pop()
            findNext(root, "")
            return current!!.first
        }// Время O(1)

        override fun remove() {
            if (current == null) throw IllegalStateException()
            else {
                remove(current!!.second)
                current = null
            }
        }// Время O(реализация remove)

    }

    internal class MyEntry<K, V>(override val key: K, override var value: V) : MutableMap.MutableEntry<K, V> {

        override fun setValue(value: V): V {
            val old = this.value
            this.value = value
            return old
        }

    }

}