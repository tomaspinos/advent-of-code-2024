package day08p2

import common.XY
import common.resourceFile

fun main() {
    process("/day08.txt")
}

fun process(name: String) {
    val city = readInput(name)
    city.computeAntinodes()
    city.print()
    println(city.countAntinodes())
}

fun readInput(name: String): City {
    val lines = resourceFile(name).readLines()
    val width = lines[0].length
    val height = lines.size
    val antennas = mutableListOf<Antenna>()
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            val ch = lines[y][x]
            if (ch != '.') {
                antennas.add(Antenna(XY(x, y), ch))
            }
        }
    }
    return City(width, height, antennas)
}

data class Antenna(val xy: XY, val frequency: Char)

class City(val width: Int, val height: Int, val antennas: List<Antenna>) {
    val antinodes = mutableSetOf<XY>()

    fun computeAntinodes() {
        for (i in antennas.indices) {
            for (j in i + 1..<antennas.size) {
                val a1 = antennas[i]
                val a2 = antennas[j]
                if (a1.frequency != a2.frequency) continue
                for (antinode in computeAntinodesXY(a1.xy, a2.xy)) {
                    antinodes.add(antinode)
                }
            }
        }
    }

    fun countAntinodes(): Int = antinodes.size

    /**
     * ..........
     * ...#......
     * ..........
     * ....a.....
     * ..........
     * .....a....
     * ..........
     * ......#...
     * ..........
     * ..........
     *
     * a1(4, 3)
     * a2(5, 5)
     *
     * dif = a2 - a1 = (1, 2)
     *
     * an1 = a1 - dif = (3, 1)
     * an2 = a2 + dif = (6, 7)
     */
    fun computeAntinodesXY(a1: XY, a2: XY): List<XY> {
        val result = mutableListOf<XY>()
        val dif = a2 - a1
        var a1m = a1
        while (a1m.isValid(width, height)) {
            result.add(a1m)
            a1m -= dif
        }
        var a1p = a1
        while (a1p.isValid(width, height)) {
            result.add(a1p)
            a1p += dif
        }
        return result
    }

    fun print() {
        for (y in 0..<height) {
            for (x in 0..<width) {
                val maybeAntenna = antennas.find { it.xy == XY(x, y) }
                if (maybeAntenna != null) print(maybeAntenna.frequency)
                else if (antinodes.contains(XY(x, y))) print('#')
                else print('.')
            }
            println()
        }
    }
}