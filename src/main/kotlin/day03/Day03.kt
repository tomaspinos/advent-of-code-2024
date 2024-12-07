package day03

import common.resourceFile

fun main() {
    var program = resourceFile("/day03.txt")
        .readLines()
        .joinToString()
    println(evaluateLine(program))
}

fun evaluateLine(line: String): Int {
    val regex = "mul\\(([0-9]+),([0-9]+)\\)".toRegex()
    return regex.findAll(line)
        .map {
            val a = it.groups[1]!!.value.toInt()
            val b = it.groups[2]!!.value.toInt()
            a * b
        }
        .sum()
}
