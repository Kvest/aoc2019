package aoc2019

import java.lang.IllegalStateException
import java.util.*

class Intcode(private val memory: IntArray) {
    private val input: Queue<Int> = LinkedList<Int>()
    private val output: Queue<Int> = LinkedList<Int>()
    private var i = 0

    fun isHalted() = memory[i] == 99

    fun hasOutput() = output.isNotEmpty()
    fun getOutput() = output.poll()
    fun addInput(inValue: Int) = input.offer(inValue)

    fun process() {
        loop@ while(true) {
            if (isHalted()) {
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
                    if (input.isEmpty()) {
                        break@loop
                    }
                    memory[memory[i + 1]] = input.poll()
                    i += 2
                }
                4 -> {
                    val p0 = getP0(i)
                    output.offer(p0)
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

    private fun getP0(i : Int) = if (((memory[i] / 100) % 10) == 1) memory[i + 1] else memory[memory[i + 1]]
    private fun getP1(i : Int) = if (((memory[i] / 1000) % 10) == 1) memory[i + 2] else memory[memory[i + 2]]
}