package day15

import common.XY
import common.resourceFile

fun main() {
    part1()
}

fun part1() = process("/day15.txt")

fun process(name: String) {
    val room = readInput(name)
    room.move()
    room.print()
    println(room.sumBoxGps())
}

fun readInput(name: String): Room {
    val lines = resourceFile(name).readLines()
    val blankLineIndex = lines.indexOfFirst { it.isBlank() }
    val room = lines.subList(0, blankLineIndex)
    val movementStr = lines.subList(blankLineIndex + 1, lines.size).joinToString("")

    val height = room.size
    val width = room[0].length
    val map = Array(height) { Array(width) { Field.FREE } }
    var robotXY = XY(0, 0)
    for (y in room.indices) {
        for (x in room[y].indices) {
            when (room[y][x]) {
                '#' -> map[y][x] = Field.WALL
                'O' -> map[y][x] = Field.BOX
                '@' -> robotXY = XY(x, y)
            }
        }
    }

    val movements = movementStr.map {
        when (it) {
            '<' -> Direction.LEFT
            '>' -> Direction.RIGHT
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            else -> throw RuntimeException("Unknown direction: $it")
        }
    }

    return Room(map, robotXY, movements)
}

class Room(val map: Array<Array<Field>>, var robotXY: XY, val movements: List<Direction>) {

    fun move() {
        movements.forEach(::move)
    }

    fun move(direction: Direction) {
        val nextXY = robotXY + direction.dif
        val nextField = get(nextXY)
        when (nextField) {
            Field.FREE -> {
                // just move the robot
                robotXY = nextXY
            }

            Field.BOX -> {
                var freeFieldBeforeWall: XY? = null
                var xy = nextXY
                while (true) {
                    xy += direction.dif
                    val field = get(xy)
                    if (field == Field.FREE) {
                        freeFieldBeforeWall = xy
                        break
                    } else if (field == Field.WALL) {
                        break
                    }
                }
                if (freeFieldBeforeWall != null) {
                    // can shift the box
                    robotXY = nextXY
                    set(nextXY, Field.FREE)
                    set(freeFieldBeforeWall, Field.BOX)
                }
            }

            Field.WALL -> {
                // do nothing
            }
        }
    }

    fun sumBoxGps(): Int = boxes().sumOf(::boxGps)

    fun boxGps(xy: XY): Int = 100 * xy.y + xy.x

    fun boxes(): List<XY> {
        val boxes = mutableListOf<XY>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == Field.BOX) boxes.add(XY(x, y))
            }
        }
        return boxes
    }

    fun get(xy: XY): Field = map[xy.y][xy.x]

    fun set(xy: XY, field: Field) {
        map[xy.y][xy.x] = field
    }

    fun print() {
        for (y in map.indices) {
            for (x in map[y].indices) {
                when (map[y][x]) {
                    Field.FREE -> if (robotXY == XY(x, y)) print('@') else print('.')
                    Field.BOX -> print('0')
                    Field.WALL -> print('#')
                }
            }
            println()
        }
    }
}

enum class Field { FREE, BOX, WALL }

enum class Direction(val dif: XY) {
    LEFT(XY(-1, 0)), RIGHT(XY(1, 0)), UP(XY(0, -1)), DOWN(XY(0, 1))
}