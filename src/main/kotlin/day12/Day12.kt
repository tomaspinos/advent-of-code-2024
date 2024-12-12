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

    val price = regions.sumOf { it.plants.size * it.plants.sumOf { plant -> 4 - plant.neighborCount } }
    println(price)
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
}

data class Plant(val xy: XY, val neighborCount: Int)

data class Region(val plantCh: Char, val plants: MutableList<Plant> = mutableListOf())