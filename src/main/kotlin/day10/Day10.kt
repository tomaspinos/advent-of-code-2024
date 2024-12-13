package day10

import common.XY
import common.directions
import common.resourceFile

fun main() {
    process("/day10.txt")
}

fun process(name: String) {
    val plan = readInput(name)
    println(plan.scores())
    println(plan.ratings())
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

class Plan(private val width: Int, private val height: Int, private val fields: Array<IntArray>) {
    private val trailheads: List<XY>

    init {
        trailheads = mutableListOf()
        for (y in fields.indices) {
            for (x in fields[y].indices) {
                if (fields[y][x] == 0) trailheads.add(XY(x, y))
            }
        }
    }

    fun ratings() = trailheads.sumOf { dfs(it) { _: XY, _: Search -> true } }

    fun scores() = trailheads.sumOf { dfs(it) { pos, search -> !search.reachedBefore(pos) } }

    private fun dfs(trailhead: XY, if9Good: (XY, Search) -> Boolean): Int {
        val search = Search()
        dfs(trailhead, listOf(trailhead), search, if9Good)
        return search.count9s()
    }

    private fun dfs(pos: XY, path: List<XY>, search: Search, if9Good: (XY, Search) -> Boolean) {
        val height = fields[pos.y][pos.x]
        for (direction in directions) {
            val nextPos = pos + direction
            if (nextPos.isValid(this.width, this.height) && !path.contains(nextPos)) {
                val nextHeight = fields[nextPos.y][nextPos.x]
                if (nextHeight == height + 1) {
                    if (nextHeight == 9) {
                        if (if9Good(nextPos, search)) search.reach(nextPos)
                    } else {
                        dfs(nextPos, path + nextPos, search, if9Good)
                    }
                }
            }
        }
    }
}

class Search {
    private val reached9s: MutableList<XY> = mutableListOf()
    fun reachedBefore(xy: XY): Boolean = reached9s.contains(xy)
    fun reach(xy: XY) = reached9s.add(xy)
    fun count9s(): Int = reached9s.size
}