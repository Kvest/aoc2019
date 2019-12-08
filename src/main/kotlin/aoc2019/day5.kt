package aoc2019

import java.io.File
import java.lang.IllegalStateException

fun main() {
    day5_1(File("./data/day5.txt").readText().split(","))
    day5_2(File("./data/day5.txt").readText().split(","))
}

private fun day5_1(params: List<String>) {
    println("part1===================")
    val memory = IntArray(params.size) { params[it].toInt() }

    performIntcodes(memory) {
        1
    }

    println("end of part1===================\n\n")
}

private fun day5_2(params: List<String>) {
    println("part2===================")
    val memory = IntArray(params.size) { params[it].toInt() }

    performIntcodes(memory) {
        5
    }

    println("end of part2===================\n\n")
}


private fun performIntcodes(memory: IntArray, readInput: () -> Int) {
    fun getP0(i : Int) = if (((memory[i] / 100) % 10) == 1) memory[i + 1] else memory[memory[i + 1]]
    fun getP1(i : Int) = if (((memory[i] / 1000) % 10) == 1) memory[i + 2] else memory[memory[i + 2]]

    var i = 0
    while(true) {
        if (memory[i] == 99) {
            break
        }

        val instruction = memory[i] % 100
        when(instruction) {
            1 -> {
                val p0 = getP0(i)
                val p1 = getP1(i)
                memory[memory[i + 3]] = p0 + p1
                i += 4
            }
            2 -> {
                val p0 = getP0(i)
                val p1 = getP1(i)
                memory[memory[i + 3]] = p0 * p1
                i += 4
            }
            3 -> {
                memory[memory[i + 1]] = readInput()
                i += 2
            }
            4 -> {
                val p0 = getP0(i)
                println(p0)
                i += 2
            }
            5 -> {
                if (getP0(i) != 0) {
                    i = getP1(i)
                } else {
                    i += 3
                }
            }
            6 -> {
                if (getP0(i) == 0) {
                    i = getP1(i)
                } else {
                    i += 3
                }
            }
            7 -> {
                memory[memory[i + 3]] = if (getP0(i) < getP1(i)) 1 else 0
                i += 4
            }
            8 -> {
                memory[memory[i + 3]] = if (getP0(i) == getP1(i)) 1 else 0
                i += 4
            }
            else -> throw IllegalStateException("Unknown instruction ${memory[i]}")
        }
    }
}