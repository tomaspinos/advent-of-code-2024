package day06p2

import common.resourceFile

fun main() {
    val startTimeMillis = System.currentTimeMillis()

    process("/day06.txt")

    println("${(System.currentTimeMillis() - startTimeMillis).toDouble() / 1000} s")
}

var width: Int = 0
var height: Int = 0
val initialObstacles = HashSet<XY>()
var initialPosition: DirectedPosition = DirectedPosition(XY(0, 0), Direction.UP)

fun process(name: String) {
    readInput(name)

    var cycles = 0

    for (x in 0..<width) {
        for (y in 0..<height) {
            val extraObstacle = XY(x, y)

            if (initialObstacles.contains(extraObstacle) || initialPosition.xy == extraObstacle) continue

            var currentPosition = initialPosition
            val visitedPositions = HashSet<DirectedPosition>()
            visitedPositions.add(currentPosition)

            while (true) {
                val nextPosition = nextPosition(currentPosition, extraObstacle)

                if (hasLeftArea(nextPosition)) {
                    break
                } else if (visitedPositions.contains(nextPosition)) {
                    cycles++
                    break
                } else {
                    currentPosition = nextPosition
                    visitedPositions.add(currentPosition)
                }
            }
        }
    }

    println(cycles)
}

fun hasLeftArea(position: DirectedPosition): Boolean {
    return with(position.xy) { x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1 }
}

fun nextPosition(currentPosition: DirectedPosition, extraObstacle: XY): DirectedPosition {
    val nextXY = currentPosition.xy.step(currentPosition.direction)

    return if (extraObstacle == nextXY || initialObstacles.contains(nextXY)) {
        DirectedPosition(currentPosition.xy, currentPosition.direction.turn())
    } else {
        DirectedPosition(nextXY, currentPosition.direction)
    }
}

fun readInput(name: String) {
    var y = 0

    resourceFile(name).forEachLine {
        width = it.length
        for (x in it.indices) {
            when (it[x]) {
                '#' -> initialObstacles.add(XY(x, y))
                '<' -> initialPosition = DirectedPosition(XY(x, y), Direction.LEFT)
                '>' -> initialPosition = DirectedPosition(XY(x, y), Direction.RIGHT)
                '^' -> initialPosition = DirectedPosition(XY(x, y), Direction.UP)
                'v' -> initialPosition = DirectedPosition(XY(x, y), Direction.DOWN)
            }
        }
        y++
    }

    height = y
}

data class XY(val x: Int, val y: Int) {

    fun step(direction: Direction): XY {
        return when (direction) {
            Direction.LEFT -> XY(x - 1, y)
            Direction.RIGHT -> XY(x + 1, y)
            Direction.UP -> XY(x, y - 1)
            Direction.DOWN -> XY(x, y + 1)
        }
    }
}

enum class Direction {
    LEFT, RIGHT, UP, DOWN;

    fun turn(): Direction {
        return when (this) {
            LEFT -> UP
            RIGHT -> DOWN
            UP -> RIGHT
            DOWN -> LEFT
        }
    }
}

data class DirectedPosition(val xy: XY, val direction: Direction)