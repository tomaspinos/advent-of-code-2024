package day04

import common.resourceFile

fun main() {
    searchFile("/day04.txt")
}

fun searchFile(name: String) {
    val characters = ArrayList<List<Char>>()
    resourceFile(name).forEachLine {
        characters.add(it.toCharArray().asList())
    }

    val leftToRight = Pair(1, 0)
    val topToBottom = Pair(0, 1)
    val diagDownRight = Pair(1, 1)
    val diagDownLeft = Pair(-1, 1)

    val width = characters[0].size
    val height = characters.size

    var leftToRightCount = 0
    var topToBottomCount = 0
    var diagDownRightCount = 0
    var diagDownLeftCount = 0

    // left to right
    for (y in 0..height - 1) {
        leftToRightCount += Vector(0, y, leftToRight, characters).search()
    }

    // top to bottom
    for (x in 0..width - 1) {
        topToBottomCount += Vector(x, 0, topToBottom, characters).search()
    }

    // diag down right
    // left side
    for (y in 0..height - 1) {
        diagDownRightCount += Vector(0, y, diagDownRight, characters).search()
    }
    // top (minus left corner)
    for (x in 1..width - 1) {
        diagDownRightCount += Vector(x, 0, diagDownRight, characters).search()
    }

    // diag down left
    // right side
    for (y in 0..height - 1) {
        diagDownLeftCount += Vector(width - 1, y, diagDownLeft, characters).search()
    }
    // top (minus right corner)
    for (x in 0..width - 2) {
        diagDownLeftCount += Vector(x, 0, diagDownLeft, characters).search()
    }

    println("leftToRightCount = ${leftToRightCount}")
    println("topToBottomCount = ${topToBottomCount}")
    println("diagDownRightCount = ${diagDownRightCount}")
    println("diagDownLeftCount = ${diagDownLeftCount}")

    val result = leftToRightCount + topToBottomCount + diagDownRightCount + diagDownLeftCount

    println(result)
}

val XMAS = "XMAS".toRegex()
val SAMX = "SAMX".toRegex()

fun search(s: String): Int {
    return XMAS.findAll(s).count() + SAMX.findAll(s).count()
}

class Vector(
    var x: Int,
    var y: Int,
    val delta: Pair<Int, Int>,
    val characters: List<List<Char>>
) {
    fun search(): Int {
        return search(asString())
    }

    fun asString(): String {
        var s = ""

        while (isValid()) {
            s += characters[y][x]
            x += delta.first
            y += delta.second
        }

        return s
    }

    fun isValid(): Boolean {
        val width = characters[0].size
        val height = characters.size
        return x in 0..<width && y in 0..<height
    }
}