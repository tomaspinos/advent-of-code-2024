package day02p2

import day02.isAsc
import day02.isDesc
import java.io.File
import kotlin.math.abs

fun main() {
    val reports = ArrayList<List<Int>>()
    File(object {}.javaClass.getResource("/day02.txt").toURI()).forEachLine {
        reports.add(it.split(" ").map { it.toInt() })
    }
    val countOfSafeReports = reports.count {
        isSafe(it) || isFixable(it)
    }
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

fun isFixable(report: List<Int>): Boolean {
    for (i in report.indices) {
        val clone = ArrayList<Int>(report)
        clone.removeAt(i)
        if (isSafe(clone)) {
            return true
        }
    }
    return false
}

fun isAsc(a: Int, b: Int): Boolean {
    return a < b
}

fun isDesc(a: Int, b: Int): Boolean {
    return a > b
}
