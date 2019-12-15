package aoc2019

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day12 {
    @Test
    fun testDay12_1() {
        assertEquals(
            179,
            day12_1(
                intArrayOf(-1, 2, 4, 3),
                intArrayOf(0, -10, -8, 5),
                intArrayOf(2, -7, 8, -1),
                10
            )
        )

        assertEquals(
            1940,
            day12_1(
                intArrayOf(-8, 5, 2, 9),
                intArrayOf(-10, 5, -7, -8),
                intArrayOf(0, 10, 3, -3),
                100
            )
        )
    }

    @Test
    fun testDay12_2() {
        assertEquals(
            2772,
            day12_2(
                intArrayOf(-1, 2, 4, 3),
                intArrayOf(0, -10, -8, 5),
                intArrayOf(2, -7, 8, -1)
            )
        )

        assertEquals(
            4686774924L,
            day12_2(
                intArrayOf(-8, 5, 2, 9),
                intArrayOf(-10, 5, -7, -8),
                intArrayOf(0, 10, 3, -3)
            )
        )
    }
}