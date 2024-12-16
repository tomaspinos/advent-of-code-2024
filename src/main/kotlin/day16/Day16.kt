package day16

import common.XY
import common.resourceFile

fun main() {
    part1()
}

fun part1() = process("/day16.txt")

fun process(name: String) {
    val maze = readInput(name)
    go(listOf(Path(listOf(Pair(Direction.RIGHT, maze.start)), 0)), maze)
    println(maze.cost(maze.end))
}

fun go(paths: List<Path>, maze: Maze) {
//    print(paths, maze)
    val nextPaths = mutableListOf<Path>()
    for (path in paths) {
        for ((direction, xy) in maze.freeFieldsAround(path.lastXY())) {
            val nextPath = path.step(direction, xy)
            if (nextPath.cost < maze.cost(xy)) {
                maze.cost(xy, nextPath.cost)
                if (xy != maze.end) nextPaths.add(nextPath)
            }
        }
    }
    if (nextPaths.isNotEmpty()) go(nextPaths, maze)
}

fun print(paths: List<Path>, maze: Maze) {
    val canvas = Array(maze.height) { Array(maze.width) { '.' } }
    for (y in maze.fields.indices) {
        for (x in maze.fields[y].indices) {
            when (maze.fields[y][x]) {
                Field.WALL -> canvas[y][x] = '#'
                Field.FREE -> canvas[y][x] = '.'
                Field.START -> canvas[y][x] = 'S'
                Field.END -> canvas[y][x] = 'E'
            }
        }
    }
    for (path in paths) {
        for ((direction, xy) in path.steps) {
            canvas[xy.y][xy.x] = direction.ch
        }
    }
    canvas.forEach { println(it.joinToString("")) }
    println()
}

fun readInput(name: String): Maze {
    val lines = resourceFile(name).readLines()
    val height = lines.size
    val width = lines[0].length
    val map = Array(height) { Array(width) { Field.FREE } }
    var start = XY(0, 0)
    var end = XY(0, 0)
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            when (lines[y][x]) {
                '#' -> map[y][x] = Field.WALL
                '.' -> map[y][x] = Field.FREE
                'S' -> {
                    map[y][x] = Field.START
                    start = XY(x, y)
                }

                'E' -> {
                    map[y][x] = Field.END
                    end = XY(x, y)
                }
            }
        }
    }
    return Maze(width, height, map, start, end)
}

enum class Field { WALL, FREE, START, END }

enum class Direction(val ch: Char, val dif: XY) {
    LEFT('<', XY(-1, 0)),
    RIGHT('>', XY(1, 0)),
    UP('^', XY(0, -1)),
    DOWN('v', XY(0, 1))
}

data class Maze(
    val width: Int,
    val height: Int,
    val fields: Array<Array<Field>>,
    val start: XY,
    val end: XY
) {
    val costs: Array<Array<Int>> = Array(height) { Array(width) { Int.MAX_VALUE } }
    fun field(xy: XY): Field = fields[xy.y][xy.x]
    fun freeFieldsAround(xy: XY): List<Pair<Direction, XY>> =
        listOf(
            Pair(Direction.LEFT, xy.left()),
            Pair(Direction.UP, xy.up()),
            Pair(Direction.RIGHT, xy.right()),
            Pair(Direction.DOWN, xy.down())
        )
            .filter { field(it.second) == Field.FREE || field(it.second) == Field.END }

    fun cost(xy: XY): Int = costs[xy.y][xy.x]
    fun cost(xy: XY, cost: Int) {
        costs[xy.y][xy.x] = cost
    }
}

data class Path(val steps: List<Pair<Direction, XY>>, val cost: Int) {
    fun lastXY(): XY = steps.last().second
    fun step(direction: Direction, xy: XY): Path {
        return if (steps.isNotEmpty()) {
            val lastDirection = steps.last().first
            Path(steps + Pair(direction, xy), cost + (if (direction == lastDirection) 1 else 1001))
        } else {
            Path(steps + Pair(direction, xy), cost + 1)
        }
    }
}