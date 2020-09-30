@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.sqrt

/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    val data = File(inputName).readLines().map { it.toInt() }
    if (data.size < 2) throw IllegalArgumentException()
    val subArray = IntArray(File(inputName).readLines().map { it.toInt() }.size - 1)
    for (i in data.indices) {
        if (i == 0) continue
        subArray[i - 1] = data[i] - data[i - 1]
    }

    val answer = maxSubArray(subArray)

    return Pair(answer.first + 1, answer.second + 2)
}/* время O(n)
    память O(n)*/

private fun maxSubArray(array: IntArray): Pair<Int, Int> {
    var li = 0
    var ri = 0
    var range = Pair(0, 0)
    var current = 0
    var maxSum = array[0]
    while (ri < array.size) {
        current += array[ri]

        if (current > maxSum) {
            maxSum = current
            range = Pair(li, ri)
        }

        if (current < 0) {
            current = 0
            li = ri + 1
        }

        ri++
    }
    return range
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 * Общий комментарий: решение из Википедии для этой задачи принимается,
 * но приветствуется попытка решить её самостоятельно.
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    var res = 0
    for (i in 1..menNumber) {
        res = (res + choiceInterval) % i
    }
    return res + 1
}/* Время O(n)
    Память O(1)*/

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */
fun longestCommonSubstring(first: String, second: String): String {
    val data = Array(first.length + 1) { IntArray(second.length + 1) }
    var maxSubString = ""
    for (i in first.indices)
        for (j in second.indices) {
            if (first[i] == second[j]) {
                data[i + 1][j + 1] = data[i][j] + 1
                if (data[i + 1][j + 1] > maxSubString.length)
                    maxSubString = first.substring(i + 1 - data[i + 1][j + 1], i + 1)
            }
        }
    return maxSubString
}/* Время O(n*m)
    Память O(n*m + 1) n,m = first,second.length*/

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */
fun calcPrimesNumber(limit: Int): Int {
    if (limit <= 1) return 0
    val number = BooleanArray(limit + 1) { true }
    number[0] = false
    number[1] = false
    val border = sqrt(limit.toDouble()).toInt()
    var j = 0
    for (i in 2..border) {
        if (number[i]) {
            j = i * 2
            while (j < limit + 1) {
                number[j] = false
                j += i
            }
        }
    }
    return number.filter { it }.size
}/* время O(N*log(logN))
    память O(N) */