package aoc2019

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class Day16 {
    @Test
    fun testDay16_1() {
        assertEquals(
            "24176176",
            day16_1(
                "80871224585914546619083218645595".map { it.toString().toInt() }
            )
        )

        assertEquals(
            "73745418",
            day16_1(
                "19617804207202209144916044189917".map { it.toString().toInt() }
            )
        )

        assertEquals(
            "52432133",
            day16_1(
                "69317163492948606335995924319873".map { it.toString().toInt() }
            )
        )
    }

    @Test
    fun testDay16_2() {
        assertEquals(
            "84462026",
            day16_2(
                "03036732577212944063491565474664".map { it.toString().toInt() }
            )
        )

        assertEquals(
            "78725270",
            day16_2(
                "02935109699940807407585447034323".map { it.toString().toInt() }
            )
        )

        assertEquals(
            "53553731",
            day16_2(
                "03081770884921959731165446850517".map { it.toString().toInt() }
            )
        )
    }
}