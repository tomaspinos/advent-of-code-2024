package day21

import common.XY
import day21.DirKey.*

fun main() {
    computeNumPaths()
    computeDirPaths()
    part1(
        listOf(
            listOf(3, 1, 9, A),
            listOf(6, 7, 0, A),
            listOf(3, 4, 9, A),
            listOf(9, 6, 4, A),
            listOf(5, 8, 6, A)
        )
    )
}

const val A = 10

val numKeys = mapOf(
    7 to XY(0, 0), 8 to XY(1, 0), 9 to XY(2, 0),
    4 to XY(0, 1), 5 to XY(1, 1), 6 to XY(2, 1),
    1 to XY(0, 2), 2 to XY(1, 2), 3 to XY(2, 2),
    0 to XY(1, 3), A to XY(2, 3)
)

val dirKeys = mapOf(
    UP to XY(1, 0), PRESS to XY(2, 0),
    LEFT to XY(0, 1), DOWN to XY(1, 1), RIGHT to XY(2, 1)
)

val numPaths = Array(11) { Array(11) { mutableListOf<Path>() } }
val dirPaths = Array(5) { Array(5) { mutableListOf<Path>() } }

fun computeNumPaths() {
    numKeys.keys.forEach(::computeNumPaths)
}

fun computeDirPaths() {
    dirKeys.keys.forEach(::computeDirPaths)
}

fun computeNumPaths(num: Int) {
    val startXY = numKeys[num]!!
    val visitedXYs = mutableSetOf<XY>()
    val queue = mutableListOf(startXY to Path(emptyList()))
    while (queue.isNotEmpty()) {
        val (xy, path) = queue.removeFirst()
        for ((nextNum, nextXY, dirKey) in numKeyNeighbors(xy)) {
            if (nextXY !in visitedXYs) {
                val nextPath = path.extend(dirKey)
                numPaths[num][nextNum] += nextPath
                queue += nextXY to nextPath
            }
        }
        visitedXYs += xy
    }
}

fun computeDirPaths(dirKey: DirKey) {
    val startXY = dirKeys[dirKey]!!
    val visitedXYs = mutableSetOf<XY>()
    val queue = mutableListOf(startXY to Path(emptyList()))
    while (queue.isNotEmpty()) {
        val (xy, path) = queue.removeFirst()
        for ((toDirKey, nextXY, throughDirKey) in dirKeyNeighbors(xy)) {
            if (nextXY !in visitedXYs) {
                val nextPath = path.extend(throughDirKey)
                dirPaths[dirKey.ordinal][toDirKey.ordinal] += nextPath
                queue += nextXY to nextPath
            }
        }
        visitedXYs += xy
    }
}

fun numKeyNeighbors(xy: XY): List<Triple<Int, XY, DirKey>> {
    return listOf(xy.left() to LEFT, xy.up() to UP, xy.right() to RIGHT, xy.down() to DOWN)
        .filter { (xy, _) -> numKeys.values.contains(xy) }
        .map { (xy, dirKey) -> Triple(numKeys.entries.find { it.value == xy }!!.key, xy, dirKey) }
}

fun dirKeyNeighbors(xy: XY): List<Triple<DirKey, XY, DirKey>> {
    return listOf(xy.left() to LEFT, xy.up() to UP, xy.right() to RIGHT, xy.down() to DOWN)
        .filter { (xy, _) -> dirKeys.values.contains(xy) }
        .map { (xy, dirKey) -> Triple(dirKeys.entries.find { it.value == xy }!!.key, xy, dirKey) }
}

fun part1(codes: List<List<Int>>) {
    TODO("Not yet implemented")
}

class Path(val dirKeys: List<DirKey>) {
    fun extend(dirKey: DirKey): Path = Path(dirKeys + dirKey)
    fun join(other: Path): Path = Path(dirKeys + other.dirKeys)
    fun print(code: List<Int>) {
        print("${code.subList(0, 3).joinToString("") { it.toString() } + 'A'}: ")
        for (dirKey in dirKeys) print(dirKey.ch)
        println()
    }

    override fun toString(): String {
        return dirKeys.map(DirKey::ch).joinToString("")
    }
}

enum class DirKey(val ch: Char) {
    UP('^'), DOWN('v'), LEFT('<'), RIGHT('>'), PRESS('A')
}
