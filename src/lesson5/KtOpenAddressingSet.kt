package lesson5

/**
 * Множество(таблица) с открытой адресацией на 2^bits элементов без возможности роста.
 */
class KtOpenAddressingSet<T : Any>(private val bits: Int) : AbstractMutableSet<T>() {
    init {
        require(bits in 2..31)
    }

    private class Removed

    private val capacity = 1 shl bits

    private val storage = Array<Any?>(capacity) { null }

    override var size: Int = 0

    /**
     * Индекс в таблице, начиная с которого следует искать данный элемент
     */
    private fun T.startingIndex(): Int {
        return hashCode() and (0x7FFFFFFF shr (31 - bits))
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    override fun contains(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null || current is Removed) {
            if (current == element) {
                return true
            }
            index = (index + 1) % capacity
            if (index == startingIndex) return false
            current = storage[index]
        }
        return false
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    override fun add(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null && current !is Removed) {
            if (current == element) {
                return false
            }
            index = (index + 1) % capacity
            check(index != startingIndex) { "Table is full" }
            current = storage[index]
        }
        storage[index] = element
        size++
        return true
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: [java.util.Set.remove] (Ctrl+Click по remove)
     *
     * Средняя
     */
    override fun remove(element: T): Boolean {
        val startIndex = element.startingIndex()
        var index = startIndex
        var current = storage[index]
        while (current != null && current !is Removed) {
            if (current == element) {
                storage[index] = Removed()
                size--
                return true
            }
            index = (index + 1) % capacity
            if (index == startIndex) return false
            current = storage[index]
        }
        return false
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    override fun iterator(): MutableIterator<T> = TableIterator()

    inner class TableIterator internal constructor() : MutableIterator<T> {
        private var index = -1
        private var current: Any? = null
        private val tableSize = storage.size

        private fun findNext(onlyCheck: Boolean): Boolean {
            var tmpIndex = index + 1
            val tmpCurrent: Any?
            while (tmpIndex != tableSize && (storage[tmpIndex] == null || storage[tmpIndex] is Removed)) {
                tmpIndex++
            }
            tmpCurrent = if (tmpIndex != tableSize) {
                storage[tmpIndex]
            } else {
                null
            }
            if (!onlyCheck) {
                current = tmpCurrent
                index = tmpIndex
            }
            return tmpCurrent != null
        }

        override fun hasNext(): Boolean {
            return findNext(true)
        }

        override fun next(): T {
            findNext(false)
            if (current == null) throw IllegalStateException()
            return current as T
        }

        override fun remove() {
            if (current == null) throw IllegalStateException()
            else {
                remove(current)
                current = null
            }
        }

    }
}