package aoc2019

import java.io.File
import java.lang.IllegalStateException
import java.math.BigInteger
import kotlin.math.min

fun main() {
    val prog = File("./data/day15.txt")
        .readText()
        .split(",")

    val result1 = day15_1(prog)
    println(result1)

    val result2 = day15_2(prog)
    println(result2)
}

private const val NORTH = 1
private const val SOUTH = 2
private const val WEST = 3
private const val EAST = 4
private val directions = listOf(EAST, NORTH, WEST, SOUTH)
private val offsets = listOf(3, 0, 1, 2)
private val dx = listOf(1, 0, -1, 0)
private val dy = listOf(0, 1, 0, -1)

fun day15_1(prog: List<String>): Int {
    var offset = 0
    var x = 0
    var y = 0
    val steps = mutableMapOf<XY, Int>()
    var step = 0

    val intcodes = Intcode(prog)

    while (true) {
        loop@ for (i in 0 until directions.size) {
            val index = (i + offset) % directions.size

            intcodes.addInput(directions[index])
            intcodes.process()

            when (val res = intcodes.getOutput()) {
                BigInteger.ONE, BigInteger.TWO -> {
                    x += dx[index]
                    y += dy[index]

                    step = min(step + 1, steps.getOrDefault(XY(x, y), Int.MAX_VALUE))
                    steps[XY(x, y)] = step

                    offset = offsets[index]

                    if (res == BigInteger.TWO) {
                        return step
                    }

                    break@loop
                }
            }
        }
    }

    throw IllegalStateException("Unreachable")
}

fun day15_2(prog: List<String>): Int {
    var offset = 0
    var x = 0
    var y = 0
    val cells = mutableMapOf<XY, Int>()
    val next = mutableSetOf<XY>()

    val intcodes = Intcode(prog)

    //explore whole maze
    do {
        loop@ for (i in 0 until directions.size) {
            val index = (i + offset) % directions.size

            intcodes.addInput(directions[index])
            intcodes.process()

            when (val res = intcodes.getOutput()) {
                BigInteger.ONE, BigInteger.TWO -> {
                    x += dx[index]
                    y += dy[index]

                    cells[XY(x, y)] = res.toInt()

                    offset = offsets[index]

                    if (res ==BigInteger.TWO) {
                        next.add(XY(x, y))
                    }

                    break@loop
                }
            }
        }
    } while (x != 0 || y != 0)

    //delete start position
    cells.remove(next.first())

    var time = 0
    while (cells.isNotEmpty()) {
        val old = next.toList()
        next.clear()

        old.forEach { xy ->
            var n = XY(xy.x + 1, xy.y)
            if (cells.contains(n)) {
                cells.remove(n)
                next.add(n)
            }

            n = XY(xy.x - 1, xy.y)
            if (cells.contains(n)) {
                cells.remove(n)
                next.add(n)
            }

            n = XY(xy.x, xy.y + 1)
            if (cells.contains(n)) {
                cells.remove(n)
                next.add(n)
            }

            n = XY(xy.x, xy.y - 1)
            if (cells.contains(n)) {
                cells.remove(n)
                next.add(n)
            }
        }
        ++time
    }

    return time
}