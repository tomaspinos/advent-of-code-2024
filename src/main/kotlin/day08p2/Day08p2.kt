package day08p2

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
    val city = City(lines[0].length, lines.size)
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            val ch = lines[y][x]
            if (ch != '.') {
                city.add(Antenna(XY(x, y), ch))
            }
        }
    }
    return city
}

data class XY(val x: Int, val y: Int) {
    fun plus(xy: XY): XY = XY(x + xy.x, y + xy.y)
    fun minus(xy: XY): XY = XY(x - xy.x, y - xy.y)
}

data class Antenna(val xy: XY, val frequency: Char)

class City(val width: Int, val height: Int) {
    val plan: Array<Array<Boolean>> = Array(height) { Array(width) { false } }
    val antennas = mutableListOf<Antenna>()
    val antinodes = mutableSetOf<XY>()

    fun add(antenna: Antenna) {
        antennas.add(antenna)
        plan[antenna.xy.y][antenna.xy.x] = true
    }

    fun computeAntinodes() {
        for (i in antennas.indices) {
            for (j in antennas.indices) {
                if (i == j) continue
                val a1 = antennas[i]
                val a2 = antennas[j]
                if (a1.frequency != a2.frequency) continue
                for (antinode in computeAntinodesXY(a1.xy, a2.xy)) {
                    antinodes.add(antinode)
                    plan[antinode.y][antinode.x] = true
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
        val dif = a2.minus(a1)
        var a1m = a1.minus(dif)
        while (isValidXY(a1m)) {
            result.add(a1m)
            a1m = a1m.minus(dif)
        }
        var a1p = a1.plus(dif)
        while (isValidXY(a1p)) {
            result.add(a1p)
            a1p = a1p.plus(dif)
        }
        return result
    }

    fun isValidXY(xy: XY): Boolean = xy.y in plan.indices && xy.x in plan[0].indices

    fun print() {
        for (y in plan.indices) {
            for (x in plan[y].indices) {
                if (plan[y][x]) {
                    val maybeAntenna = antennas.find { it.xy == XY(x, y) }
                    if (maybeAntenna != null) {
                        print(maybeAntenna.frequency)
                    } else {
                        print('#')
                    }
                } else {
                    print('.')
                }
            }
            println()
        }
    }
}