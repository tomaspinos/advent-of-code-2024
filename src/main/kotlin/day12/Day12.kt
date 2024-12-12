package day12

import common.resourceFile

fun main() {
    process("/day12.txt")
}

var garden: Array<Array<Char>> = Array(0) { Array(0) { ' ' } }
var width = 0
var height = 0

val directions = arrayOf(
   /* left  */ XY(-1, 0),
   /* up    */ XY(0, -1),
   /* right */ XY(1, 0),
   /* down  */ XY(0, 1)
)

fun process(name: String) {
    readInput(name)

    val visited = mutableSetOf<XY>()
    val regions = mutableListOf<Region>()

    for (y in garden.indices) {
        for (x in garden[y].indices) {
            val xy = XY(x, y)
            if (!visited.contains(xy)) {
                val region = Region(garden[y][x])
                explore(xy, region, visited)
                regions.add(region)
            }
        }
    }

    val price1 =
        regions.sumOf { it.plants.size * it.plants.sumOf { plant -> 4 - plant.neighborCount } }
    println(price1)

    val price2 = regions.sumOf { it.plants.size * countSides(it) }
    println(price2)
}

fun explore(xy: XY, region: Region, visited: MutableSet<XY>) {
    val plantCh = garden[xy.y][xy.x]
    visited.add(xy)
    val samePlantNeighborXYs = directions
        .map { xy + it }
        .filter { isValid(it) }
        .filter { garden[it.y][it.x] == plantCh }
    region.plants.add(Plant(xy, samePlantNeighborXYs.size))
    for (samePlantNeighborXY in samePlantNeighborXYs) {
        if (!visited.contains(samePlantNeighborXY)) {
            explore(samePlantNeighborXY, region, visited)
        }
    }
}

fun countSides(region: Region): Int {
    val regionXys = region.plants.map { it.xy }.toSet()
    val borderXys = region.plants
        .filter { it.neighborCount < 4 }
        .map { it.xy }

    var sideCount = 0

    borderXys.groupBy { it.y }.values.forEach { row ->
        val rowSortedByX = row.sortedBy { it.x }

        val withNoNeighborUp = rowSortedByX.filter { xy -> !regionXys.contains(xy.up()) }
        sideCount += countRowSections(withNoNeighborUp)

        val withNoNeighborDown = rowSortedByX.filter { xy -> !regionXys.contains(xy.down()) }
        sideCount += countRowSections(withNoNeighborDown)
    }

    borderXys.groupBy { it.x }.values.forEach { column ->
        val columnSortedByY = column.sortedBy { it.y }

        val withNoNeighborLeft = columnSortedByY.filter { xy -> !regionXys.contains(xy.left()) }
        sideCount += countColumnSections(withNoNeighborLeft)

        val withNoNeighborRight = columnSortedByY.filter { xy -> !regionXys.contains(xy.right()) }
        sideCount += countColumnSections(withNoNeighborRight)
    }

    return sideCount
}

fun countRowSections(xys: List<XY>): Int {
    if (xys.isEmpty()) return 0
    var count = 1
    for (i in 1..<xys.size) {
        if (xys[i].x != xys[i - 1].x + 1) count++
    }
    return count
}

fun countColumnSections(xys: List<XY>): Int {
    if (xys.isEmpty()) return 0
    var count = 1
    for (i in 1..<xys.size) {
        if (xys[i].y != xys[i - 1].y + 1) count++
    }
    return count
}

fun readInput(name: String) {
    val lines = resourceFile(name).readLines()
    height = lines.size
    width = lines[0].length
    garden = Array(height) { Array(width) { ' ' } }
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            garden[y][x] = lines[y][x]
        }
    }
}

fun isValid(xy: XY): Boolean = xy.y in garden.indices && xy.x in garden[0].indices

data class XY(val x: Int, val y: Int) {
    operator fun plus(other: XY) = XY(x + other.x, y + other.y)
    fun up(): XY = XY(x, y - 1)
    fun down(): XY = XY(x, y + 1)
    fun left(): XY = XY(x - 1, y)
    fun right(): XY = XY(x + 1, y)
}

data class Plant(val xy: XY, val neighborCount: Int)

data class Region(val plantCh: Char, val plants: MutableList<Plant> = mutableListOf())