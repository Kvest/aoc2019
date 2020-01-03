package aoc2019

import java.io.File

fun main() {
    val prog = File("./data/day21.txt")
        .readText()
        .split(",")

    val result1 = day21_1(prog)
    println(result1)

    val result2 = day21_2(prog)
    println(result2)
}

fun day21_1(prog: List<String>): Int {
    val intcodes = Intcode(prog)
    intcodes.process()

    //prompt
    while (intcodes.hasOutput()) {
        print(intcodes.getOutput().toInt().toChar())
    }

    //Rule: (!A || !B || !C) && D
    """
        |NOT A J
        |NOT B T
        |OR T J
        |NOT C T
        |OR T J
        |AND D J
        |WALK
        |
    """.trimMargin().forEach {
        intcodes.addInput(it.toInt())
    }
    intcodes.process()

    var result = 0
    while (intcodes.hasOutput()) {
        result = intcodes.getOutput().toInt()
    }

    return result
}

fun day21_2(prog: List<String>): Int {
    val intcodes = Intcode(prog)
    intcodes.process()

    //prompt
    while (intcodes.hasOutput()) {
        print(intcodes.getOutput().toInt().toChar())
    }

    //Rule: (!A || !B || !C) && D && (E || H)
    """
        |NOT A J
        |NOT B T
        |OR T J
        |NOT C T
        |OR T J
        |AND D J
        |AND E T
        |OR H T
        |AND T J
        |RUN
        |
    """.trimMargin().forEach {
        intcodes.addInput(it.toInt())
    }
    intcodes.process()

    var result = 0
    while (intcodes.hasOutput()) {
        result = intcodes.getOutput().toInt()
    }

    return result
}