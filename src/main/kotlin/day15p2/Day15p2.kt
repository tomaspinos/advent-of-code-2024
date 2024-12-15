package day15p2

import common.XY
import common.resourceFile

fun main() {
    part2()
}

fun part2() = process("/day15.txt")

fun process(name: String) {
    val room = readInput(name)
    room.move()
    println(room.sumBoxGps())
}

fun readInput(name: String): Room {
    val lines = resourceFile(name).readLines()
    val blankLineIndex = lines.indexOfFirst { it.isBlank() }
    val room = lines.subList(0, blankLineIndex)
    val movementStr = lines.subList(blankLineIndex + 1, lines.size).joinToString("")

    val map = Array(room.size) { Array(room[0].length * 2) { Field.FREE } }
    var robotXY = XY(0, 0)
    for (y in room.indices) {
        for (x in room[y].indices) {
            when (room[y][x]) {
                '#' -> {
                    map[y][2 * x] = Field.WALL
                    map[y][2 * x + 1] = Field.WALL
                }

                'O' -> {
                    map[y][2 * x] = Field.BOX_LEFT
                    map[y][2 * x + 1] = Field.BOX_RIGHT
                }

                '@' -> robotXY = XY(2 * x, y)
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

            Field.BOX_LEFT -> {
                val boxesToMove = mutableListOf<XY>()
                if (canMoveBox(nextXY, direction, boxesToMove)) {
                    shiftBoxes(boxesToMove, direction)
                    robotXY = nextXY
                }
            }

            Field.BOX_RIGHT -> {
                val boxesToMove = mutableListOf<XY>()
                if (canMoveBox(nextXY + XY(-1, 0), direction, boxesToMove)) {
                    shiftBoxes(boxesToMove, direction)
                    robotXY = nextXY
                }
            }

            Field.WALL -> {
                // do nothing
            }
        }
    }

    fun canMoveBox(box: XY, direction: Direction, boxesToMove: MutableList<XY>): Boolean {
        if (boxesToMove.contains(box)) return true

        val newBoxLeft = box + direction.dif
        val newBoxRight = newBoxLeft + XY(1, 0)

        if (direction == Direction.RIGHT) {
            if (get(newBoxRight) == Field.FREE) {
                boxesToMove.add(box)
                return true
            } else if (get(newBoxRight) == Field.WALL) {
                return false
            } else if (get(newBoxRight) == Field.BOX_LEFT) {
                if (canMoveBox(newBoxRight, direction, boxesToMove)) {
                    boxesToMove.add(box)
                    return true
                }
            }
        } else if (direction == Direction.LEFT) {
            if (get(newBoxLeft) == Field.FREE) {
                boxesToMove.add(box)
                return true
            } else if (get(newBoxLeft) == Field.WALL) {
                return false
            } else if (get(newBoxLeft) == Field.BOX_RIGHT) {
                if (canMoveBox(newBoxLeft + XY(-1, 0), direction, boxesToMove)) {
                    boxesToMove.add(box)
                    return true
                }
            }
        } else {
            // direction is UP or DOWN
            if (get(newBoxLeft) == Field.FREE && get(newBoxRight) == Field.FREE) {
                // ..
                // []
                boxesToMove.add(box)
                return true
            } else if (get(newBoxLeft) == Field.BOX_LEFT || get(newBoxRight) == Field.BOX_RIGHT) {
                // []
                // []
                if (canMoveBox(newBoxLeft, direction, boxesToMove)) {
                    boxesToMove.add(box)
                    return true
                }
            } else if (get(newBoxLeft) == Field.BOX_RIGHT && get(newBoxRight) == Field.FREE) {
                // []
                //  []
                if (canMoveBox(newBoxLeft + XY(-1, 0), direction, boxesToMove)) {
                    boxesToMove.add(box)
                    return true
                }
            } else if (get(newBoxLeft) == Field.FREE && get(newBoxRight) == Field.BOX_LEFT) {
                //  []
                // []
                if (canMoveBox(newBoxRight, direction, boxesToMove)) {
                    boxesToMove.add(box)
                    return true
                }
            } else if (get(newBoxLeft) == Field.BOX_RIGHT && get(newBoxRight) == Field.BOX_LEFT) {
                // [][]
                //  []
                if (canMoveBox(newBoxLeft + XY(-1, 0), direction, boxesToMove) && canMoveBox(
                        newBoxRight,
                        direction,
                        boxesToMove
                    )
                ) {
                    boxesToMove.add(box)
                    return true
                }
            }
        }

        return false
    }

    fun shiftBoxes(boxes: List<XY>, direction: Direction) {
        boxes.forEach { box ->
            val newBox = box + direction.dif
            shiftBox(box, newBox)
        }
    }

    fun shiftBox(box: XY, newBox: XY) {
        set(box, Field.FREE)
        set(box + XY(1, 0), Field.FREE)
        set(newBox, Field.BOX_LEFT)
        set(newBox + XY(1, 0), Field.BOX_RIGHT)
    }

    fun sumBoxGps(): Int = boxes().sumOf(::boxGps)

    fun boxGps(xy: XY): Int = 100 * xy.y + xy.x

    fun boxes(): List<XY> {
        val boxes = mutableListOf<XY>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == Field.BOX_LEFT) boxes.add(XY(x, y))
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
                    Field.BOX_LEFT -> print('[')
                    Field.BOX_RIGHT -> print(']')
                    Field.WALL -> print('#')
                }
            }
            println()
        }
        println()
    }
}

enum class Field { FREE, BOX_LEFT, BOX_RIGHT, WALL }

enum class Direction(val dif: XY) {
    LEFT(XY(-1, 0)), RIGHT(XY(1, 0)), UP(XY(0, -1)), DOWN(XY(0, 1))
}