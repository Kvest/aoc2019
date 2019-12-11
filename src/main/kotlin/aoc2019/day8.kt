package aoc2019

import java.io.File

fun main() {
    val input = File("./data/day8.txt").readText().map {
        it.toString().toInt()
    }

    val result1 = day8_1(input, wide = 25, tall = 6)
    println(result1)

    day8_2(input, wide = 25, tall = 6)
}

fun day8_1(input: List<Int>, wide : Int, tall: Int): Int {
    val count = wide * tall
    var min = Int.MAX_VALUE
    var result = -1

    var a = Int.MAX_VALUE
    var b = 0
    var c = 0
    input.forEachIndexed { index, value ->
        if ((index % count) == 0) {
            if (a < min) {
                min = a
                result = b * c
            }

            a = 0
            b = 0
            c = 0
        }

        when(value) {
            0 -> ++a
            1 -> ++b
            2 -> ++c
        }
    }

    return result
}

fun day8_2(input: List<Int>, wide : Int, tall: Int) {
    val count = wide * tall

    repeat(count) { i ->
        if (i % wide == 0) {
            println()
        }

        var di = 0
        while(input[i + di] >= 2) {
            di += count
        }

        print(if (input[i + di] == 0) " " else "#")
    }
}
