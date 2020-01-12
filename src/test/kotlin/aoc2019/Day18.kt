package aoc2019

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

class Day18 {
    @Test
    fun testDay18_1() {
        assertThat(8).isEqualTo(
            day18_1(
                arrayOf(
                    "#########".toCharArray(),
                    "#b.A.@.a#".toCharArray(),
                    "#########".toCharArray()
                )
            )
        )

        assertThat(86).isEqualTo(
            day18_1(
                arrayOf(
                    "########################".toCharArray(),
                    "#f.D.E.e.C.b.A.@.a.B.c.#".toCharArray(),
                    "######################.#".toCharArray(),
                    "#d.....................#".toCharArray(),
                    "########################".toCharArray()
                )
            )
        )

        assertThat(132).isEqualTo(
            day18_1(
                arrayOf(
                    "########################".toCharArray(),
                    "#...............b.C.D.f#".toCharArray(),
                    "#.######################".toCharArray(),
                    "#.....@.a.B.c.d.A.e.F.g#".toCharArray(),
                    "########################".toCharArray()
                )
            )
        )

        assertThat(136).isEqualTo(
            day18_1(
                arrayOf(
                    "#################".toCharArray(),
                    "#i.G..c...e..H.p#".toCharArray(),
                    "########.########".toCharArray(),
                    "#j.A..b...f..D.o#".toCharArray(),
                    "########@########".toCharArray(),
                    "#k.E..a...g..B.n#".toCharArray(),
                    "########.########".toCharArray(),
                    "#l.F..d...h..C.m#".toCharArray(),
                    "#################".toCharArray()
                )
            )
        )

        assertThat(81).isEqualTo(
            day18_1(
                arrayOf(
                    "########################".toCharArray(),
                    "#@..............ac.GI.b#".toCharArray(),
                    "###d#e#f################".toCharArray(),
                    "###A#B#C################".toCharArray(),
                    "###g#h#i################".toCharArray(),
                    "########################".toCharArray()
                )
            )
        )
    }

    @Test
    fun testDay18_2() {
        assertThat(8).isEqualTo(
            day18_2(
                arrayOf(
                    "#######".toCharArray(),
                    "#a.#Cd#".toCharArray(),
                    "##...##".toCharArray(),
                    "##.@.##".toCharArray(),
                    "##...##".toCharArray(),
                    "#cB#Ab#".toCharArray(),
                    "#######".toCharArray()
                )
            )
        )

        assertThat(24).isEqualTo(
            day18_2(
                arrayOf(
                    "###############".toCharArray(),
                    "#d.ABC.#.....a#".toCharArray(),
                    "######...######".toCharArray(),
                    "######.@.######".toCharArray(),
                    "######...######".toCharArray(),
                    "#b.....#.....c#".toCharArray(),
                    "###############".toCharArray()
                )
            )
        )
        assertThat(32).isEqualTo(
            day18_2(
                arrayOf(
                    "#############".toCharArray(),
                    "#DcBa.#.GhKl#".toCharArray(),
                    "#.###...#I###".toCharArray(),
                    "#e#d#.@.#j#k#".toCharArray(),
                    "###C#...###J#".toCharArray(),
                    "#fEbA.#.FgHi#".toCharArray(),
                    "#############".toCharArray()
                )
            )
        )
        assertThat(72).isEqualTo(
            day18_2(
                arrayOf(
                    "#############".toCharArray(),
                    "#g#f.D#..h#l#".toCharArray(),
                    "#F###e#E###.#".toCharArray(),
                    "#dCba...BcIJ#".toCharArray(),
                    "#####.@.#####".toCharArray(),
                    "#nK.L...G...#".toCharArray(),
                    "#M###N#H###.#".toCharArray(),
                    "#o#m..#i#jk.#".toCharArray(),
                    "#############".toCharArray()
                )
            )
        )
    }
}