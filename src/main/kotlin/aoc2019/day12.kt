package aoc2019

import kotlin.math.absoluteValue

fun main() {
    val x = intArrayOf(0, 4, -11, 2)
    val y = intArrayOf(6, 4, 1, 19)
    val z = intArrayOf(1, 19, 8, 15)

    val result1 = day12_1(x.copyOf(), y.copyOf(), z.copyOf(), 1000)
    println(result1)

    val result2 = day12_2(x.copyOf(), y.copyOf(), z.copyOf())
    println(result2)
}

fun day12_1(x: IntArray, y: IntArray, z: IntArray, steps: Int): Int {
    require(x.size == y.size && y.size == z.size) { "Wrong size" }

    val vx = IntArray(x.size) { 0 }
    val vy = IntArray(x.size) { 0 }
    val vz = IntArray(x.size) { 0 }

    repeat(steps) {
        //update velocity
        (0 until (x.size - 1)).forEach { i ->
            ((i + 1) until x.size).forEach { j ->
                recalcVelocity(x, vx, i, j)
                recalcVelocity(y, vy, i, j)
                recalcVelocity(z, vz, i, j)
            }
        }

        //update coordinates
        (0 until x.size).forEach { i ->
            x[i] += vx[i]
            y[i] += vy[i]
            z[i] += vz[i]
        }
    }

    var energy  = 0
    (0 until x.size).forEach { i ->
        energy += (x[i].absoluteValue + y[i].absoluteValue + z[i].absoluteValue) * (vx[i].absoluteValue + vy[i].absoluteValue + vz[i].absoluteValue)
    }

    return energy
}

fun day12_2(x: IntArray, y: IntArray, z: IntArray): Long {
    val stepsX = findCycle(x)
    val stepsY = findCycle(y)
    val stepsZ = findCycle(z)

    return stepsX.lcm(stepsY).lcm(stepsZ)
}

private fun findCycle(initial: IntArray): Long {
    val arr = initial.copyOf()
    val v = IntArray(arr.size) { 0 }
    var step = 0L

    do {
        //update velocity
        (0 until (arr.size - 1)).forEach { i ->
            ((i + 1) until arr.size).forEach { j ->
                recalcVelocity(arr, v, i, j)
            }
        }

        //update coordinates
        (0 until arr.size).forEach { i ->
            arr[i] += v[i]
        }

        ++step
    } while (!initial.contentEquals(arr) || v.all { it != 0 })

    return step
}

private fun recalcVelocity(coord: IntArray, v: IntArray, i: Int, j : Int) {
    if (coord[i] > coord[j]) {
        v[i]--
        v[j]++
    } else if (coord[i] < coord[j]) {
        v[i]++
        v[j]--
    }
}