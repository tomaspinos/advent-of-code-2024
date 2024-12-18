package day18

import common.XY
import common.directions
import common.resourceFile

fun main() {
    process("/day18.txt", 71, 71, 1024)
}

fun process(name: String, width: Int, height: Int, applicableBytes: Int) {
    val space = readInput(name, width, height, applicableBytes)
    space.bfs()
    println(space.cost(XY(width - 1, height - 1)))
}

fun readInput(name: String, width: Int, height: Int, applicableBytes: Int): Space {
    val bytes = resourceFile(name).readLines()
        .map {
            val split = it.split(",")
            XY(split[0].toInt(), split[1].toInt())
        }
    return Space(width, height, bytes, applicableBytes)
}

class Space(val width: Int, val height: Int, val bytes: List<XY>, val applicableBytes: Int) {
    val space = Array(height) { Array(width) { Field.FREE } }
    val costs = Array(height) { Array(width) { Int.MAX_VALUE } }

    init {
        bytes.subList(0, applicableBytes).forEach { (x, y) -> space[y][x] = Field.BLOCKED }
        costs[0][0] = 0
    }

    fun bfs() {
        var xys = listOf(XY(0, 0))
        while (xys.isNotEmpty()) {
            val nextXys = mutableListOf<XY>()
            for (xy in xys) {
                for (direction in directions) {
                    val nextXY = xy + direction
                    if (isValid(nextXY) && field(nextXY) == Field.FREE) {
                        val nextCost = cost(xy) + 1
                        if (nextCost < cost(nextXY)) {
                            cost(nextXY, nextCost)
                            nextXys.add(nextXY)
                        }
                    }
                }
            }
            xys = nextXys
        }
    }

    fun cost(xy: XY): Int = costs[xy.y][xy.x]

    fun cost(xy: XY, cost: Int) {
        costs[xy.y][xy.x] = cost
    }

    fun field(xy: XY): Field = space[xy.y][xy.x]

    fun isValid(xy: XY): Boolean = xy.x in 0..<width && xy.y in 0..<height
}

enum class Field { FREE, BLOCKED }