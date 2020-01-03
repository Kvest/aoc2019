package aoc2019

import java.io.File
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.min

fun main() {
    val data = File("./data/day20.txt").readLines()

    val result1 = day20_1(data)
    println(result1)

    val result2 = day20_2(data)
    println(result2)
}

fun day20_1(data: List<String>): Int {
    fun Map<XY, Char>.getLeft(xy: XY): String =
        this.getOrDefault(XY(xy.x, xy.y - 2), ' ').toString() + this.getOrDefault(XY(xy.x, xy.y - 1), ' ')

    fun Map<XY, Char>.getRight(xy: XY): String =
        this.getOrDefault(XY(xy.x, xy.y + 1), ' ').toString() + this.getOrDefault(XY(xy.x, xy.y + 2), ' ')

    fun Map<XY, Char>.getTop(xy: XY): String =
        this.getOrDefault(XY(xy.x - 2, xy.y), ' ').toString() + this.getOrDefault(XY(xy.x - 1, xy.y), ' ')

    fun Map<XY, Char>.getBottom(xy: XY): String =
        this.getOrDefault(XY(xy.x + 1, xy.y), ' ').toString() + this.getOrDefault(XY(xy.x + 2, xy.y), ' ')

    fun String.isLabel() = this.all { it.isLetter() }

    val maze = parseData(data)

    lateinit var start: XY
    lateinit var end: XY
    val halfTeleports = mutableMapOf<String, XY>()
    val teleports = mutableMapOf<XY, XY>()

    maze.forEach { xy, ch ->
        if (ch == '.') {
            listOf(maze.getLeft(xy), maze.getRight(xy), maze.getTop(xy), maze.getBottom(xy))
                .filter { it.isLabel() }
                .forEach {
                    when (it) {
                        "AA" -> start = xy
                        "ZZ" -> end = xy
                        else -> {
                            if (halfTeleports.containsKey(it)) {
                                val other = halfTeleports.getValue(it)
                                teleports[xy] = other
                                teleports[other] = xy
                            } else {
                                halfTeleports[it] = xy
                            }
                        }
                    }
                }
        }
    }

    val queue: Queue<XY> = LinkedList<XY>()
    queue.offer(start)
    val values = mutableMapOf(start to 0)

    var result: Int? = null
    while (true) {
        val xy = queue.poll()
        val stepsCount = values.getValue(xy) + 1

        var result: Int? = null

        xy.neighbors { n ->
            //direct neighbors
            if (maze[n] == '.' && !values.containsKey(n)) {
                if (n == end) {
                    result = stepsCount
                } else {
                    queue.add(n)
                    values[n] = stepsCount
                }
            }
        }

        result?.let {
            return it
        }

        //if teleport is available
        teleports[xy]?.let {
            if (it == end) {
                return stepsCount
            }

            if (!values.containsKey(it)) {
                queue.add(it)
                values[it] = stepsCount
            }
        }
    }
}

