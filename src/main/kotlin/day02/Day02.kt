package day02

import resourceFile
import kotlin.math.abs

fun main() {
    val reports = ArrayList<List<Int>>()
    resourceFile("/day02.txt").forEachLine {
        val levels = it.split(" ").map { it.toInt() }
        reports.add(levels)
    }
    val countOfSafeReports = reports.filter {
        val safe = isSafe(it)
        println("${it}: ${safe}")
        safe
    }.count()
    println(countOfSafeReports)
}

fun isSafe(report: List<Int>): Boolean {
    val first = report[0]
    val second = report[1]

    if (first == second) return false

    val compare = if (first < second) ::isAsc else ::isDesc

    var prev = first
    for (i in 1..<report.count()) {
        val curr = report[i]
        if (compare(prev, curr) && abs(prev - curr) <= 3) {
            prev = curr
        } else {
            return false
        }
    }

    return true
}

fun isAsc(a: Int, b: Int): Boolean {
    return a < b
}

fun isDesc(a: Int, b: Int): Boolean {
    return a > b
}