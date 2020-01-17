package aoc2019

import java.io.File

fun main() {
    val prog = File("./data/day25.txt")
        .readText()
        .split(",")

    day25_1(prog)
}

fun day25_1(prog: List<String>) {
    val intcode = Intcode(prog)

    //collect all needed items
    //NOTE: this sequence was found manually
    intcode.command("west")
    intcode.command("take cake")
    intcode.command("west")
    intcode.command("south")
    intcode.command("take monolith")
    intcode.command("north")
    intcode.command("west")
    intcode.command("south")
    intcode.command("east")
    intcode.command("east")
    intcode.command("east")
    intcode.command("take mug")
    intcode.command("west")
    intcode.command("west")
    intcode.command("west")
    intcode.command("north")
    intcode.command("east")
    intcode.command("east")
    intcode.command("east")
    intcode.command("south")
    intcode.command("take coin")
    intcode.command("south")
    intcode.command("west")
    intcode.command("north")
    intcode.command("north")

    //clear output
    intcode.process()
    while (intcode.hasOutput()) {
        intcode.getOutput()
    }

    //final step
    intcode.command("north")
    intcode.process()
    while (intcode.hasOutput()) {
        print(intcode.getOutput().toInt().toChar())
    }
}

private fun Intcode.command(cmd: String) {
    cmd.forEach {
        this.addInput(it.toInt())
    }
    this.addInput(10)
}