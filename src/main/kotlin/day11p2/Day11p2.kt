package day11p2

import common.resourceFile

fun main() {
    process("/day11.txt", 75)
}

fun process(name: String, iterations: Int) {
    var stones = readInput(name)

    for (i in 0..<iterations) stones = blink(stones)

    println(stones.values.sum())
}

fun blink(stones: Map<Long, Long>): Map<Long, Long> {
    val newStones = mutableMapOf<Long, Long>()
    for ((stone, count) in stones.entries) {
        if (stone == 0L) {
            newStones[1L] = (newStones[1L] ?: 0) + count
        } else {
            val string = stone.toString()
            if (string.length % 2 == 0) {
                val s1L = string.substring(0, string.length / 2).toLong()
                val s2L = string.substring(string.length / 2).toLong()
                newStones[s1L] = (newStones[s1L] ?: 0) + count
                newStones[s2L] = (newStones[s2L] ?: 0) + count
            } else {
                newStones[stone * 2024] = (newStones[stone * 2024] ?: 0) + count
            }
        }
    }
    return newStones
}

fun readInput(name: String): Map<Long, Long> {
    return resourceFile(name).readText()
        .split(" ")
        .map { it.toLong() }
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
}
