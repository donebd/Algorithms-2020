@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.abs

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {
    require(!File(inputName).readLines().none { Regex("""(([0-9]{2}:[0-9]{2}:[0-9]{2}) [AaPp][Mm])""").matches(it) })
    val times = mutableListOf<Pair<String, Int>>()
    for (i in File(inputName).readLines()) {
        if (i.endsWith("AM")) {
            times.add(Pair(i, calculateTime(i, true)))
        } else {
            times.add(Pair(i, calculateTime(i, false)))
            // да как-то моя цепочка рассуждений, свернула не в то место в прошлый раз
        }
    }
    times.sortBy { it.second }
    val writer = File(outputName).bufferedWriter()
    times.map {
        writer.write(it.first)
        writer.newLine()
    }
    writer.close()
}/* время O(N*logN)
    память O(N)*/

private fun calculateTime(str: String, am: Boolean): Int {
    var hrs = str.substring(0..1).toInt()
    val minute = str.substring(3..4).toInt()
    val seconds = str.substring(6..7).toInt()
    if (hrs > 12 || minute > 60 || seconds > 60 || hrs == 0) throw IllegalArgumentException()
    if (hrs == 12) hrs = 0
    if (!am) hrs += 12
    return hrs * 3600 + minute * 60 + seconds
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {
    val count = IntArray(7731)// -2730..5000
    val lowNumLim = 2730
    for (element in File(inputName).readLines()) {
        count[(element.toDouble() * 10).toInt() + lowNumLim]++
    }
    val writer = File(outputName).bufferedWriter()
    for (i in count.indices) {
        for (j in 1..count[i]) {
            if (i < lowNumLim) writer.write("-")
            writer.write("${abs((i - lowNumLim) / 10)}")
            writer.write(".")
            writer.write("${abs((i - lowNumLim) % 10)}")
            writer.newLine()
        }
    }
    writer.close()
}/* время O(N)
    память O(Const) Const = 7731*/

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    val map = mutableMapOf<String, Int>()
    for (i in File(inputName).readLines()) {
        map[i] = map.getOrPut(i) { 0 } + 1
    }

    if (map.isEmpty()) {
        File(outputName).writeText("")
        return
    }

    var maxCnt = 0
    for (i in map) {
        if (i.value > maxCnt) {
            maxCnt = i.value
        }
    }

    val minStr = map.filterKeys { map[it] == maxCnt }.minBy { it.key.toInt() }!!.key

    val writer = File(outputName).bufferedWriter()
    for (i in File(inputName).readLines()) {
        if (i != minStr) {
            writer.write(i)
            writer.newLine()
        }
    }
    for (i in 1..maxCnt) {
        writer.write(minStr)
        writer.newLine()
    }
    writer.close()
}/* время O(n), наверное
    память O(n)*/

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    var li = 0
    var ri = first.size
    for (i in second.indices) {
        if (li < first.size && (ri == second.size || first[li] <= second[ri]!!)) {
            second[i] = first[li++]
        } else {
            second[i] = second[ri++]
        }
    }
}/* время O(n)
    память O(2)*/
