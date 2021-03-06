package aoc2019

import java.lang.IllegalStateException
import java.math.BigInteger
import java.util.*

private val HALT = BigInteger("99")
class Intcode(initialMemory: List<String>) {
    private val memory = mutableMapOf<Int, BigInteger>()
    private val input: Queue<BigInteger> = LinkedList<BigInteger>()
    private var defaultInput: (() -> BigInteger)? = null
    private val output: Queue<BigInteger> = LinkedList<BigInteger>()
    private var i = 0
    private var rbo = 0

    init {
        initialMemory.forEachIndexed { index, value ->
            memory[index] = value.toBigInteger()
        }
    }

    fun isHalted() = memory[i] == HALT

    fun hasOutput(): Boolean = output.isNotEmpty()
    fun getOutput(): BigInteger = output.poll()
    fun getOutputSize(): Int = output.size
    fun addInput(inValue: BigInteger) = input.offer(inValue)
    fun addInput(inValue: Int) = input.offer(inValue.toBigInteger())
    fun setDefaultInput(default: () -> BigInteger) {
        defaultInput = default
    }

    fun process() {
        loop@ while(true) {
            if (isHalted()) {
                break
            }

            if (tick()) break@loop
        }
    }

    fun tick(): Boolean {
        val instruction = getAt(i) % 100
        when (instruction) {
            1 -> {
                val p0 = getP0(i)
                val p1 = getP1(i)
                setMem3(i, p0 + p1)
                i += 4
            }
            2 -> {
                val p0 = getP0(i)
                val p1 = getP1(i)
                setMem3(i, p0 * p1)
                i += 4
            }
            3 -> {
                if (input.isEmpty() && defaultInput == null) {
                    return true
                }

                val inputValue = if (input.isNotEmpty()) input.poll() else defaultInput!!.invoke()

                when (((getAt(i) / 100) % 10)) {
                    0 -> memory[getAt(i + 1)] = inputValue
                    2 -> memory[rbo + getAt(i + 1)] = inputValue
                    else -> throw IllegalStateException("Unknown output destination")
                }

                i += 2
            }
            4 -> {
                val p0 = getP0(i)
                output.offer(p0)
                i += 2
            }
            5 -> {
                if (getP0(i) != BigInteger.ZERO) {
                    i = getP1(i).toInt()
                } else {
                    i += 3
                }
            }
            6 -> {
                if (getP0(i) == BigInteger.ZERO) {
                    i = getP1(i).toInt()
                } else {
                    i += 3
                }
            }
            7 -> {
                val newVal = if (getP0(i) < getP1(i)) BigInteger.ONE else BigInteger.ZERO
                setMem3(i, newVal)
                i += 4
            }
            8 -> {
                val newVal = if (getP0(i) == getP1(i)) BigInteger.ONE else BigInteger.ZERO
                setMem3(i, newVal)
                i += 4
            }
            9 -> {
                rbo += getP0(i).toInt()
                i += 2
            }
            else -> throw IllegalStateException("Unknown instruction ${memory[i]}")
        }

        return false
    }

    private fun getP0(index : Int): BigInteger {
        return when(((getAt(index) / 100) % 10)) {
            0 -> memory[getAt(index + 1)]
            1 -> memory[index + 1]
            2 -> memory[rbo + getAt(index + 1)]
            else -> throw IllegalStateException("Unknown P0 mode for ${getAt(index)}")
        } ?: BigInteger.ZERO
    }

    private fun getP1(index : Int): BigInteger {
        return when (((getAt(index) / 1000) % 10)) {
            0 -> memory[getAt(index + 2)]
            1 -> memory[index + 2]
            2 -> memory[rbo + getAt(index + 2)]
            else -> throw IllegalStateException("Unknown P1 mode for ${getAt(index)}")
        } ?: BigInteger.ZERO
    }

    private fun setMem3(index : Int, value: BigInteger) {
        when(((getAt(index) / 10000) % 10)) {
            0 -> memory[getAt(i + 3)] = value
            2 -> memory[rbo + getAt(i + 3)] = value
            else -> throw IllegalStateException("Unknown output destination")
        }
    }

    private fun getAt(index : Int) = memory[index]!!.toInt()
}

