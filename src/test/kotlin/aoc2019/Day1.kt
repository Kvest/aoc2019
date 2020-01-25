package aoc2019

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Day1 {
    @Test
    fun testDay1_1() {
        val result = aoc2019.day1_1(listOf("12", "14", "1969", "100756"))
        assertEquals(2 + 2 + 654 + 33583, result)
    }

    @Test
    fun testDay1_2() {
        val result = aoc2019.day1_2(listOf("12", "14", "1969", "100756"))
        assertEquals(2 + 2 + 966 + 50346, result)
    }

    @Test
    fun testFail() {
        assertEquals(2 + 2, 5)
    }
}

