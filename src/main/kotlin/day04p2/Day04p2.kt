package day04p2

import common.resourceFile

fun main() {
    searchFile("/day04.txt")
}

fun searchFile(name: String) {
    val paper = ArrayList<String>()
    resourceFile(name).forEachLine { paper.add(it) }

    val patterns = listOf(
        listOf(
            "M S",
            " A ",
            "M S"
        ),
        listOf(
            "M M",
            " A ",
            "S S"
        ),
        listOf(
            "S S",
            " A ",
            "M M"
        ),
        listOf(
            "S M",
            " A ",
            "S M"
        )
    )

    val width = paper[0].length
    val height = paper.size

    var result = 0

    for (y in 0..height - 3) {
        for (x in 0..width - 3) {
            for (pattern in patterns) {
                if (matchPattern(x, y, pattern, paper)) {
                    result++
                }
            }
        }
    }

    println(result)
}

fun matchPattern(x: Int, y: Int, pattern: List<String>, paper: List<String>): Boolean {
    for (yy in 0..2) {
        for (xx in 0..2) {
            val patternCh = pattern[yy][xx]
            val paperCh = paper[y + yy][x + xx]
            if (patternCh != ' ' && patternCh != paperCh) return false
        }
    }
    return true
}