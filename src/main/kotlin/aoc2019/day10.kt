package aoc2019

import java.io.File
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2

fun main() {
    val asteroidsMap = File("./data/day10.txt").readLines()

    val (result1, location) = day10_1(asteroidsMap)
    println(result1)

    val result2 = day10_2(asteroidsMap, location)
    println(result2)
}

fun day10_1(asteroidsMap: List<String>): Pair<Int, XY> {
    val asteroids = parseAsteroidsMap(asteroidsMap)

    var max = -1
    lateinit var location: XY
    asteroids.forEach{ from ->
        val size = asteroids.map { to ->
            Vector.of(from, to)
        }.distinct().size - 1

        if (size > max) {
            max = size
            location = from
        }
    }

    return Pair(max, location)
}

private const val TARGET_ITEM_NUMBER = 200
fun day10_2(asteroidsMap: List<String>, location: XY): Int {
    val asteroids = parseAsteroidsMap(asteroidsMap) - location

    val grouped = asteroids.groupBy { Vector.of(location, it) }
    val maxCount = grouped.maxBy { it.value.size }?.value?.size ?: 0
    val counts = IntArray(maxCount) { 0 }
    grouped.forEach { _, items ->
        repeat(items.size) {i ->
            counts[i] += 1
        }
    }

    var itemNumber = 0
    var groupNumber = TARGET_ITEM_NUMBER
    while(groupNumber > counts[itemNumber]) {
        groupNumber -= counts[itemNumber]
        ++itemNumber
    }
    --groupNumber

    val anglesCache = mutableMapOf<Vector, Double>()
    val targetGroupKey = grouped
        .keys
        .filter {
            grouped.getValue(it).size > itemNumber
        }
        .sortedBy {
            anglesCache.getOrPut(it) {
                location.angleTo(grouped.getValue(it).first())
            }
        }[groupNumber]
    val targetItem = grouped
        .getValue(targetGroupKey)
        .sortedBy {
            location.distanceTo(it)
        }[itemNumber]

    return targetItem.x * 100 + targetItem.y
}

private fun parseAsteroidsMap(asteroidsMap: List<String>): List<XY> {
    var y = -1
    return asteroidsMap.flatMap {row ->
        ++y
        row.mapIndexedNotNull { x, ch ->
            ch.takeIf { it == '#' }?.run { XY(x, y) }
        }
    }
}

private data class Vector private constructor(val x: Int, val y : Int) {
    companion object {
        fun of(from: XY, to: XY): Vector {
            val dX = from.x - to.x
            val dY = from.y - to.y
            val gcd = dX.gcd(dY).absoluteValue

            return Vector(dX / gcd, dY / gcd)
        }
    }
}

private fun XY.angleTo(other: XY): Double {
    val dx = other.x - x
    val dy = y - other.y
    val theta = (PI / 2 - atan2(dy.toDouble(), dx.toDouble())) * 180.0 / PI

    return if (theta < 0) theta + 360.0 else theta
}

private fun XY.distanceTo(other: XY): Int = (x - other.x).absoluteValue + (y - other.y).absoluteValue

