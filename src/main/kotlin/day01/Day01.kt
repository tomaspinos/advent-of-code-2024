package day01

import java.io.File
import kotlin.math.abs

fun main() {
    val left = ArrayList<Int>()
    val right = ArrayList<Int>()
    File(object {}.javaClass.getResource("day01.txt").toURI()).forEachLine {
        val numbers = it.split("   ")
        left.add(numbers[0].toInt())
        right.add(numbers[1].toInt())
    }
    val sortedLeft = left.sorted()
    val sortedRight = right.sorted()
    var sum = 0
    for (i in sortedLeft.indices) {
        sum += abs(sortedLeft[i] - sortedRight[i])
    }
    println(sum)
}