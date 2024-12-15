package day15p2

import common.XY
import common.resourceFile
import day15p2.Field.*

fun main() {
    process("/day15.txt")
}

fun process(name: String) {
    val room = readInput(name)
    room.move()
    println(room.sumBoxGps())
}

class Room(val map: Array<Array<Field>>, var robotXY: XY, val movements: List<Direction>) {

    fun move() {
        movements.forEach(::move)
    }

    fun move(direction: Direction) {
        val nextXY = robotXY + direction.dif
        val nextField = get(nextXY)
        when (nextField) {
            FREE -> {
                // just move the robot
                robotXY = nextXY
            }

            BOX_LEFT -> {
                val boxesToMove = mutableListOf<XY>()
                if (canMoveBox(nextXY, direction, boxesToMove)) {
                    moveBoxes(boxesToMove, direction)
                    robotXY = nextXY
                }
            }

            BOX_RIGHT -> {
                val boxesToMove = mutableListOf<XY>()
                if (canMoveBox(nextXY.left(), direction, boxesToMove)) {
                    moveBoxes(boxesToMove, direction)
                    robotXY = nextXY
                }
            }

            WALL -> {
                // do nothing
            }
        }
    }

    fun canMoveBox(box: XY, direction: Direction, boxesToMove: MutableList<XY>): Boolean {
        if (boxesToMove.contains(box)) return true

        val newLeft = box + direction.dif
        val newRight = newLeft.right()

        val newLeftField = get(newLeft)
        val newRightField = get(newRight)

        if (direction == Direction.RIGHT) {
            if (newRightField == FREE) {
                boxesToMove.add(box)
                return true
            } else if (newRightField == WALL) {
                return false
            } else if (newRightField == BOX_LEFT) {
                if (canMoveBox(newRight, direction, boxesToMove)) {
                    boxesToMove.add(box)
                    return true
                }
            }
        } else {
            if (direction == Direction.LEFT) {
                if (newLeftField == FREE) {
                    boxesToMove.add(box)
                    return true
                } else if (newLeftField == WALL) {
                    return false
                } else if (newLeftField == BOX_RIGHT) {
                    if (canMoveBox(newLeft.left(), direction, boxesToMove)) {
                        boxesToMove.add(box)
                        return true
                    }
                }
            } else {
                // direction is UP or DOWN
                if (newLeftField == FREE && newRightField == FREE) {
                    // ..
                    // []
                    boxesToMove.add(box)
                    return true
                } else if (newLeftField == BOX_LEFT || newRightField == BOX_RIGHT) {
                    // []
                    // []
                    if (canMoveBox(newLeft, direction, boxesToMove)) {
                        boxesToMove.add(box)
                        return true
                    }
                } else if (newLeftField == BOX_RIGHT && newRightField == FREE) {
                    // []
                    //  []
                    if (canMoveBox(newLeft.left(), direction, boxesToMove)) {
                        boxesToMove.add(box)
                        return true
                    }
                } else if (newLeftField == FREE && newRightField == BOX_LEFT) {
                    //  []
                    // []
                    if (canMoveBox(newRight, direction, boxesToMove)) {
                        boxesToMove.add(box)
                        return true
                    }
                } else if (newLeftField == BOX_RIGHT && newRightField == BOX_LEFT) {
                    // [][]
                    //  []
                    if (canMoveBox(newLeft.left(), direction, boxesToMove)
                        && canMoveBox(newRight, direction, boxesToMove)
                    ) {
                        boxesToMove.add(box)
                        return true
                    }
                }
            }
        }

        return false
    }

    fun moveBoxes(boxes: List<XY>, direction: Direction) {
        boxes.forEach { box ->
            val newBox = box + direction.dif
            set(box, FREE)
            set(box.right(), FREE)
            set(newBox, BOX_LEFT)
            set(newBox.right(), BOX_RIGHT)
        }
    }

    fun sumBoxGps(): Int = boxes().sumOf { 100 * it.y + it.x }

    fun boxes(): List<XY> {
        val boxes = mutableListOf<XY>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == BOX_LEFT) boxes.add(XY(x, y))
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
                    FREE -> if (robotXY == XY(x, y)) print('@') else print('.')
                    BOX_LEFT -> print('[')
                    BOX_RIGHT -> print(']')
                    WALL -> print('#')
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

fun readInput(name: String): Room {
    val lines = resourceFile(name).readLines()
    val blankLineIndex = lines.indexOfFirst { it.isBlank() }
    val room = lines.subList(0, blankLineIndex)
    val movementStr = lines.subList(blankLineIndex + 1, lines.size).joinToString("")

    val map = Array(room.size) { Array(room[0].length * 2) { FREE } }
    var robotXY = XY(0, 0)
    for (y in room.indices) {
        for (x in room[y].indices) {
            when (room[y][x]) {
                '#' -> {
                    map[y][2 * x] = WALL
                    map[y][2 * x + 1] = WALL
                }

                'O' -> {
                    map[y][2 * x] = BOX_LEFT
                    map[y][2 * x + 1] = BOX_RIGHT
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
