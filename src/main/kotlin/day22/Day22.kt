package day22

import common.resourceFile

fun main() {
    part1("/day22.txt")
}

fun part1(name: String) {
    val input = readInput(name)
    val result = input.map { nextSecretNumber(it, 2000) }.sum()
    println(result)
}

fun nextSecretNumber(num: Long, count: Int): Long {
    var result = num
    for (i in 1..count) result = nextSecretNumber(result)
    println("$num: $result")
    return result
}

fun nextSecretNumber(num: Long): Long {
    var next = ((num * 64) xor num) % 16777216
    next = ((next / 32) xor next) % 16777216
    next = ((next * 2048) xor next) % 16777216
    return next
}

fun readInput(name: String): List<Long> {
    return resourceFile(name).readLines().map(String::toLong)
}