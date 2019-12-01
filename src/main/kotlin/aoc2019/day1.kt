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
        .map(String::toInt)
        .map(::countFuel)
        .sum()
}

fun day1_2(params: List<String>): Int {
    return params
        .asSequence()
        .map(String::toInt)
        .map {
            var current = countFuel(it)
            var total = current

            while(current > 0) {
                current = countFuel(current)
                if (current > 0) {
                    total += current
                }
            }

            total
        }
        .sum()
}

private fun countFuel(mass: Int): Int = (mass / 3) - 2