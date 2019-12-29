package aoc2019

import java.io.File

fun main() {
    val prog = File("./data/day19.txt")
        .readText()
        .split(",")

    val result1 = day19_1(prog)
    println(result1)

    val result2 = day19_2(prog)
    println(result2)
}

fun day19_1(prog: List<String>): Int {
    var result = 0

    for (x in 0..49) {
        for (y in 0..49) {
            result += getSignal(prog, x, y)
        }
    }

    return result
}

private const val SQUARE_SIZE = 100
fun day19_2(prog: List<String>): Int {
    var x = SQUARE_SIZE - 1
    var y = 0
    while (getSignal(prog, x, y) == 0) {
        ++y
    }

    while (getSignal(prog, x - (SQUARE_SIZE - 1), y + (SQUARE_SIZE - 1)) == 0) {
        ++x
        while (getSignal(prog, x, y) == 0) {
            ++y
        }
    }

    return (x - (SQUARE_SIZE - 1)) * 10000 + y
}

private fun getSignal(prog: List<String>, x: Int, y: Int) : Int {
    val intcodes = Intcode(prog)

    intcodes.addInput(x)
    intcodes.addInput(y)

    intcodes.process()

    return intcodes.getOutput().toInt()
}