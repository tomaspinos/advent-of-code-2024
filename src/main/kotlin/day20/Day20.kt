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
    val path = bfs(room)
    val cheats = findCheats(room)
    print(path, emptyList(), room)
    val goodCheatCount = cheats.groupBy { it.second }
        .filter { it.key >= cheatCostLimit }
        .map { it.value.size }
        .sum()
    println(goodCheatCount)
}

fun part2(name: String, cheatCostLimit: Int) {
    val room = readInput(name)
    val path = bfs(room)

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

    cheats.groupBy { it.savedSteps }.forEach { savedSteps, cheats ->
        println("${cheats.size} cheats $savedSteps picoseconds") }

    println(cheats.size)
}

fun taxicabDistance(from: XY, to: XY): Int = abs(from.x - to.x) + abs(from.y - to.y)

fun bfs(room: Room): Path {
    var paths = listOf(Path(listOf(room.start), 0))
    while (paths.isNotEmpty()) {
        val nextPaths = mutableListOf<Path>()
        for (path in paths) {
            for (xy in room.freeFieldsAround(path.lastXY())) {
                val nextPath = path.step(xy)
                if (nextPath.cost <= room.cost(xy)) {
                    if (nextPath.cost < room.cost(xy)) room.cost(xy, nextPath.cost)
                    if (xy == room.end) return nextPath else nextPaths.add(nextPath)
                }
            }
        }
        paths = nextPaths
    }
    throw IllegalStateException("No path found")
}

/**
 * O#O
 *
 * O
 * #
 * O
 */
fun findCheats(room: Room): List<Pair<XY, Int>> {
    val cheats = mutableListOf<Pair<XY, Int>>()
    for (y in 1..<room.height - 1) {
        for (x in 1..<room.width - 1) {
            val xy = XY(x, y)
            if (room.field(xy) == Field.WALL) {
                checkCheat(xy, xy.left(), xy.right(), room, cheats)
                checkCheat(xy, xy.up(), xy.down(), room, cheats)
            }
        }
    }
    return cheats
}

fun checkCheat(cheat: XY, from: XY, to: XY, room: Room, cheats: MutableList<Pair<XY, Int>>) {
    val fromCost = room.cost(from)
    val totCost = room.cost(to)
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
    val costs: Array<Array<Int>> = Array(height) { Array(width) { Int.MAX_VALUE } }

    fun freeFieldsAround(xy: XY): List<XY> =
        listOf(xy.up(), xy.down(), xy.left(), xy.right())
            .filter { field(it) == Field.FREE || field(it) == Field.START || field(it) == Field.END }

    fun field(xy: XY): Field = fields[xy.y][xy.x]

    fun cost(xy: XY): Int = costs[xy.y][xy.x]

    fun cost(xy: XY, cost: Int) {
        costs[xy.y][xy.x] = cost
    }
}

data class Path(val steps: List<XY>, val cost: Int) {
    fun lastXY(): XY = steps.last()
    fun step(xy: XY): Path = Path(steps + xy, cost + 1)
}

data class Cheat(val from: XY, val to: XY, val savedSteps: Int)
