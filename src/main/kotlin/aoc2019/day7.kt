package aoc2019

import java.io.File
import kotlin.math.max

fun main() {
    val prog = File("./data/day7.txt").readText().split(",").map { it.toInt() }

    day7_1(prog.toIntArray())
    day7_2(prog.toIntArray())
}

private fun day7_1(prog: IntArray) {
    val order = intArrayOf(0, 1, 2, 3, 4)

    var maxSignal = Int.MIN_VALUE
    order.permute {
        maxSignal = max(maxSignal, calcAmplifierOutput(prog, it))
    }

    println(maxSignal)
}

private fun day7_2(prog: IntArray) {
    val order = intArrayOf(5, 6, 7, 8, 9)
    var maxSignal = Int.MIN_VALUE
    order.permute {
        maxSignal = max(maxSignal, calcLoopedAmplifierOutput(prog, it))
    }

    println(maxSignal)
}

fun calcAmplifierOutput(memory: IntArray, order: IntArray): Int {
    var output = 0

    order.forEach { index ->
        val intcode = Intcode(memory.copyOf()).also {
            it.addInput(index)
            it.addInput(output)
        }

        intcode.process()

        output = intcode.getOutput()
    }

    return output
}

fun calcLoopedAmplifierOutput(memory: IntArray, order: IntArray): Int {
    val intcodes = order.map {
        Intcode(memory.copyOf()).apply {
            addInput(it)
        }
    }
    intcodes[0].addInput(0)

    var result = 0
    while (!intcodes.last().isHalted()) {
        intcodes.forEachIndexed { index, intcode ->
            intcode.process()

            if (intcode.isHalted() && index == intcodes.size - 1) {
                result = intcode.getOutput()
            } else {
                val next = (index + 1) % intcodes.size
                intcodes[next].addInput(intcode.getOutput())
            }
        }
    }

    return result
}