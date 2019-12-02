package aoc2019

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Day2 {
    @Test
    fun testDay2_1() {
        val result = aoc2019.day2_1(listOf("1", "9", "10", "3", "2", "3", "11", "0", "99", "30", "40", "50", "60"))
        assertEquals(3100, result)
    }

    @Test
    fun testDay2_2() {
    }
}