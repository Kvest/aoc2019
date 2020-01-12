package aoc2019

import java.io.File
import java.lang.IllegalStateException
import java.math.BigInteger

fun main() {
    val rules = File("./data/day22.txt").readLines()

    val result1 = day22_1(2019, 10007, rules)
    println(result1)

    val result2 = day22_2(119315717514047, 101741582076661, 2020, rules)
    println(result2)
}

fun day22_1(card: Int, deckSize: Int,  rules: List<String>): Int {
    val (a, b) = rules.collapseRules(deckSize)

    return Math.floorMod(a * card + b, deckSize)
}

//Used approach: https://codeforces.com/blog/entry/72593
private fun List<String>.collapseRules(m: Int): Pair<Int, Int> = this.map { rule ->
    when {
        rule == "deal into new stack" -> Pair(-1, -1)
        rule.startsWith("cut") -> {
            val n = rule.substringAfterLast(" ").toInt()
            Pair(1, -n)
        }
        rule.startsWith("deal with increment") -> {
            val n = rule.substringAfterLast(" ").toInt()
            Pair(n, 0)
        }
        else -> throw IllegalStateException("Unknown operation")
    }
}.fold(Pair(1, 0)) { (a, b), (c, d) ->
    Pair(Math.floorMod(a * c, m), Math.floorMod(b * c + d, m))
}

fun day22_2(deckSize: Long, k : Long, targetPosition: Long, rules: List<String>): Long {
    //Used approach: https://github.com/mebeim/aoc/blob/master/2019/README.md#day-22---slam-shuffle
    val repetitions = k.toBigInteger()
    var start = BigInteger.ZERO
    var step = BigInteger.ONE
    val size = deckSize.toBigInteger()

    //collapse rules
    rules.forEach { rule ->
        when {
            rule == "deal into new stack" -> {
                start = (start - step).mod(size)
                step = (-step).mod(size)
            }
            rule.startsWith("cut") -> {
                var n = rule.substringAfterLast(" ").toBigInteger()
                if (n < BigInteger.ZERO) {
                    n += size
                }
                start = (start + step * n).mod(size)
            }
            rule.startsWith("deal with increment") -> {
                val n = rule.substringAfterLast(" ").toBigInteger()
                step = (step * n.modPow(size - BigInteger.TWO, size)).mod(size)
            }
        }
    }

    //count params after repetition
    val finalStep = step.modPow(repetitions, size)
    val finalStart = (start * (BigInteger.ONE - finalStep) * (BigInteger.ONE - step).modPow(size - BigInteger.TWO, size)).mod(size)

    val result = (finalStart + finalStep * targetPosition.toBigInteger()).mod(size)

    return result.toLong()
}