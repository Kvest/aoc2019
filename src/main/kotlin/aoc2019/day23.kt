package aoc2019

import java.io.File
import java.math.BigInteger

fun main() {
    val prog = File("./data/day23.txt")
        .readText()
        .split(",")

    val result1 = day23_1(prog)
    println(result1)

    val result2 = day23_2(prog)
    println(result2)
}

fun day23_1(prog: List<String>): Int {
    val defaultInput: () -> BigInteger = {
        (-1).toBigInteger()
    }
    val computers = Array(50) { address ->
        Intcode(prog).also {
            it.addInput(address)
            it.setDefaultInput(defaultInput)
        }
    }

    while (true) {
        computers.forEach { it.tick() }
        computers.forEach {
            while (it.getOutputSize() >= 3) {
                val destination = it.getOutput().toInt()
                val x = it.getOutput()
                val y = it.getOutput()

                if (destination == 255) {
                    return y.toInt()
                } else {
                    computers[destination].addInput(x)
                    computers[destination].addInput(y)
                }
            }
        }
    }
}

fun day23_2(prog: List<String>): Int {
    val di = Array(50) {
        DefaultInput()
    }
    val computers = Array(50) { address ->
        Intcode(prog).also {
            it.addInput(address)
            it.setDefaultInput(di[address]::request)
        }
    }

    var natX: BigInteger? = null
    var natY: BigInteger? = null
    var prevY = natY
    while (true) {
        if (di.all { it.requested }) {
            if (natX != null && natY != null) {
                if (prevY == natY) {
                    return natY.toInt()
                }
                prevY = natY

                computers[0].addInput(natX)
                computers[0].addInput(natY)
            }
            di.forEach { it.reset() }
        }

        computers.forEach { it.tick() }
        computers.forEach{
            while (it.getOutputSize() >= 3) {
                val destination = it.getOutput().toInt()
                val x = it.getOutput()
                val y = it.getOutput()

                if (destination == 255) {
                    natX = x
                    natY = y
                } else {
                    computers[destination].addInput(x)
                    computers[destination].addInput(y)
                    di[destination].reset()
                }
            }
        }
    }
}

private class DefaultInput {
    var requested = false
        private set


    fun request(): BigInteger {
        requested = true

        return (-1).toBigInteger()
    }

    fun reset() {
        requested = false
    }
}