package aoc2019

import java.io.File
import kotlin.math.max

fun main() {
    val prog = File("./data/day7.txt").readText().split(",")

    day7_1(prog)
    day7_2(prog)
}

private fun day7_1(prog: List<String>) {
    val order = intArrayOf(0, 1, 2, 3, 4)

    var maxSignal = Int.MIN_VALUE
    order.permute {
        maxSignal = max(maxSignal, calcAmplifierOutput(prog, it))
    }

    println(maxSignal)
}

private fun day7_2(prog: List<String>) {
    val order = intArrayOf(5, 6, 7, 8, 9)
    var maxSignal = Int.MIN_VALUE
    order.permute {
        maxSignal = max(maxSignal, calcLoopedAmplifierOutput(prog, it))
    }

    println(maxSignal)
}

fun calcAmplifierOutput(memory: List<String>, order: IntArray): Int {
    var output = 0

    order.forEach { index ->
        val intcode = Intcode(memory).also {
            it.addInput(index)
            it.addInput(output)
        }

        intcode.process()

        output = intcode.getOutput().toInt()
    }

    return output
}

fun calcLoopedAmplifierOutput(memory: List<String>, order: IntArray): Int {
    val intcodes = order.map {
        Intcode(memory).apply {
            addInput(it)
        }
    }
    intcodes[0].addInput(0)

    var result = 0
    while (!intcodes.last().isHalted()) {
        intcodes.forEachIndexed { index, intcode ->
            intcode.process()

            if (intcode.isHalted() && index == intcodes.size - 1) {
                result = intcode.getOutput().toInt()
            } else {
                val next = (index + 1) % intcodes.size
                intcodes[next].addInput(intcode.getOutput())
            }
        }
    }

    return result
}