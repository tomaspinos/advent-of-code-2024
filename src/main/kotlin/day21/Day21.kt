package day21

import common.XY
import day21.Key.*

fun main() {
    computeNumPaths()
    computeDirPaths()
    val input = listOf(
        listOf(_3, _1, _9, A),
        listOf(_6, _7, _0, A),
        listOf(_3, _4, _9, A),
        listOf(_9, _6, _4, A),
        listOf(_5, _8, _6, A)
    )
    part1(input)
    //part2(input)
}

fun part1(codes: List<List<Key>>) {
    val result = codes.sumOf { code ->
        val pressCount = count(code, 3, numPaths)
        overallCost(code, pressCount)
    }
    println(result)
}

fun part2(codes: List<List<Key>>) {
    val result = codes.sumOf { code ->
        val pressCount = count(code, 26, numPaths)
        overallCost(code, pressCount)
    }
    println(result)
}

fun overallCost(code: List<Key>, pressCount: Long): Long {
    val num = codeToNum(code)
    println("${num}A: $pressCount * $num")
    return pressCount * num
}

val numKeyboard = mapOf(
    _7 to XY(0, 0), _8 to XY(1, 0), _9 to XY(2, 0),
    _4 to XY(0, 1), _5 to XY(1, 1), _6 to XY(2, 1),
    _1 to XY(0, 2), _2 to XY(1, 2), _3 to XY(2, 2),
    _0 to XY(1, 3), A to XY(2, 3)
)

val dirKeyboard = mapOf(
    UP to XY(1, 0), A to XY(2, 0),
    LEFT to XY(0, 1), DOWN to XY(1, 1), RIGHT to XY(2, 1)
)

val numPaths: MutableMap<Key, MutableMap<Key, MutableList<Path>>> = mutableMapOf()
val dirPaths: MutableMap<Key, MutableMap<Key, MutableList<Path>>> = mutableMapOf()

fun count(code: List<Key>, level: Int, paths: Map<Key, Map<Key, List<Path>>>): Long {
    println("$code, $level")
    if (level == 0) {
        return code.size.toLong()
    } else {
        val keyToKeyTransitions = (listOf(A) + code).zipWithNext()
        return keyToKeyTransitions.sumOf { (from, to) ->
            val pathsFromTo = paths[from]!![to]!!
            pathsFromTo.minOf { path ->
                count(path.extend(A).keys, level - 1, dirPaths)
            }
        }
    }
}

fun codeToNum(code: List<Key>): Int {
    return code.filter { it in setOf(_0, _1, _2, _3, _4, _5, _6, _7, _8, _9) }
        .map { it.ch }
        .joinToString("")
        .toInt()
}

fun computeNumPaths() {
    numKeyboard.keys.forEach(::computeNumPaths)
}

fun computeDirPaths() {
    dirKeyboard.keys.forEach(::computeDirPaths)
}

fun computeNumPaths(num: Key) {
    numPaths[num] = mutableMapOf()
    numPaths[num]!![num] = mutableListOf(Path(listOf()))
    val startXY = numKeyboard[num]!!
    val visitedXYs = mutableSetOf<XY>()
    val queue = mutableListOf(startXY to Path(emptyList()))
    while (queue.isNotEmpty()) {
        val (xy, path) = queue.removeFirst()
        for ((nextNum, nextXY, throughKey) in numKeyNeighbors(xy)) {
            if (nextXY !in visitedXYs) {
                val nextPath = path.extend(throughKey)
                numPaths[num]!!.getOrPut(nextNum) { mutableListOf() } += nextPath
                queue += nextXY to nextPath
            }
        }
        visitedXYs += xy
    }
}

fun computeDirPaths(dir: Key) {
    dirPaths[dir] = mutableMapOf()
    dirPaths[dir]!![dir] = mutableListOf(Path(listOf()))
    val startXY = dirKeyboard[dir]!!
    val visitedXYs = mutableSetOf<XY>()
    val queue = mutableListOf(startXY to Path(emptyList()))
    while (queue.isNotEmpty()) {
        val (xy, path) = queue.removeFirst()
        for ((toDir, nextXY, throughKey) in dirKeyNeighbors(xy)) {
            if (nextXY !in visitedXYs) {
                val nextPath = path.extend(throughKey)
                dirPaths[dir]!!.getOrPut(toDir) { mutableListOf() } += nextPath
                queue += nextXY to nextPath
            }
        }
        visitedXYs += xy
    }
}

fun numKeyNeighbors(xy: XY): List<Triple<Key, XY, Key>> {
    return listOf(xy.left() to LEFT, xy.up() to UP, xy.right() to RIGHT, xy.down() to DOWN)
        .filter { (xy, _) -> numKeyboard.values.contains(xy) }
        .map { (xy, key) ->
            Triple(numKeyboard.entries.find { it.value == xy }!!.key, xy, key)
        }
}

fun dirKeyNeighbors(xy: XY): List<Triple<Key, XY, Key>> {
    return listOf(xy.left() to LEFT, xy.up() to UP, xy.right() to RIGHT, xy.down() to DOWN)
        .filter { (xy, _) -> dirKeyboard.values.contains(xy) }
        .map { (xy, dirKey) ->
            Triple(
                dirKeyboard.entries.find { it.value == xy }!!.key,
                xy,
                dirKey
            )
        }
}

class Path(val keys: List<Key>) {
    fun extend(key: Key): Path = Path(keys + key)

    fun join(other: Path): Path = Path(keys + other.keys)

    override fun toString(): String {
        return keys.map(Key::ch).joinToString("")
    }
}

enum class Key(val ch: Char) {
    _0('0'), _1('1'), _2('2'), _3('3'), _4('4'), _5('5'), _6('6'), _7('7'), _8('8'), _9('9'),
    UP('^'), DOWN('v'), LEFT('<'), RIGHT('>'),
    A('A')
}
