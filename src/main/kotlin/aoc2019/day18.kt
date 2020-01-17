package aoc2019

import java.io.File
import java.util.*
import kotlin.Comparator

fun main() {
    val data = File("./data/day18.txt").readLines()
    val map = Array(data.size) {
        data[it].toCharArray()
    }

    val result1 = day18_1(map)
    println(result1)

    val result2 = day18_2(map)
    println(result2)
}

private const val KEY_START = 'a'.toInt()
private const val KEY_COUNT = 'z'.toInt() - KEY_START + 1
private fun Char.toKeyIndex() = this.toInt() - KEY_START
private fun Int.toKey() = (this + KEY_START).toChar()
/**
 * Method return the list of the keys, that can be taken now
 */
private val delta = listOf(XY(1, 0), XY(-1, 0), XY(0, 1), XY(0, -1))

fun day18_1(map: Array<CharArray>): Int {
    val allKeys = mutableSetOf<Int>()
    val keyXY = mutableMapOf<Int, XY>()
    var startI = 0
    var startJ = 0

    map.forEachIndexed { i, row ->
        row.forEachIndexed { j, ch ->
            when {
                ch == '@' -> {
                    startI = i
                    startJ = j
                }
                ch.isLetter() && ch.isLowerCase() -> {
                    val index = ch.toKeyIndex()
                    allKeys.add(index)
                    keyXY[index] = XY(i, j)
                }
            }
        }
    }

    val distances = Array(KEY_COUNT) { IntArray(KEY_COUNT) { 0 } }
    val requiredKeys = Array(KEY_COUNT) { IntArray(KEY_COUNT) { 0 } }
    keyXY.forEach { key, xy ->
        calcDistances(map, key, xy.x, xy.y, distances, requiredKeys)
    }

    var result = Int.MAX_VALUE

    val allKeysMask = allKeys.fold(0) { acc, index -> acc or (1 shl index) }
    val queue = PriorityQueue<Triple<Int, Int, Int>>(object : Comparator<Triple<Int, Int, Int>> {
        override fun compare(o1: Triple<Int, Int, Int>, o2: Triple<Int, Int, Int>): Int {
            return o1.third - o2.third
        }
    })

    val startKeys = getStartKeys(map, startI, startJ)
    startKeys.forEach { (keyIndex, distance) ->
        queue.add(Triple(keyIndex, 1 shl keyIndex, distance))
    }

    while (queue.isNotEmpty()) {
        val (keyIndex, mask, distance) = queue.poll()
        if (distance > result) {
            continue
        }

        if (mask == allKeysMask) {
            //because we already checked earlier - at this point distance is less or equal to best known "result"
            result = distance
            continue
        }

        allKeys.forEach { toKeyIndex ->
            //if the key is already taken - skip it
            if (mask and (1 shl toKeyIndex) > 0) {
                return@forEach
            }

            //check if all requirements are met
            val required = requiredKeys[keyIndex][toKeyIndex]
            if ((mask and required) == required) {
                val steps = distance + distances[keyIndex][toKeyIndex]
                //add to queue only if the potential steps count are less then the current known result
                if (steps < result) {
                    val newMask = mask or (1 shl toKeyIndex)
                    val oldItem = queue.find { it.first == toKeyIndex && it.second == newMask }

                    if (oldItem != null) {
                        if (oldItem.third > steps) {
                            queue.remove(oldItem)
                            queue.add(Triple(toKeyIndex, newMask, steps))
                        }
                    } else {
                        queue.add(Triple(toKeyIndex, newMask, steps))
                    }
                }
            }
        }
    }

    return result
}

fun day18_2(map: Array<CharArray>): Int {
    adjustMap(map)

    val allKeys = mutableSetOf<Int>()
    val keyXY = mutableMapOf<Int, XY>()
    var starts = mutableListOf<XY>()

    map.forEachIndexed { i, row ->
        row.forEachIndexed { j, ch ->
            when {
                ch == '@' -> {
                    starts.add(XY(i, j))
                }
                ch.isLetter() && ch.isLowerCase() -> {
                    val index = ch.toKeyIndex()
                    allKeys.add(index)
                    keyXY[index] = XY(i, j)
                }
            }
        }
    }

    val distances = Array(KEY_COUNT) { IntArray(KEY_COUNT) { 0 } }
    val requiredKeys = Array(KEY_COUNT) { IntArray(KEY_COUNT) { 0 } }
    keyXY.forEach { key, xy ->
        calcDistances(map, key, xy.x, xy.y, distances, requiredKeys)
    }

    var result = Int.MAX_VALUE

    val allKeysMask = allKeys.fold(0) { acc, index -> acc or (1 shl index) }
    val queue = PriorityQueue<Triple<IntArray, Int, Int>>(object : Comparator<Triple<IntArray, Int, Int>> {
        override fun compare(o1: Triple<IntArray, Int, Int>, o2: Triple<IntArray, Int, Int>): Int {
            return o1.third - o2.third
        }
    })

    val count = starts.size
    starts.forEachIndexed { index, xy ->
        val startKeys = getStartKeys(map, xy.x, xy.y)

        startKeys.forEach {(keyIndex, distance) ->
            val keyIndexes = IntArray(count) { KEY_COUNT }
            keyIndexes[index] = keyIndex

            queue.add(Triple(keyIndexes, 1 shl keyIndex, distance))
        }
    }

    while (queue.isNotEmpty()) {
        val (keyIndexes, mask, distance) = queue.poll()
        if (distance > result) {
            continue
        }

        if (mask == allKeysMask) {
            //because we already checked earlier - at this point distance is less or equal to best known "result"
            result = distance
            continue
        }

        keyIndexes.forEachIndexed { i, keyIndex ->
            if (keyIndex < KEY_COUNT) {
                allKeys.forEach { toKeyIndex ->
                    //if the key is already taken - skip it
                    if (mask and (1 shl toKeyIndex) > 0) {
                        return@forEach
                    }

                    //check if all requirements are met
                    val required = requiredKeys[keyIndex][toKeyIndex]
                    val isConnected = distances[keyIndex][toKeyIndex] > 0
                    if (isConnected && (mask and required) == required) {
                        val steps = distance + distances[keyIndex][toKeyIndex]
                        //add to queue only if the potential steps count are less then the current known result
                        if (steps < result) {
                            val newKeyIndexes = keyIndexes.copyOf()
                            newKeyIndexes[i] = toKeyIndex

                            val newMask = mask or (1 shl toKeyIndex)
                            val oldItem = queue.find { it.first.contentEquals(newKeyIndexes) && it.second == newMask }
                            if (oldItem != null) {
                              //  println("!!!!!!!!!!!!!!")
                            }

                            if (oldItem != null) {
                                if (oldItem.third > steps) {
                                    queue.remove(oldItem)
                                    queue.add(Triple(newKeyIndexes, newMask, steps))
                                }
                            } else {
                                queue.add(Triple(newKeyIndexes, newMask, steps))
                            }
                        }
                    }
                }
            } else {
                //key is in start position yet
                val xy = starts[i]
                val startKeys = getStartKeys(map, xy.x, xy.y, mask = mask)

                startKeys.forEach {(keyIndex, dst) ->
                    val newKeyIndexes = keyIndexes.copyOf()
                    newKeyIndexes[i] = keyIndex

                    queue.add(Triple(newKeyIndexes, mask or (1 shl keyIndex), distance + dst))
                }
            }
        }
    }

    return result
}

