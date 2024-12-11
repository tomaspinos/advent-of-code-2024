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

fun blink(map: Map<Long, Long>): Map<Long, Long> {
    val newMap = mutableMapOf<Long, Long>()
    for (entry in map.entries) {
        val (uniqueStoneNumber, count) = entry
        if (uniqueStoneNumber == 0L) {
            newMap[1L] = (newMap[1L] ?: 0) + count
        } else {
            val string = uniqueStoneNumber.toString()
            if (string.length % 2 == 0) {
                val s1L = string.substring(0, string.length / 2).toLong()
                val s2L = string.substring(string.length / 2).toLong()
                newMap[s1L] = (newMap[s1L] ?: 0) + count
                newMap[s2L] = (newMap[s2L] ?: 0) + count
            } else {
                newMap[uniqueStoneNumber * 2024] = (newMap[uniqueStoneNumber * 2024] ?: 0) + count
            }
        }
    }
    return newMap
}

fun readInput(name: String): Map<Long, Long> {
    return resourceFile(name).readText()
        .split(" ")
        .map { it.toLong() }
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
}
