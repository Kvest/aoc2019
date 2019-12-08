package aoc2019

import java.io.File
import java.lang.IllegalStateException

fun main() {
    val result1 = day6_1(File("./data/day6.txt").readLines())
    println(result1)

    val result2 = day6_2(File("./data/day6.txt").readLines())
    println(result2)
}

fun day6_1(params: List<String>): Int {
    val orbits = mutableMapOf<String, MutableSet<String>>()

    //build map of orbits
    params.forEach { param ->
        val (first, second) = param.split(")")
        orbits.getOrPut(first, { mutableSetOf() }).add(second)
    }

    return sumOrbits("COM", orbits, 0)
}

fun day6_2(params: List<String>): Int {
    val orbits = mutableMapOf<String, MutableSet<String>>()

    //build map of orbits(but in opposite order)
    params.forEach { param ->
        val (first, second) = param.split(")")
        orbits.getOrPut(second, { mutableSetOf() }).add(first)
    }

    val youPath = getPathFrom("YOU", orbits)
    val sanPath = getPathFrom("SAN", orbits)

    var i = youPath.size - 1
    var j = sanPath.size - 1

    while(youPath[i] == sanPath[j]) {
        --i
        --j
    }

    return i + j + 2
}

private fun sumOrbits(orbitName: String, orbits: Map<String, MutableSet<String>>, steps: Int): Int {
    val children = orbits[orbitName]

    if (children.isNullOrEmpty()) {
        return steps
    }

    val childrenSum = children.map {
        sumOrbits(it, orbits, steps + 1)
    }.sum()

    return childrenSum + steps
}

private fun getPathFrom(target: String, orbits: Map<String, MutableSet<String>>): List<String> {
    val result = mutableListOf<String>()
    var current = target

    while(current != "COM") {
        val next = orbits[current]?.first() ?: throw IllegalStateException("Orbit $current not found")

        result.add(next)
        current = next
    }

    return result
}