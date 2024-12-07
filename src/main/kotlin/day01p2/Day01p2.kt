package day01p2

import common.resourceFile

fun main() {
    val left = ArrayList<Int>()
    val right = ArrayList<Int>()
    resourceFile("/day01.txt").forEachLine {
        val numbers = it.split("   ")
        left.add(numbers[0].toInt())
        right.add(numbers[1].toInt())
    }
    val rightOccurrences = mutableMapOf<Int, Int>()
    right.forEach {
        val v = rightOccurrences[it]
        if (v == null) {
            rightOccurrences[it] = 1
        } else {
            rightOccurrences[it] = v + 1
        }
    }
    var score = 0
    left.forEach { score += it * rightOccurrences[it]!! }
    println(score)
}