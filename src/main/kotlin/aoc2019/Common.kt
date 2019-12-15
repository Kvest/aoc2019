package aoc2019

import kotlin.math.absoluteValue

data class XY private constructor(val x: Int, val y: Int) {
    companion object {
        private val cash = mutableMapOf<Int, MutableMap<Int, XY>>()

        //Avoid allocations of the huge amount of the XY items
        operator fun invoke(x: Int, y: Int): XY {
            val rowsCash = cash.getOrPut(x) { mutableMapOf() }
            return rowsCash.getOrPut(y) { XY(x, y) }
        }
    }
}

data class XYZ(val x: Int, val y: Int, val z: Int)

fun IntArray.permute(onNextPermutation: (IntArray) -> Unit) = permute(this, 0, this.size, onNextPermutation)

//Greatest Common Divisor
fun Int.gcd(other : Int): Int {
    var n1 = this.absoluteValue
    var n2 = other.absoluteValue

    if (n1 == 0 && n2 == 0) {
        return 1
    }

    if (n1 == 0) {
        return n2
    }

    if (n2 == 0) {
        return n1
    }

    while (n1 != n2) {
        if (n1 > n2)
            n1 -= n2
        else
            n2 -= n1
    }

    return n1
}

//Greatest Common Divisor
fun Long.gcd(other : Long): Long {
    var n1 = this.absoluteValue
    var n2 = other.absoluteValue

    if (n1 == 0L && n2 == 0L) {
        return 1L
    }

    if (n1 == 0L) {
        return n2
    }

    if (n2 == 0L) {
        return n1
    }

    while (n1 != n2) {
        if (n1 > n2)
            n1 -= n2
        else
            n2 -= n1
    }

    return n1
}

//Least Common Multiple
fun Int.lcm(other : Int): Int = (this * other) / this.gcd(other)

//Least Common Multiple
fun Long.lcm(other : Long): Long = (this * other) / this.gcd(other)

private fun IntArray.swap(i: Int, j : Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

private fun permute(src: IntArray, l: Int, r: Int, onNextPermutation: (IntArray) -> Unit) {
    if (l == r) {
        onNextPermutation(src)
    } else {
        (l until r).forEach { i ->
            // Swapping done
            src.swap(l, i)

            // Recursion called
            permute(src, l + 1, r, onNextPermutation)

            //backtrack
            src.swap(l, i)
        }
    }
}