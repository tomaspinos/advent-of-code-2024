package day25

import common.resourceFile

fun main() {
    part1("/day25.txt")
}

fun part1(name: String) {
    val (locks, keys) = readInput(name)
    var pairs = 0
    for (lock in locks) {
        for (key in keys) {
            if (!overlaps(lock, key)) pairs++
        }
    }
    println(pairs)
}

fun overlaps(lock: IntArray, key: IntArray): Boolean {
    for (i in 0..<5) if (lock[i] + key[i] > 5) return true
    return false
}

fun readInput(name: String): Pair<List<IntArray>, List<IntArray>> {
    val locks = mutableListOf<IntArray>()
    val keys = mutableListOf<IntArray>()
    val lines = resourceFile(name).readLines().iterator()
    while (lines.hasNext()) {
        val lock = lines.next() == "#####"
        if (lock) {
            val shape = (0..5).map { lines.next().toCharArray() }
            locks += readHeights(shape)
        } else {
            val shape = (0..5).map { lines.next().toCharArray() }.reversed().subList(1, 6)
            keys += readHeights(shape)
        }
        if (lines.hasNext()) lines.next()
    }
    return locks to keys
}

fun readHeights(shape: List<CharArray>): IntArray {
    val heights = IntArray(5)
    for (x in shape[0].indices) {
        var height = 0
        while (height < 5 && shape[height][x] == '#') height++
        heights[x] = height
    }
    return heights
}
