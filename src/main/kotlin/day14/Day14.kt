package day14

import common.XY
import common.resourceFile
import java.io.BufferedWriter
import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() = process("/day14.txt", 101, 103, 100)

fun part2() = process("/day14.txt", 101, 103, 10000, File("tree.txt").bufferedWriter())

fun process(name: String, spaceWidth: Int, spaceHeight: Int, iterations: Int, writer: BufferedWriter? = null) {
    var robots = readInput(name)

    if (writer != null) print(0, robots, spaceWidth, spaceHeight, writer)

    for (i in 1..iterations) {
        robots = robots.map { it.move(spaceWidth, spaceHeight) }
        if (writer != null) print(i, robots, spaceWidth, spaceHeight, writer)
    }

    val result = robots
        .groupBy { it.quadrant(spaceWidth, spaceHeight) }
        .filterKeys { it != 0 }
        .values.map { it.size }
        .reduce { acc, cur -> acc * cur }

    println(result)
}

fun print(iteration: Int, robots: List<Robot>, spaceWidth: Int, spaceHeight: Int, writer: BufferedWriter) {
    writer.write("$iteration\n")
    val space = Array(spaceHeight) { Array(spaceWidth) { 0 } }
    for (robot in robots) space[robot.xy.y][robot.xy.x]++
    for (y in 0..<spaceHeight) {
        var s = ""
        for (x in 0..<spaceWidth) {
            val count = space[y][x]
            if (count > 0) s += count else s += '.'
        }
        writer.write("$s\n")
    }
}

val REGEX = "p=(\\d+),(\\d+) v=(-*\\d+),(-*\\d+)".toRegex()

fun readInput(name: String): List<Robot> {
    val robots = mutableListOf<Robot>()
    resourceFile(name).forEachLine {
        val groups = REGEX.find(it)!!.groups
        robots.add(
            Robot(
                XY(groups[1]!!.value.toInt(), groups[2]!!.value.toInt()),
                XY(groups[3]!!.value.toInt(), groups[4]!!.value.toInt())
            )
        )
    }
    return robots
}

data class Robot(val xy: XY, val velocity: XY) {
    fun move(spaceWidth: Int, spaceHeight: Int): Robot {
        val xx = (xy.x + velocity.x) % spaceWidth
        val yy = (xy.y + velocity.y) % spaceHeight
        return Robot(XY(if (xx >= 0) xx else spaceWidth + xx, if (yy >= 0) yy else spaceHeight + yy), velocity)
    }

    fun quadrant(spaceWidth: Int, spaceHeight: Int): Int {
        val middleHeight = spaceHeight / 2
        val middleWidth = spaceWidth / 2
        return with(xy) {
            if (y < middleHeight) {
                if (x < middleWidth) 1 else if (x > middleWidth) 2 else 0
            } else if (y > middleHeight) {
                if (x < middleWidth) 3 else if (x > middleWidth) 4 else 0
            } else {
                0
            }
        }
    }
}
