package day20

import common.XY
import common.resourceFile
import kotlin.math.abs

fun main() {
    part1("/day20.txt", 100)
    part2("/day20.txt", 100)
}

fun part1(name: String, cheatCostLimit: Int) {
    val room = readInput(name)
    val path = findInitialPath(room)
    val cheats = findCheats1(path, room)
    print(path, emptyList(), room)
    val goodCheatCount = cheats.groupBy { it.second }
        .filter { it.key >= cheatCostLimit }
        .map { it.value.size }
        .sum()
    println(goodCheatCount)
}

fun part2(name: String, cheatCostLimit: Int) {
    val room = readInput(name)
    val path = findInitialPath(room)

    val cheats = mutableListOf<Cheat>()

    for (i in path.steps.indices) {
        for (j in i + 1 until path.steps.size) {
            val from = path.steps[i]
            val to = path.steps[j]
            val pathDistance = j - i
            val taxicabDistance = taxicabDistance(from, to)
            if (taxicabDistance <= 20 && pathDistance - taxicabDistance >= cheatCostLimit) {
                cheats.add(Cheat(from, to, pathDistance - taxicabDistance))
            }
        }
    }

    cheats.groupBy { it.savedSteps }.forEach { (savedSteps, cheats) ->
        println("${cheats.size} cheats $savedSteps picoseconds") }

    println(cheats.size)
}

fun taxicabDistance(from: XY, to: XY): Int = abs(from.x - to.x) + abs(from.y - to.y)

fun findInitialPath(room: Room): Path {
    var path = Path(listOf(room.start), 0)
    while (true) {
        val xy = room.nextFreeField(path.lastXY(), path)
        val nextPath = path.step(xy)
        if (xy == room.end) return nextPath
        path = nextPath
    }
}

/**
 * O#O
 *
 * O
 * #
 * O
 */
fun findCheats1(initialPath: Path, room: Room): List<Pair<XY, Int>> {
    val cheats = mutableListOf<Pair<XY, Int>>()
    for (y in 1..<room.height - 1) {
        for (x in 1..<room.width - 1) {
            val xy = XY(x, y)
            if (room.field(xy) == Field.WALL) {
                checkCheat1(xy, xy.left(), xy.right(), initialPath, cheats)
                checkCheat1(xy, xy.up(), xy.down(), initialPath, cheats)
            }
        }
    }
    return cheats
}

fun checkCheat1(cheat: XY, from: XY, to: XY, initialPath: Path, cheats: MutableList<Pair<XY, Int>>) {
    val fromCost = initialPath.steps.indexOf(from)
    val totCost = initialPath.steps.indexOf(to)
    if (fromCost < Int.MAX_VALUE && totCost < Int.MAX_VALUE) {
        cheats.add(Pair(cheat, abs(fromCost - totCost) - 2))
    }
}

fun readInput(name: String): Room {
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
    return Room(width, height, map, start, end)
}

fun print(path: Path, cheats: List<XY>, room: Room) {
    val canvas = Array(room.height) { Array(room.width) { '.' } }
    for (y in room.fields.indices)
        for (x in room.fields[y].indices)
            when (room.fields[y][x]) {
                Field.WALL -> canvas[y][x] = '#'
                Field.FREE -> canvas[y][x] = '.'
                Field.START -> canvas[y][x] = 'S'
                Field.END -> canvas[y][x] = 'E'
            }
    for (xy in path.steps) if (xy != room.start && xy != room.end) canvas[xy.y][xy.x] = 'O'
    for (xy in cheats) canvas[xy.y][xy.x] = '+'
    canvas.forEach { println(it.joinToString("")) }
    println()
}

enum class Field { WALL, FREE, START, END }

data class Room(
    val width: Int,
    val height: Int,
    val fields: Array<Array<Field>>,
    val start: XY,
    val end: XY
) {
    fun nextFreeField(xy: XY, path: Path): XY =
        listOf(xy.up(), xy.down(), xy.left(), xy.right())
            .filter { field(it) == Field.FREE || field(it) == Field.START || field(it) == Field.END }
            .first { !path.steps.contains(it) }

    fun field(xy: XY): Field = fields[xy.y][xy.x]
}

data class Path(val steps: List<XY>, val cost: Int) {
    fun lastXY(): XY = steps.last()
    fun step(xy: XY): Path = Path(steps + xy, cost + 1)
}

data class Cheat(val from: XY, val to: XY, val savedSteps: Int)
