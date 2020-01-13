package aoc2019

import java.io.File
import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val prog = File("./data/day17.txt")
        .readText()
        .split(",")

    val result1 = day17_1(prog)
    println(result1)

    val result2 = day17_2(prog)
    println(result2)
}

private const val SCAFFOLD = 35
private const val SPACE = 46

private const val CH_NORTH = '^'.toInt()
private const val CH_EAST = '>'.toInt()
private const val CH_SOUTH = 'v'.toInt()
private const val CH_WEST = '<'.toInt()
private const val NORTH = 0
private const val EAST = 1
private const val SOUTH = 2
private const val WEST = 3
private val dx = intArrayOf(0, 1, 0, -1)
private val dy = intArrayOf(-1, 0, 1, 0)
private fun Int.rotateLeft(): Int = if (this == NORTH) WEST else (this - 1)
private fun Int.rotateRight(): Int = if (this == WEST) NORTH else (this + 1)
private fun XY.left(direction: Int): XY {
    return when (direction) {
        NORTH -> XY(x - 1, y)
        EAST -> XY(x, y - 1)
        SOUTH -> XY(x + 1, y)
        WEST -> XY(x, y + 1)
        else -> throw IllegalStateException("Unexisting direction $direction")
    }
}

private fun XY.right(direction: Int): XY {
    return when (direction) {
        NORTH -> XY(x + 1, y)
        EAST -> XY(x, y + 1)
        SOUTH -> XY(x - 1, y)
        WEST -> XY(x, y - 1)
        else -> throw IllegalStateException("Unexisting direction $direction")
    }
}

fun day17_1(prog: List<String>): Int {
    val intcodes = Intcode(prog)
    intcodes.process()

    val map = mutableMapOf<XY, Int>()
    var x = 0
    var y = 0
    while (intcodes.hasOutput()) {
        var cell = intcodes.getOutput().toInt()
        if (cell == 10) {
            ++y
            x = 0
        } else {
            map[XY(x, y)] = cell
            ++x
        }
    }

    val crosses = findCrosses(map)

    return crosses.sumBy { it.x * it.y }
}

fun day17_2(prog: List<String>): Int {
    val intcodes = Intcode(
        prog.mapIndexed { index, s ->
            if (index == 0) {
                "2"
            } else {
                s
            }
        }
    )

    intcodes.process()
    val map = mutableMapOf<XY, Int>()
    var x = 0
    var y = 0
    var direction = -1
    var startX = -1
    var startY = -1
    while (intcodes.hasOutput()) {
        var cell = intcodes.getOutput().toInt()

        when (cell) {
            CH_NORTH -> {
                direction = NORTH
                startX = x
                startY = y
            }
            CH_EAST -> {
                direction = EAST
                startX = x
                startY = y
            }
            CH_SOUTH -> {
                direction = SOUTH
                startX = x
                startY = y
            }
            CH_WEST -> {
                direction = WEST
                startX = x
                startY = y
            }
        }

        if (cell == 10) {
            ++y
            x = 0
        } else {
            map[XY(x, y)] = cell
            ++x
        }
    }

    val fullRout = findRoute(map, XY(startX, startY), direction)
    val routParts = cutRoute(fullRout)

    //main function, function A, function B, function C
    routParts.forEach { str ->
        str.forEach { intcodes.addInput(it.toInt()) }
        //new line symbol - terminal symbol
        intcodes.addInput(10)
    }

    //Continuous video feed?
    "n".forEach { intcodes.addInput(it.toInt()) }
    intcodes.addInput(10)//new line symbol

    var result = 0
    intcodes.process()
    while (intcodes.hasOutput()) {
        result = intcodes.getOutput().toInt()
    }

    return result
}

private fun findRoute(map: Map<XY, Int>, position: XY, direction: Int): String {
    val leftXY = position.left(direction)
    val rightXY = position.right(direction)

    val newDirection: Int
    var newXY: XY
    var action: String
    when (SCAFFOLD) {
        map[leftXY] -> {
            action = "L,"
            newDirection = direction.rotateLeft()
            newXY = leftXY
        }
        map[rightXY] -> {
            action = "R,"
            newDirection = direction.rotateRight()
            newXY = rightXY
        }
        else -> return ""
    }

    while (map[XY(newXY.x + dx[newDirection], newXY.y + dy[newDirection])] == SCAFFOLD) {
        newXY = XY(newXY.x + dx[newDirection], newXY.y + dy[newDirection])
    }
    val distance = max(abs(position.x - newXY.x), abs(position.y - newXY.y))

    action += distance.toString()

    return action + "," + findRoute(map, newXY, newDirection)
}

private fun cutRoute(route: String): List<String> {
    for (i in 1 until min(20, route.length)) {
        //cut only by comma
        if (route[i] != ',') {
            continue
        }
        val next = route.split(route.substring(0, i + 1)).filter { it.isNotEmpty() }
        if (next.isEmpty()) {
            continue
        }

        for (j in 1 until min(20, next[0].length)) {
            if (next[0][j] != ',') {
                continue
            }
            val nextSplitter = next[0].substring(0, j + 1)
            val third = next.flatMap { it.split(nextSplitter).filter { it.isNotEmpty() } }

            if (third.isEmpty()) {
                continue
            }

            if (third.all { it == third[0] }) {
                val a = route.substring(0, i)
                val b = nextSplitter.take(nextSplitter.length - 1)
                val c = third[0].take(third[0].length - 1)
                val main = route.replace(a, "A").replace(b, "B").replace(c, "C")

                //check main fits the size(21 because it has extra comma in the end)
                if (main.length <= 21) {
                    return listOf(main.take(main.length - 1), a, b, c) //main without last comma
                }
            }
        }
    }

    throw UnknownError("Can't calc")
}

private fun findCrosses(map: Map<XY, Int>): Set<XY> {
    return map.filter { (xy, value) ->
        value != SPACE &&
                map.getOrDefault(XY(xy.x + 1, xy.y), -1) != SPACE &&
                map.getOrDefault(XY(xy.x - 1, xy.y), -1) != SPACE &&
                map.getOrDefault(XY(xy.x, xy.y + 1), -1) != SPACE &&
                map.getOrDefault(XY(xy.x, xy.y - 1), -1) != SPACE
    }.keys
}