fun day20_2(data: List<String>): Int {
    val outer_i_start = if (data[0].any { it.isLetter() }) 2 else 0
    val outer_i_end = if (data.last().any { it.isLetter() }) (data.size - 3) else (data.size - 1)
    val outer_i = outer_i_start..outer_i_end

    val outer_j_start = if (data.any { it[0].isLetter() }) 2 else 0
    val outer_j_end = data.map { it.length - if (it.last().isLetter()) 3 else 1 }.max() ?: throw IllegalStateException("outer_j_end not found")
    val outer_j = outer_j_start..outer_j_end//((data.maxBy { it.length }?.length ?: 0) - 3)

    val inner_i =
        findInnerI(data, (outer_i.endInclusive - outer_i.start) / 2, (outer_j.endInclusive - outer_j.start) / 2)
    val inner_j =
        findInnerJ(data, (outer_i.endInclusive - outer_i.start) / 2, (outer_j.endInclusive - outer_j.start) / 2)

    val teleports = mutableMapOf<XY, Teleport>()

    //outer teleports
    for (i in outer_i) {
        if (data[i][outer_j.start] == '.') {
            val label = data[i].substring(0, outer_j.start)
            teleports[XY(i, outer_j.start)] = Teleport(label, -1)
        }
        if (data[i][outer_j.endInclusive] == '.') {
            val label = data[i].substring(outer_j.endInclusive + 1)
            teleports[XY(i, outer_j.endInclusive)] = Teleport(label, -1)
        }
    }
    for (j in outer_j) {
        if (data[outer_i.start][j] == '.') {
            val label = data[outer_i.start - 2][j].toString() + data[outer_i.start - 1][j]
            teleports[XY(outer_i.start, j)] = Teleport(label, -1)
        }
        if (data[outer_i.endInclusive][j] == '.') {
            val label = data[outer_i.endInclusive + 1][j].toString() + data[outer_i.endInclusive + 2][j]
            teleports[XY(outer_i.endInclusive, j)] = Teleport(label, -1)
        }
    }

    //inner teleports
    for (i in inner_i) {
        if (data[i][inner_j.start] == '.') {
            val label = data[i].substring(inner_j.start + 1, inner_j.start + 3)
            teleports[XY(i, inner_j.start)] = Teleport(label, 1)
        }
        if (data[i][inner_j.endInclusive] == '.') {
            val label = data[i].substring(inner_j.endInclusive - 2, inner_j.endInclusive)
            teleports[XY(i, inner_j.endInclusive)] = Teleport(label, 1)
        }
    }
    for (j in inner_j) {
        if (data[inner_i.start][j] == '.') {
            val label = data[inner_i.start + 1][j].toString() + data[inner_i.start + 2][j]
            teleports[XY(inner_i.start, j)] = Teleport(label, 1)
        }
        if (data[inner_i.endInclusive][j] == '.') {
            val label = data[inner_i.endInclusive - 2][j].toString() + data[inner_i.endInclusive - 1][j]
            teleports[XY(inner_i.endInclusive, j)] = Teleport(label, 1)
        }
    }

    //build teleports connections
    val connections = mutableMapOf<Teleport, Set<Pair<Teleport, Int>>>()
    teleports.forEach { xy, teleport ->
        connections[teleport] = findConnections(xy, data, teleports)
    }

    //finally look for the path))
    var result = Int.MAX_VALUE
    val queue: Queue<Triple<Teleport, Int, Int>> = LinkedList() //first - teleport, second - level, third - made steps

    //start point
    val startTeleport = teleports.values.first { it.name == "AA" }
    queue.add(Triple(startTeleport, 0, 0))

    while (queue.isNotEmpty()) {
        val (currentTeleport, level, stepsMade) = queue.poll()
        if (stepsMade > result) {
            continue
        }

        val possibleWays = connections.getValue(currentTeleport)
        possibleWays.forEach { (nextTeleport, distance) ->
            //check if target available
            if (nextTeleport.name == "ZZ" && level == 0) {
                result = min(result, stepsMade + distance)
                return@forEach
            }

            //skip not suitable teleports
            if (nextTeleport.name == "AA" ||
                nextTeleport.name == "ZZ" ||
                (level == 0 && nextTeleport.d < 0)
            ) {
                return@forEach
            }

            val newDistance = stepsMade + distance + 1
            queue.add(Triple(nextTeleport.getOther(), level + nextTeleport.d, newDistance))
        }
    }

    return result
}

private fun findConnections(
    xy: XY,
    data: List<String>,
    teleports: Map<XY, Teleport>
): Set<Pair<Teleport, Int>> {
    val queue: Queue<XY> = LinkedList<XY>()
    queue.offer(xy)
    val values = mutableMapOf(xy to 0)
    val result = mutableSetOf<Pair<Teleport, Int>>()

    while (queue.isNotEmpty()) {
        val next = queue.poll()
        val stepsCount = values.getValue(next) + 1
        if (next != xy && teleports.containsKey(next)) {
            val connection = teleports.getValue(next)
            result.add(connection to (stepsCount - 1))
        }

        next.neighbors { n ->
            if (data[n.x][n.y] == '.' && !values.containsKey(n)) {
                values[n] = stepsCount
                queue.offer(n)
            }
        }
    }

    return result
}

private fun findInnerI(data: List<String>, center_i: Int, center_j: Int): IntRange {
    var start = center_i
    var end = center_i
    while (data[start][center_j] != '.' && data[start][center_j] != '#') {
        --start
    }
    while (data[end][center_j] != '.' && data[end][center_j] != '#') {
        ++end
    }

    return start..end
}

private fun findInnerJ(data: List<String>, center_i: Int, center_j: Int): IntRange {
    var start = center_j
    var end = center_j
    while (data[center_i][start] != '.' && data[center_i][start] != '#') {
        --start
    }
    while (data[center_i][end] != '.' && data[center_i][end] != '#') {
        ++end
    }

    return start..end
}

private fun parseData(data: List<String>): Map<XY, Char> {
    val result = mutableMapOf<XY, Char>()
    data.forEachIndexed { i, row ->
        row.forEachIndexed { j, ch ->
            result[XY(i, j)] = ch
        }
    }

    return result
}

private data class Teleport(val name: String, val d: Int) {
    fun getOther() = this.copy(d = -d)
}