package day11

import common.resourceFile

fun main() {
    val stones = process("/day11.txt", 25)
    println(stones.size)
}

fun process(name: String, iterations: Int): List<Long> {
    var stones = readInput(name)

    for (i in 0..<iterations) {
        val newStones = mutableListOf<Long>()
        for (stone in stones) {
            val stoneAfterRulesApplied = applyRules(stone)
            newStones += stoneAfterRulesApplied
        }
        stones = newStones
    }

    return stones
}

fun applyRules(stone: Long): List<Long> {
    if (stone == 0L) {
        return listOf(1L)
    } else {
        val string = stone.toString()
        if (string.length % 2 == 0) {
            val s1 = string.substring(0, string.length / 2)
            val s2 = string.substring(string.length / 2)
            return listOf(s1.toLong(), s2.toLong())
        } else {
            return listOf(stone * 2024)
        }
    }
}

fun readInput(name: String): List<Long> {
    return resourceFile(name).readText().split(" ").map { it.toLong() }
}
