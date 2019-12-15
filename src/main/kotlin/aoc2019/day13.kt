package aoc2019

import java.io.File

fun main() {
    val prog = File("./data/day13.txt")
        .readText()
        .split(",")

    val result1 = day13_1(prog)
    println(result1)

    val result2 = day13_2(prog)
    println(result2)
}

private const val EMPTY = 0
private const val WALL = 1
private const val BLOCK = 2
private const val PADDLE = 3
private const val BALL = 4

fun day13_1(prog: List<String>): Int {
    val intcodes = Intcode(prog)
    intcodes.process()

    var counts = 0
    while (intcodes.hasOutput()) {
        intcodes.getOutput() //x
        intcodes.getOutput() //y
        if (intcodes.getOutput().toInt() == BLOCK) {
            ++counts
        }
    }

    return counts
}

fun day13_2(prog: List<String>): Int {
    val intcodes = Intcode(
        prog.mapIndexed { index, s ->
            if (index == 0) {
                "2"
            } else {
                s
            }
        }
    )

    var score = 0
    var paddleX = 0
    var ballX = 0

    while (!intcodes.isHalted()) {
        intcodes.process()

        while (intcodes.hasOutput()) {
            val x = intcodes.getOutput().toInt()
            val y = intcodes.getOutput().toInt()
            val id = intcodes.getOutput().toInt()

            if (x == -1 && y == 0) {
                score = id
            } else {
                when(id) {
                    BALL -> ballX = x
                    PADDLE -> paddleX = x
                }
            }
        }

        //set action(just follow the ball)
        when {
            ballX > paddleX -> intcodes.addInput(1)
            ballX < paddleX -> intcodes.addInput(-1)
            else -> intcodes.addInput(0)
        }
    }

    return score
}