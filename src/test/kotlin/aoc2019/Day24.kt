package aoc2019

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class Day24 {
    @Test
    fun testDay24_1() {
        Assertions
            .assertThat(2129920)
            .isEqualTo(
                day24_1(
                    listOf(
                        "....#",
                        "#..#.",
                        "#..##",
                        "..#..",
                        "#...."
                    )
                )
            )
    }

    @Test
    fun testDay24_2() {
        Assertions
            .assertThat(99)
            .isEqualTo(
                day24_2(
                    listOf(
                        "....#",
                        "#..#.",
                        "#..##",
                        "..#..",
                        "#...."
                    ),
                    10
                )
            )
    }
}