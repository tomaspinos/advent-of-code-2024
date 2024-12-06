package day06

import resourceFile

fun main() {
    process("/day06.txt")
}

var width: Int = 0
var height: Int = 0
val obstacles = HashSet<XY>()
var position: DirectedPosition = DirectedPosition(XY(0, 0), Direction.UP)
val visitedPositions = HashSet<XY>()

fun process(name: String) {
    readInput(name)

    while (!hasLeftArea()) step()

    println(visitedPositions.size)
}

fun hasLeftArea(): Boolean {
    return position.xy.x <= 0 || position.xy.x >= width - 1
            || position.xy.y <= 0 || position.xy.y >= height - 1
}

fun step() {
    val newXY = position.xy.step(position.direction)

    if (obstacles.contains(newXY)) {
        position = DirectedPosition(position.xy, position.direction.turn())
    } else {
        position = DirectedPosition(newXY, position.direction)
        visitedPositions.add(position.xy)
    }
}

fun readInput(name: String) {
    var y = 0

    resourceFile(name).forEachLine {
        width = it.length
        for (x in it.indices) {
            when (it[x]) {
                '#' -> obstacles.add(XY(x, y))
                '<' -> position = DirectedPosition(XY(x, y), Direction.LEFT)
                '>' -> position = DirectedPosition(XY(x, y), Direction.RIGHT)
                '^' -> position = DirectedPosition(XY(x, y), Direction.UP)
                'v' -> position = DirectedPosition(XY(x, y), Direction.DOWN)
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
