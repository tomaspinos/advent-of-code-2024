package day03

import java.io.File

fun main() {
    var program = ""
    File(object {}.javaClass.getResource("/day03.txt").toURI()).forEachLine {
        program += it
    }
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
