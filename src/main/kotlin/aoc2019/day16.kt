package aoc2019

import java.io.File
import java.lang.IllegalStateException
import kotlin.math.absoluteValue

fun main() {
    val input = File("./data/day16.txt")
        .readText()
        .map {
            it.toString().toInt()
        }

    val result1 = day16_1(input)
    println(result1)

    val result2 = day16_2(input)
    println(result2)
}

private val PHASES = 100
private val PATTERN = listOf(0, 1, 0, -1)

fun day16_1(items: List<Int>): String {
    val result = transform(items)

    return result.take(8).joinToString(separator = "")
}

fun day16_2(items: List<Int>): String {
    var index = 0

    //first 7 digits are offset
    var mul = 1000000
    (0..6).forEach { i ->
        index += items[i] * mul
        mul /= 10
    }

    if (index < (items.size * 10000) / 2) throw IllegalStateException("THis algorithm doesn't work for cases when index in the first half of the items")

    val size = (items.size * 10000) - index + 1

    var result = IntArray(size) { items[(index++) % items.size] }

    repeat(PHASES) {
        var sum = 0
        for (i in (result.size - 1) downTo 0) {
            sum += result[i]
            result[i] = sum.absoluteValue % 10
        }
    }

    return result.take(8).joinToString(separator = "")
}

private fun transform(items: List<Int>): List<Int> {
    var result = items

    repeat(PHASES) {
        result = result.mapIndexed { i, _ ->
            val sum = result.foldIndexed(0) {
                    j, sum, item -> sum + item * getKoeff(i, j)
            }

            sum.absoluteValue % 10
        }
    }

    return result
}

private fun getKoeff(i: Int, j: Int): Int {
    val a = (j + 1) % (PATTERN.size * (i + 1))
    return PATTERN[a / (i + 1)]
}