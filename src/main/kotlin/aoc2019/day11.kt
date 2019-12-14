package aoc2019

import java.io.File
import java.lang.IllegalStateException
import kotlin.math.max
import kotlin.math.min

fun main() {
    val prog = File("./data/day11.txt")
        .readText()
        .split(",")

    val result1 = day11_1(prog)
    println(result1)

    day11_2(prog)
}

private const val UP = 0
private const val RIGHT = 1
private const val DOWN = 2
private const val LEFT = 3

private const val BLACK = 0
private const val WHITE = 1

private fun day11_1(prog: List<String>): Int {
    var canvas = mutableMapOf<XY, Int>()
    val startLocation = XY(0, 0)

    drawIdentifier(prog, canvas, startLocation)

    return canvas.size
}

private fun day11_2(prog: List<String>) {
    var canvas = mutableMapOf<XY, Int>()
    val startLocation = XY(0, 0)
    canvas[startLocation] = WHITE

    drawIdentifier(prog, canvas, startLocation)

    var xMax = Int.MIN_VALUE
    var xMin = Int.MAX_VALUE
    var yMax = Int.MIN_VALUE
    var yMin = Int.MAX_VALUE
    canvas.forEach { key, v ->
        xMax = max(xMax, key.x)
        xMin = min(xMin, key.x)

        yMax = max(yMax, key.y)
        yMin = min(yMin, key.y)
    }

    (yMin..yMax).forEach { y ->
        (xMin..xMax).forEach { x ->
            when(canvas.getOrDefault(XY(x, y), BLACK)) {
                WHITE -> print("#")
                BLACK -> print(" ")
            }
        }

        println()
    }
}

private fun drawIdentifier(prog: List<String>, canvas: MutableMap<XY, Int>, startLocation: XY) {
    val intcodes = Intcode(prog)

    var direction = UP
    var location = startLocation
    while(!intcodes.isHalted()) {
        intcodes.addInput(canvas.getOrDefault(location, BLACK))
        intcodes.process()

        if (intcodes.hasOutput()) {
            canvas[location] = intcodes.getOutput().toInt()

            val rotate = intcodes.getOutput().toInt()
            when (rotate) {
                0 -> {
                    direction--
                    if (direction == -1) {
                        direction = LEFT
                    }
                }
                1 -> {
                    direction = (direction + 1) % 4
                }
            }

            location = when(direction) {
                UP -> location.copy(y = location.y + 1)
                RIGHT -> location.copy(x = location.x + 1)
                DOWN -> location.copy(y = location.y - 1)
                LEFT -> location.copy(x = location.x - 1)
                else -> throw IllegalStateException("Not possible")
            }
        }
    }
}