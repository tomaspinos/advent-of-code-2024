package day14

import common.XY
import common.resourceFile

fun main() {
    process("/day14.txt", 101, 103)
}

fun process(name: String, spaceWidth: Int, spaceHeight: Int) {
    var robots = readInput(name)

    for (i in 1..100) {
        val newRobots = mutableListOf<Robot>()
        for (robot in robots) {
            newRobots.add(robot.move(spaceWidth, spaceHeight))
        }
        robots = newRobots
    }

    val quadrants = robots.groupBy { it.quadrant(spaceWidth, spaceHeight) }.filterKeys { it != 0 }
    val quadrantSizes = quadrants.values.map { it.size }

    var result = quadrantSizes[0]
    for (i in 1..<quadrantSizes.size) result *= quadrantSizes[i]

    println(result)
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
        } else if (xy.y > middleHeight){
            if (xy.x < middleWidth) 3 else if (xy.x > middleWidth) 4 else 0
        } else {
            0
        }
    }
}
