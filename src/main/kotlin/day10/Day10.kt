package day10

import common.resourceFile

fun main() {
    process("/day10.txt")
}

fun process(name: String) {
    val plan = readInput(name)

    val score = plan.trailheads
        .sumOf { dfs(it, plan) { position, search -> !search.reachedBefore(position) } }
    println(score)

    val rating = plan.trailheads
        .sumOf { dfs(it, plan) { _, _ -> true } }
    println(rating)
}

fun dfs(trailhead: XY, plan: Plan, nineCondition: (XY, Search) -> Boolean): Int {
    val search = Search()
    dfs(trailhead, listOf(trailhead), plan, search, nineCondition)
    return search.countNines()
}

fun dfs(
    position: XY, path: List<XY>, plan: Plan, search: Search, nineCondition: (XY, Search) -> Boolean
) {
    val height = plan.fields[position.y][position.x]
    for (direction in plan.directions) {
        val nextPosition = position + direction
        if (plan.isValid(nextPosition) && !path.contains(nextPosition)) {
            val nextHeight = plan.fields[nextPosition.y][nextPosition.x]
            if (nextHeight == height + 1) {
                if (nextHeight == 9) {
                    if (nineCondition(nextPosition, search)) search.reach(nextPosition)
                } else {
                    val nextPath = mutableListOf<XY>()
                    nextPath.addAll(path)
                    nextPath.add(nextPosition)
                    dfs(nextPosition, nextPath, plan, search, nineCondition)
                }
            }
        }
    }
}

fun readInput(name: String): Plan {
    val lines = resourceFile(name).readLines()
    val width = lines[0].length
    val height = lines.size
    val fields = Array(height) { IntArray(width) }
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            fields[y][x] = lines[y][x].digitToInt()
        }
    }
    return Plan(width, height, fields)
}

class Plan(val width: Int, val height: Int, val fields: Array<IntArray>) {
    val directions: Array<XY> = arrayOf(
        /* left  */ XY(-1, 0),
        /* up    */ XY(0, -1),
        /* right */ XY(1, 0),
        /* down  */ XY(0, 1)
    )
    val trailheads: List<XY>

    init {
        trailheads = mutableListOf()
        for (y in fields.indices) {
            for (x in fields[y].indices) {
                if (fields[y][x] == 0) trailheads.add(XY(x, y))
            }
        }
    }

    fun isValid(xy: XY): Boolean = xy.y in 0..<height && xy.x in 0..<width
}

class Search {
    private val reachedNines: MutableList<XY> = mutableListOf()
    fun reachedBefore(xy: XY): Boolean = reachedNines.contains(xy)
    fun reach(xy: XY) = reachedNines.add(xy)
    fun countNines(): Int = reachedNines.size
}

data class XY(val x: Int, val y: Int) {
    operator fun plus(other: XY): XY = XY(x + other.x, y + other.y)
}