package aoc2019

fun main() {
    val result1 = day4_1(172930, 683082)
    println(result1)

    val result2 = day4_2(172930, 683082)
    println(result2)
}

fun day4_1(start: Int, end: Int): Int {
    var count = 0

    (start..end).forEach { password ->
        if (isPasswordValid(password)) {
            ++count
        }
    }

    return count
}

fun day4_2(start: Int, end: Int): Int {
    var count = 0

    (start..end).forEach { password ->
        if (isPasswordValid2(password)) {
            ++count
        }
    }

    return count
}

private fun isPasswordValid(password: Int): Boolean {
    var last = password % 10
    var value = password / 10
    var hasPair = false
    while(value > 0) {
        val next = value % 10

        if (next > last) {
            return false
        }

        if (next == last) {
            hasPair = true
        }

        last = next
        value /= 10
    }

    return hasPair
}

private fun isPasswordValid2(password: Int): Boolean {
    var last = password % 10
    var value = password / 10
    var hasPair = false
    var count = 1

    while(value > 0) {
        val next = value % 10

        if (next > last) {
            return false
        }

        if (next == last) {
            ++count
        } else {
            hasPair = hasPair or (count == 2)
            count = 1
        }

        last = next
        value /= 10
    }

    return hasPair or (count == 2)
}