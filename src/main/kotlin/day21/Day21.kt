package day21

import day21.DirKey.*

fun main() {
    initializeKeyPaths()
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

fun part1(codes: List<List<Int>>) {
    TODO("Not yet implemented")
}

class Path {
    val dirKeyPresses: List<DirKeyPress>

    constructor(vararg input: DirKeyPress) {
        dirKeyPresses = input.toList()
    }

    constructor(input: List<DirKeyPress>) {
        dirKeyPresses = input
    }

    companion object {
        fun join(paths: List<Path>): Path {
            return Path(paths.flatMap { it.dirKeyPresses })
        }
    }

    fun completeIncludingVariants(): List<Path> {
        if (dirKeyPresses.size == 1) {
            return listOf(Path(dirKeyPresses + DirKeyPress(PRESS)))
        } else if (dirKeyPresses.size == 2) {
            return listOf(
                Path(dirKeyPresses + DirKeyPress(PRESS)),
                Path(listOf(dirKeyPresses[1], dirKeyPresses[0]) + DirKeyPress(PRESS))
            )
        } else if (dirKeyPresses.isEmpty()) {
            return emptyList()
        } else {
            throw IllegalStateException()
        }
    }

    fun expand(): List<DirKey> {
        val dirKeys = mutableListOf<DirKey>()
        for (dirKeyPress in dirKeyPresses) {
            for (i in 0..<dirKeyPress.count) dirKeys += dirKeyPress.dirKey
        }
        return dirKeys
    }

    fun print(code: List<Int>) {
        print("${code.subList(0, 3).joinToString("") { it.toString() } + 'A'}: ")
        for (dirKey in expand()) print(dirKey.ch)
        println()
    }
}

val numPaths = Array(11) { Array(11) { Path() } }
val numPaths2 = Array(11) { Array(11) { emptyList<Path>() } }
val dirPaths = Array(5) { Array(5) { Path() } }
val dirPaths2 = Array(5) { Array(5) { emptyList<Path>() } }

const val A = 10

fun type1(code: List<Int>, index: Int, currentNum: Int, paths: List<Path>): List<Path> {
    if (index < code.size) {
        val c = code[index]
        val pathsToC = numPaths2[currentNum][c]
        val allPaths = mutableListOf<Path>()
        for (path in pathsToC) {
            allPaths += type1(code, index + 1, c, paths + path)
        }
        return allPaths
    } else {
        return listOf(Path.join(paths))
    }
}

fun type2(path: Path, index: Int, currentKey: DirKey, paths: List<Path>): List<Path> {
    return listOf()
}

fun typeX(code: List<Int>) {
    val paths1 = type1(code, 0, A, emptyList())
    for (path1 in paths1) {
        //type2(path1, 0, )
    }
}

fun initializeKeyPaths() {
    numPaths[0][0] = Path()
    numPaths[0][1] = Path(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[0][2] = Path(DirKeyPress(UP))
    numPaths[0][3] = Path(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[0][4] = Path(DirKeyPress(UP, 2), DirKeyPress(LEFT))
    numPaths[0][5] = Path(DirKeyPress(UP, 2))
    numPaths[0][6] = Path(DirKeyPress(UP, 2), DirKeyPress(RIGHT))
    numPaths[0][7] = Path(DirKeyPress(UP, 3), DirKeyPress(LEFT))
    numPaths[0][8] = Path(DirKeyPress(UP, 3))
    numPaths[0][9] = Path(DirKeyPress(UP, 3), DirKeyPress(RIGHT))
    numPaths[0][A] = Path(DirKeyPress(RIGHT))

    numPaths[1][0] = Path(DirKeyPress(RIGHT), DirKeyPress(DOWN))
    numPaths[1][1] = Path()
    numPaths[1][2] = Path(DirKeyPress(RIGHT))
    numPaths[1][3] = Path(DirKeyPress(RIGHT, 2))
    numPaths[1][4] = Path(DirKeyPress(UP))
    numPaths[1][5] = Path(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[1][6] = Path(DirKeyPress(UP), DirKeyPress(RIGHT, 2))
    numPaths[1][7] = Path(DirKeyPress(UP, 2))
    numPaths[1][8] = Path(DirKeyPress(UP, 2), DirKeyPress(RIGHT))
    numPaths[1][9] = Path(DirKeyPress(UP, 2), DirKeyPress(RIGHT, 2))
    numPaths[1][A] = Path(DirKeyPress(RIGHT, 2), DirKeyPress(DOWN))

    numPaths[2][0] = Path(DirKeyPress(DOWN))
    numPaths[2][1] = Path(DirKeyPress(LEFT))
    numPaths[2][2] = Path()
    numPaths[2][3] = Path(DirKeyPress(RIGHT))
    numPaths[2][4] = Path(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[2][5] = Path(DirKeyPress(UP))
    numPaths[2][6] = Path(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[2][7] = Path(DirKeyPress(UP, 2), DirKeyPress(LEFT))
    numPaths[2][8] = Path(DirKeyPress(UP, 2))
    numPaths[2][9] = Path(DirKeyPress(UP, 2), DirKeyPress(RIGHT))
    numPaths[2][A] = Path(DirKeyPress(DOWN), DirKeyPress(RIGHT))

    numPaths[3][0] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[3][1] = Path(DirKeyPress(LEFT, 2))
    numPaths[3][2] = Path(DirKeyPress(LEFT))
    numPaths[3][3] = Path()
    numPaths[3][4] = Path(DirKeyPress(UP), DirKeyPress(LEFT, 2))
    numPaths[3][5] = Path(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[3][6] = Path(DirKeyPress(UP))
    numPaths[3][7] = Path(DirKeyPress(UP, 2), DirKeyPress(LEFT, 2))
    numPaths[3][8] = Path(DirKeyPress(UP, 2), DirKeyPress(LEFT))
    numPaths[3][9] = Path(DirKeyPress(UP, 2))
    numPaths[3][A] = Path(DirKeyPress(DOWN))

    numPaths[4][0] = Path(DirKeyPress(RIGHT), DirKeyPress(DOWN, 2))
    numPaths[4][1] = Path(DirKeyPress(DOWN))
    numPaths[4][2] = Path(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    numPaths[4][3] = Path(DirKeyPress(DOWN), DirKeyPress(RIGHT, 2))
    numPaths[4][4] = Path()
    numPaths[4][5] = Path(DirKeyPress(RIGHT))
    numPaths[4][6] = Path(DirKeyPress(RIGHT, 2))
    numPaths[4][7] = Path(DirKeyPress(UP))
    numPaths[4][8] = Path(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[4][9] = Path(DirKeyPress(UP), DirKeyPress(RIGHT, 2))
    numPaths[4][A] = Path(DirKeyPress(RIGHT, 2), DirKeyPress(DOWN, 2))

    numPaths[5][0] = Path(DirKeyPress(DOWN, 2))
    numPaths[5][1] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[5][2] = Path(DirKeyPress(DOWN))
    numPaths[5][3] = Path(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    numPaths[5][4] = Path(DirKeyPress(LEFT))
    numPaths[5][5] = Path()
    numPaths[5][6] = Path(DirKeyPress(RIGHT))
    numPaths[5][7] = Path(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[5][8] = Path(DirKeyPress(UP))
    numPaths[5][9] = Path(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[5][A] = Path(DirKeyPress(DOWN, 2), DirKeyPress(RIGHT))

    numPaths[6][0] = Path(DirKeyPress(DOWN, 2), DirKeyPress(LEFT))
    numPaths[6][1] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT, 2))
    numPaths[6][2] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[6][3] = Path(DirKeyPress(DOWN))
    numPaths[6][4] = Path(DirKeyPress(LEFT, 2))
    numPaths[6][5] = Path(DirKeyPress(LEFT))
    numPaths[6][6] = Path()
    numPaths[6][7] = Path(DirKeyPress(UP), DirKeyPress(LEFT, 2))
    numPaths[6][8] = Path(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[6][9] = Path(DirKeyPress(UP))
    numPaths[6][A] = Path(DirKeyPress(DOWN, 2))

    numPaths[7][0] = Path(DirKeyPress(RIGHT), DirKeyPress(DOWN, 3))
    numPaths[7][1] = Path(DirKeyPress(DOWN, 2))
    numPaths[7][2] = Path(DirKeyPress(DOWN, 2), DirKeyPress(RIGHT))
    numPaths[7][3] = Path(DirKeyPress(DOWN, 2), DirKeyPress(RIGHT, 2))
    numPaths[7][4] = Path(DirKeyPress(DOWN))
    numPaths[7][5] = Path(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    numPaths[7][6] = Path(DirKeyPress(DOWN), DirKeyPress(RIGHT, 2))
    numPaths[7][7] = Path()
    numPaths[7][8] = Path(DirKeyPress(RIGHT))
    numPaths[7][9] = Path(DirKeyPress(RIGHT, 2))
    numPaths[7][A] = Path(DirKeyPress(RIGHT, 2), DirKeyPress(DOWN, 3))

    numPaths[8][0] = Path(DirKeyPress(DOWN, 3))
    numPaths[8][1] = Path(DirKeyPress(DOWN, 2), DirKeyPress(LEFT))
    numPaths[8][2] = Path(DirKeyPress(DOWN, 2))
    numPaths[8][3] = Path(DirKeyPress(DOWN, 2), DirKeyPress(RIGHT))
    numPaths[8][4] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[8][5] = Path(DirKeyPress(DOWN))
    numPaths[8][6] = Path(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    numPaths[8][7] = Path(DirKeyPress(LEFT))
    numPaths[8][8] = Path()
    numPaths[8][9] = Path(DirKeyPress(RIGHT))
    numPaths[8][A] = Path(DirKeyPress(DOWN, 3), DirKeyPress(RIGHT))

    numPaths[9][0] = Path(DirKeyPress(DOWN, 3), DirKeyPress(LEFT))
    numPaths[9][1] = Path(DirKeyPress(DOWN, 2), DirKeyPress(LEFT, 2))
    numPaths[9][2] = Path(DirKeyPress(DOWN, 2), DirKeyPress(LEFT))
    numPaths[9][3] = Path(DirKeyPress(DOWN, 2))
    numPaths[9][4] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT, 2))
    numPaths[9][5] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[9][6] = Path(DirKeyPress(DOWN))
    numPaths[9][7] = Path(DirKeyPress(LEFT, 2))
    numPaths[9][8] = Path(DirKeyPress(LEFT))
    numPaths[9][9] = Path()
    numPaths[9][A] = Path(DirKeyPress(DOWN, 3))

    numPaths[A][0] = Path(DirKeyPress(LEFT))
    numPaths[A][1] = Path(DirKeyPress(UP), DirKeyPress(LEFT, 2))
    numPaths[A][2] = Path(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[A][3] = Path(DirKeyPress(UP))
    numPaths[A][4] = Path(DirKeyPress(UP, 2), DirKeyPress(LEFT, 2))
    numPaths[A][5] = Path(DirKeyPress(UP, 2), DirKeyPress(LEFT))
    numPaths[A][6] = Path(DirKeyPress(UP, 2))
    numPaths[A][7] = Path(DirKeyPress(UP, 3), DirKeyPress(LEFT, 2))
    numPaths[A][8] = Path(DirKeyPress(UP, 3), DirKeyPress(LEFT))
    numPaths[A][9] = Path(DirKeyPress(UP, 3))
    numPaths[A][A] = Path()

    dirPaths[UP.ordinal][UP.ordinal] = Path()
    dirPaths[UP.ordinal][DOWN.ordinal] = Path(DirKeyPress(DOWN))
    dirPaths[UP.ordinal][LEFT.ordinal] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT))
    dirPaths[UP.ordinal][RIGHT.ordinal] = Path(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    dirPaths[UP.ordinal][PRESS.ordinal] = Path(DirKeyPress(RIGHT))

    dirPaths[DOWN.ordinal][UP.ordinal] = Path(DirKeyPress(UP))
    dirPaths[DOWN.ordinal][DOWN.ordinal] = Path()
    dirPaths[DOWN.ordinal][LEFT.ordinal] = Path(DirKeyPress(LEFT))
    dirPaths[DOWN.ordinal][RIGHT.ordinal] = Path(DirKeyPress(RIGHT))
    dirPaths[DOWN.ordinal][PRESS.ordinal] = Path(DirKeyPress(UP), DirKeyPress(RIGHT))

    dirPaths[LEFT.ordinal][UP.ordinal] = Path(DirKeyPress(RIGHT), DirKeyPress(UP))
    dirPaths[LEFT.ordinal][DOWN.ordinal] = Path(DirKeyPress(RIGHT))
    dirPaths[LEFT.ordinal][LEFT.ordinal] = Path()
    dirPaths[LEFT.ordinal][RIGHT.ordinal] = Path(DirKeyPress(RIGHT, 2))
    dirPaths[LEFT.ordinal][PRESS.ordinal] = Path(DirKeyPress(RIGHT, 2), DirKeyPress(UP))

    dirPaths[RIGHT.ordinal][UP.ordinal] = Path(DirKeyPress(UP), DirKeyPress(LEFT))
    dirPaths[RIGHT.ordinal][DOWN.ordinal] = Path(DirKeyPress(LEFT))
    dirPaths[RIGHT.ordinal][LEFT.ordinal] = Path(DirKeyPress(LEFT, 2))
    dirPaths[RIGHT.ordinal][RIGHT.ordinal] = Path()
    dirPaths[RIGHT.ordinal][PRESS.ordinal] = Path(DirKeyPress(UP))

    dirPaths[PRESS.ordinal][UP.ordinal] = Path(DirKeyPress(LEFT))
    dirPaths[PRESS.ordinal][DOWN.ordinal] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT))
    dirPaths[PRESS.ordinal][LEFT.ordinal] = Path(DirKeyPress(DOWN), DirKeyPress(LEFT, 2))
    dirPaths[PRESS.ordinal][RIGHT.ordinal] = Path(DirKeyPress(DOWN))
    dirPaths[PRESS.ordinal][PRESS.ordinal] = Path()

    for (i in numPaths.indices) {
        for (j in numPaths[i].indices) {
            numPaths2[i][j] = numPaths[i][j].completeIncludingVariants()
        }
    }

    for (i in dirPaths.indices) {
        for (j in dirPaths[i].indices) {
            dirPaths2[i][j] = dirPaths[i][j].completeIncludingVariants()
        }
    }
}

class DirKeypad1 {

    fun type(c1: Int, c2: Int, c3: Int): List<DirKey> {
        return listOf()
    }
}

class DirKeypad2 {

    fun type(c1: Int, c2: Int, c3: Int): List<DirKey> {
        return listOf()
    }
}

enum class DirKey(val ch: Char) {
    UP('^'), DOWN('v'), LEFT('<'), RIGHT('>'), PRESS('A')
}

data class DirKeyPress(val dirKey: DirKey, val count: Int = 1)