private fun adjustMap(map: Array<CharArray>) {
    var startI = 0
    var startJ = 0
    map.forEachIndexed { i, row ->
        row.forEachIndexed { j, ch ->
            if (ch == '@') {
                startI = i
                startJ = j
            }
        }
    }

    map[startI][startJ] = '#'
    map[startI][startJ + 1] = '#'
    map[startI][startJ - 1] = '#'
    map[startI + 1][startJ] = '#'
    map[startI - 1][startJ] = '#'

    map[startI + 1][startJ + 1] = '@'
    map[startI + 1][startJ - 1] = '@'
    map[startI - 1][startJ + 1] = '@'
    map[startI - 1][startJ - 1] = '@'
}

private fun getStartKeys(map: Array<CharArray>, startI: Int, startJ: Int, mask: Int = 0): List<Pair<Int, Int>> {
    val queue: Queue<XY> = LinkedList<XY>()
    queue.offer(XY(startI, startJ))
    val values = mutableMapOf(XY(startI, startJ) to 0)
    val result = mutableListOf<Pair<Int, Int>>()

    while (queue.isNotEmpty()) {
        val xy = queue.poll()
        val stepsCount = values.getValue(xy) + 1

        delta.forEach { (dx, dy) ->
            if (map.canStep(xy.x + dx, xy.y + dy)) {
                val dst = XY(xy.x + dx, xy.y + dy)

                if (!values.containsKey(dst)) {
                    val ch = map[dst.x][dst.y]
                    if (ch.isLetter()) {
                        if (ch.isLowerCase()) {
                            result.add(ch.toKeyIndex() to stepsCount)
                        } else if (mask and (1 shl ch.toLowerCase().toKeyIndex()) > 0) {
                            values[dst] = stepsCount
                            queue.add(dst)
                        }
                    } else {
                        values[dst] = stepsCount
                        queue.add(dst)
                    }
                }
            }
        }
    }

    return result
}

private fun calcDistances(
    map: Array<CharArray>,
    fromKeyIndex: Int,
    keyI: Int,
    keyJ: Int,
    distances: Array<IntArray>,
    requiredKeys: Array<IntArray>
) {
    val queue: Queue<XY> = LinkedList<XY>()
    queue.offer(XY(keyI, keyJ))
    val values = mutableMapOf(XY(keyI, keyJ) to 0)
    val gates = mutableMapOf(XY(keyI, keyJ) to emptySet<Char>())

    while (queue.isNotEmpty()) {
        val xy = queue.poll()
        val stepsCount = values.getValue(xy) + 1
        val g = gates.getValue(xy)

        delta.forEach { (dx, dy) ->
            if (map.canStep(xy.x + dx, xy.y + dy)) {
                val dst = XY(xy.x + dx, xy.y + dy)

                if (!values.containsKey(dst)) {
                    val ch = map[dst.x][dst.y]

                    values[dst] = stepsCount
                    queue.add(dst)

                    if (ch.isLetter()) {
                        if (ch.isLowerCase()) {
                            gates[dst] = g
                            distances[fromKeyIndex][ch.toKeyIndex()] = stepsCount
                            requiredKeys[fromKeyIndex][ch.toKeyIndex()] = g.fold(0) { acc, aKey -> acc or (1 shl aKey.toKeyIndex()) }

                            //put a key as a requirement in order not to jump over the key
                            gates[dst] = g + ch
                        } else {
                            gates[dst] = g + ch.toLowerCase()
                        }
                    } else {
                        gates[dst] = g
                    }
                }
            }
        }
    }
}

private fun Array<CharArray>.canStep(i: Int, j: Int): Boolean {
    if (i < 0 || i >= this.size || j < 0 || j >= this[i].size) {
        return false
    }

    if (this[i][j] == '#') {
        return false
    }

    return true
}