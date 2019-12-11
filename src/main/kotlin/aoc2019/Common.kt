package aoc2019

data class XY(val x: Int, val y: Int)
data class XYZ(val x: Int, val y: Int, val z: Int)

fun IntArray.permute(onNextPermutation: (IntArray) -> Unit) = permute(this, 0, this.size, onNextPermutation)

private fun IntArray.swap(i: Int, j : Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

private fun permute(src: IntArray, l: Int, r: Int, onNextPermutation: (IntArray) -> Unit) {
    if (l == r) {
        onNextPermutation(src)
    } else {
        (l until r).forEach { i ->
            // Swapping done
            src.swap(l, i)

            // Recursion called
            permute(src, l + 1, r, onNextPermutation)

            //backtrack
            src.swap(l, i)
        }
    }
}