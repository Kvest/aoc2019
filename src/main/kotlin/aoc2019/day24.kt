package aoc2019

import java.io.File

fun main() {
    val data = File("./data/day24.txt").readLines()

    val result1 = day24_1(data)
    println(result1)

    val result2 = day24_2(data, 200)
    println(result2)
}

private const val SIZE = 5
private const val COUNT = SIZE * SIZE

fun day24_1(data: List<String>): Int {
    var bugs = 0
    data.forEachIndexed { i, s ->
        s.forEachIndexed { j, ch ->
            if (ch == '#') {
                bugs = bugs or (1 shl (i * SIZE + j))
            }
        }
    }

    val history = mutableSetOf<Int>()
    while(!history.contains(bugs)) {
        history.add(bugs)
        val prev = bugs

        bugs = 0
        for (i in 0 until COUNT) {
            val sum = prev.left(i) + prev.right(i) + prev.top(i) + prev.down(i)

            if ((prev and (1 shl i)) > 0) {
                if (sum == 1) {
                    bugs = bugs or (1 shl i)
                }
            } else {
                if (sum == 1 || sum == 2) {
                    bugs = bugs or (1 shl i)
                }
            }
        }
    }

    return bugs
}

fun day24_2(data: List<String>, iterations: Int): Int {
    var prev = mutableSetOf<XY>()
    var index = 1
    data.forEach { row ->
        row.forEach { ch ->
            if (index != 13 && ch == '#') {
                prev.add(XY(0, index))
            }
            ++index
        }
    }
    var next = mutableSetOf<XY>()

    repeat(iterations) {
        prev.forEach {xy ->
            recalc(xy, prev, next)
            //also need to recalculate for adjacent cells
            xy.neighborCells {
                recalc(it, prev, next)
            }
        }

        prev.clear()
        prev.addAll(next)
        next.clear()
    }

    return prev.size
}

private fun recalc(
    xy: XY,
    prev: MutableSet<XY>,
    next: MutableSet<XY>
) {
    var sum = 0
    xy.neighborCells {
        if (prev.contains(it)) {
            ++sum
        }
    }

    if (prev.contains(xy)) {
        if (sum == 1) {
            next.add(xy)
        }
    } else {
        if (sum == 1 || sum == 2) {
            next.add(xy)
        }
    }
}

private fun Int.left(position: Int): Int {
    if (position % SIZE == 0) {
        return 0
    }

    if (this and (1 shl (position - 1)) > 0) {
        return 1
    } else {
        return 0
    }
}

private fun Int.right(position: Int): Int {
    if ((position + 1) % SIZE == 0) {
        return 0
    }

    if (this and (1 shl (position + 1)) > 0) {
        return 1
    } else {
        return 0
    }
}

private fun Int.top(position: Int): Int {
    if (position < SIZE) {
        return 0
    }

    if (this and (1 shl (position - SIZE)) > 0) {
        return 1
    } else {
        return 0
    }
}

private fun Int.down(position: Int): Int {
    if ((position + SIZE) >= COUNT) {
        return 0
    }

    if (this and (1 shl (position + SIZE)) > 0) {
        return 1
    } else {
        return 0
    }
}

private val NEIGHBORS = arrayOf(
    arrayOf(intArrayOf(-1, 12), intArrayOf(-1, 8), intArrayOf(0, 2), intArrayOf(0, 6)),
    arrayOf(intArrayOf(0, 1), intArrayOf(-1, 8), intArrayOf(0, 3), intArrayOf(0, 7)),
    arrayOf(intArrayOf(0, 2), intArrayOf(-1, 8), intArrayOf(0, 4), intArrayOf(0, 8)),
    arrayOf(intArrayOf(0, 3), intArrayOf(-1, 8), intArrayOf(0, 5), intArrayOf(0, 9)),
    arrayOf(intArrayOf(0, 4), intArrayOf(-1, 8), intArrayOf(-1, 14), intArrayOf(0, 10)),

    arrayOf(intArrayOf(-1, 12), intArrayOf(0, 1), intArrayOf(0, 7), intArrayOf(0, 11)),
    arrayOf(intArrayOf(0, 6), intArrayOf(0, 2), intArrayOf(0, 8), intArrayOf(0, 12)),
    arrayOf(intArrayOf(0, 7), intArrayOf(0, 3), intArrayOf(0, 9), intArrayOf(1, 1), intArrayOf(1, 2), intArrayOf(1, 3), intArrayOf(1, 4), intArrayOf(1, 5)),
    arrayOf(intArrayOf(0, 8), intArrayOf(0, 4), intArrayOf(0, 10), intArrayOf(0, 14)),
    arrayOf(intArrayOf(0, 9), intArrayOf(0, 5), intArrayOf(-1, 14), intArrayOf(0, 15)),

    arrayOf(intArrayOf(-1, 12), intArrayOf(0, 6), intArrayOf(0, 12), intArrayOf(0, 16)),
    arrayOf(intArrayOf(0, 11), intArrayOf(0, 7), intArrayOf(1, 1), intArrayOf(1, 6), intArrayOf(1, 11), intArrayOf(1, 16), intArrayOf(1, 21), intArrayOf(0, 17)),
    arrayOf(),
    arrayOf(intArrayOf(1, 5), intArrayOf(1, 10), intArrayOf(1, 15), intArrayOf(1, 20), intArrayOf(1, 25), intArrayOf(0, 9), intArrayOf(0, 15), intArrayOf(0, 19)),
    arrayOf(intArrayOf(0, 14), intArrayOf(0, 10), intArrayOf(-1, 14), intArrayOf(0, 20)),

    arrayOf(intArrayOf(-1, 12), intArrayOf(0, 11), intArrayOf(0, 17), intArrayOf(0, 21)),
    arrayOf(intArrayOf(0, 16), intArrayOf(0, 12), intArrayOf(0, 18), intArrayOf(0, 22)),
    arrayOf(intArrayOf(0, 17), intArrayOf(1, 21), intArrayOf(1, 22), intArrayOf(1, 23), intArrayOf(1, 24), intArrayOf(1, 25), intArrayOf(0, 19), intArrayOf(0, 23)),
    arrayOf(intArrayOf(0, 18), intArrayOf(0, 14), intArrayOf(0, 20), intArrayOf(0, 24)),
    arrayOf(intArrayOf(0, 19), intArrayOf(0, 15), intArrayOf(-1, 14), intArrayOf(0, 25)),

    arrayOf(intArrayOf(-1, 12), intArrayOf(0, 16), intArrayOf(0, 22), intArrayOf(-1, 18)),
    arrayOf(intArrayOf(0, 21), intArrayOf(0, 17), intArrayOf(0, 23), intArrayOf(-1, 18)),
    arrayOf(intArrayOf(0, 22), intArrayOf(0, 18), intArrayOf(0, 24), intArrayOf(-1, 18)),
    arrayOf(intArrayOf(0, 23), intArrayOf(0, 19), intArrayOf(0, 25), intArrayOf(-1, 18)),
    arrayOf(intArrayOf(0, 24), intArrayOf(0, 20), intArrayOf(-1, 14), intArrayOf(-1, 18))
)

private fun XY.neighborCells(action: (XY) -> Unit) {
    NEIGHBORS[y - 1].forEach {
        action(XY(this.x + it[0], it[1]))
    }
}