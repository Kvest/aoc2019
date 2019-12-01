package aoc2019

import java.io.File

fun main() {
    val result1 = day1_1(File("./data/day1_1.txt").readLines())
    println(result1)

    val result2 = day1_2(File("./data/day1_2.txt").readLines())
    println(result2)
}

fun day1_1(params: List<String>): Int {
    return params
        .asSequence()
        .map {
            it.toInt()
        }
        .map {
            (it / 3) - 2
        }
        .sum()
}

fun day1_2(params: List<String>): Int {
    return params
        .asSequence()
        .map {
            it.toInt()
        }
        .map {
            var current = (it / 3) - 2
            var total = current

            while(current > 0) {
                current = (current / 3) - 2
                if (current > 0) {
                    total += current
                }
            }

            total
        }
        .sum()
}