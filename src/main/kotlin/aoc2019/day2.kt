package aoc2019

import java.io.File
import java.lang.IllegalStateException

fun main() {
    val result1 = day2_1(File("./data/day2_1.txt").readText().split(","))
    println(result1)

    val result2 = day2_2(File("./data/day2_2.txt").readText().split(","))
    println(result2)
}

fun day2_1(params: List<String>): Int {
    val memory = IntArray(params.size) { params[it].toInt() }

    memory[1] = 12
    memory[2] = 2

    performIntcodes(memory)

    return memory[0]
}

fun day2_2(params: List<String>): Int {
    val initialMemory = IntArray(params.size) { params[it].toInt() }
    val target = 19690720

    (0..99).forEach { noun ->
        (0..99).forEach { verb ->
            val memory = initialMemory.copyOf()
            memory[1] = noun
            memory[2] = verb

            performIntcodes(memory)

            if (memory[0] == target) {
                return 100 * noun + verb
            }
        }
    }

    return -1
}

private fun performIntcodes(memory: IntArray) {
    var i = 0
    while(true) {
        if (memory[i] == 99) {
            break
        }

        when(memory[i]) {
            1 -> {
                memory[memory[i + 3]] = memory[memory[i + 1]] + memory[memory[i + 2]]
                i += 4
            }
            2 -> {
                memory[memory[i + 3]] = memory[memory[i + 1]] * memory[memory[i + 2]]
                i += 4
            }
            else -> throw IllegalStateException("Unknown instruction ${memory[i]}")
        }
    }
}