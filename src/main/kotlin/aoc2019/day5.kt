package aoc2019

import java.io.File

fun main() {
    day5_1(File("./data/day5.txt").readText().split(","))
    day5_2(File("./data/day5.txt").readText().split(","))
}

private fun day5_1(params: List<String>) {
    println("part1===================")
    val incode = Intcode(params).also {
        it.addInput(1)
    }
    incode.process()

    while (incode.hasOutput()) {
        println(incode.getOutput())
    }

    println("end of part1===================\n\n")
}

private fun day5_2(params: List<String>) {
    println("part2===================")
    val incode = Intcode(params).also {
        it.addInput(5)
    }
    incode.process()

    while (incode.hasOutput()) {
        println(incode.getOutput())
    }

    println("end of part2===================\n\n")
}