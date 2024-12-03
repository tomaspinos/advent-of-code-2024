package day03p2

import java.io.File

fun main() {
    var program = ""
    File(object {}.javaClass.getResource("/day03.txt").toURI()).forEachLine {
        program += it
    }
    println(evaluateLine(program))
}

fun evaluateLine(line: String): Int {
    val regex = "mul\\(([0-9]+),([0-9]+)\\)|do\\(\\)|don't\\(\\)".toRegex()
    val matches = regex.findAll(line)
    var enabled = true
    var sum = 0
    for (match in matches) {
        val value = match.value
        if (value.startsWith("mul")) {
            if (enabled) {
                val a = match.groups[1]!!.value.toInt()
                val b = match.groups[2]!!.value.toInt()
                sum += (a * b)
            }
        } else if (value.startsWith("do(")) {
            enabled = true
        } else if (value.startsWith("don")) {
            enabled = false
        }
    }
    return sum
}
