package day14

import common.XY
import common.resourceFile
import java.io.BufferedWriter
import java.io.File

fun main() {
    process("/day14.txt", 101, 103, 100, null)
    process("/day14.txt", 101, 103, 10000, File("tree.txt").bufferedWriter())
}

fun process(name: String, spaceWidth: Int, spaceHeight: Int, iterations: Int, writer: BufferedWriter?) {
    var robots = readInput(name)

    if (writer != null) {
        writer.write("0\n")
        print(robots, spaceWidth, spaceHeight, writer)
    }

    for (i in 1..iterations) {
        val newRobots = mutableListOf<Robot>()
        for (robot in robots) {
            newRobots.add(robot.move(spaceWidth, spaceHeight))
        }
        robots = newRobots
        if (writer != null) {
            writer.write("$i\n")
            print(robots, spaceWidth, spaceHeight, writer)
        }
    }

    val quadrants = robots.groupBy { it.quadrant(spaceWidth, spaceHeight) }.filterKeys { it != 0 }
    val quadrantSizes = quadrants.values.map { it.size }

    var result = quadrantSizes[0]
    for (i in 1..<quadrantSizes.size) result *= quadrantSizes[i]

    println(result)
}

fun print(robots: List<Robot>, spaceWidth: Int, spaceHeight: Int, writer: BufferedWriter) {
    val space = Array(spaceHeight) { Array(spaceWidth) { 0 } }
    for (robot in robots) {
        space[robot.xy.y][robot.xy.x]++
    }
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
        val px = groups[1]!!.value.toInt()
        val py = groups[2]!!.value.toInt()
        val vx = groups[3]!!.value.toInt()
        val vy = groups[4]!!.value.toInt()
        robots.add(Robot(XY(px, py), XY(vx, vy)))
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
        return if (xy.y < middleHeight) {
            if (xy.x < middleWidth) 1 else if (xy.x > middleWidth) 2 else 0
        } else if (xy.y > middleHeight) {
            if (xy.x < middleWidth) 3 else if (xy.x > middleWidth) 4 else 0
        } else {
            0
        }
    }
}
