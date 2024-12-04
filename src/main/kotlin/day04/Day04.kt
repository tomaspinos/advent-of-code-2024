package day04

import java.io.File

fun main() {
    searchFile("/day04.txt")
}

fun searchFile(name: String) {
    val characters = ArrayList<List<Char>>()
    File(object {}.javaClass.getResource(name).toURI()).forEachLine {
        characters.add(it.toCharArray().asList())
    }

    val leftToRight = Pair(1, 0)
    val topToBottom = Pair(0, 1)
    val diagDownRight = Pair(1, 1)
    val diagDownLeft = Pair(-1, 1)

    val width = characters[0].size
    val height = characters.size

    val vectors = ArrayList<Vector>()

    // left to right
    for (y in 0..height - 1) {
        vectors.add(Vector(0, y, leftToRight, characters))
    }

    // top to bottom
    for (x in 0..width - 1) {
        vectors.add(Vector(x, 0, topToBottom, characters))
    }

    // diag down right
    // left side
    for (y in 0..height - 1) {
        vectors.add(Vector(0, y, diagDownRight, characters))
    }
    // top (minus left corner)
    for (x in 1..width - 1) {
        vectors.add(Vector(x, 0, diagDownRight, characters))
    }

    // diag down left
    // right side
    for (y in 0..height - 1) {
        vectors.add(Vector(width - 1, y, diagDownLeft, characters))
    }
    // top (minus right corner)
    for (x in 0..width - 2) {
        vectors.add(Vector(x, 0, diagDownLeft, characters))
    }

    val result = vectors
        .map { it.asString() }
        .sumOf { search(it) }

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