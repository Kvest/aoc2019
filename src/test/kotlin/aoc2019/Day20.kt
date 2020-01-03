package aoc2019

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class Day20 {
    @Test
    fun testDay20_1() {
        Assertions
            .assertThat(23)
            .isEqualTo(
                day20_1(
                    File("./data/test_day20_1_1.txt").readLines()
                )
            )

        Assertions
            .assertThat(58)
            .isEqualTo(
                day20_1(
                    File("./data/test_day20_1_2.txt").readLines()
                )
            )
    }

    @Test
    fun testDay20_2() {
        Assertions
            .assertThat(26)
            .isEqualTo(
                day20_2(
                    File("./data/test_day20_1_1.txt").readLines()
                )
            )

        Assertions
            .assertThat(396)
            .isEqualTo(
                day20_2(
                    File("./data/test_day20_2.txt").readLines()
                )
            )
    }
}