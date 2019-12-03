package aoc2019

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Day3 {
    @Test
    fun testDay3_1() {
        assertEquals(
            6,
            day3_1("R8,U5,L5,D3", "U7,R6,D4,L4")
        )
        assertEquals(
            159,
            day3_1("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
        )
        assertEquals(
            135,
            day3_1("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        )
    }

    @Test
    fun testDay3_2() {
        assertEquals(
            30,
            day3_2("R8,U5,L5,D3", "U7,R6,D4,L4")
        )
        assertEquals(
            610,
            day3_2("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
        )
        assertEquals(
            410,
            day3_2("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        )
    }
}