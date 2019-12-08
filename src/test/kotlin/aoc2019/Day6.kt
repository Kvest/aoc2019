package aoc2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day6 {
    @Test
    fun testDay6_1() {
        val testData = listOf(
            "COM)B",
            "B)C",
            "C)D",
            "D)E",
            "E)F",
            "B)G",
            "G)H",
            "D)I",
            "E)J",
            "J)K",
            "K)L"
        )

        Assertions.assertEquals(42, day6_1(testData))
    }

    @Test
    fun testDay6_2() {
        val testData = listOf(
            "COM)B",
            "B)C",
            "C)D",
            "D)E",
            "E)F",
            "B)G",
            "G)H",
            "D)I",
            "E)J",
            "J)K",
            "K)L",
            "K)YOU",
            "I)SAN"
        )

        Assertions.assertEquals(4, day6_2(testData))
    }
}