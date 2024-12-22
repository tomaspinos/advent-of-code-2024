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

val numPaths = Array(11) { Array(11) { emptyList<DirKeyPress>() } }
val numPaths2 = Array(11) { Array(11) { emptyList<List<DirKeyPress>>() } }
val dirPaths = Array(5) { Array(5) { emptyList<DirKeyPress>() } }
val dirPaths2 = Array(5) { Array(5) { emptyList<List<DirKeyPress>>() } }

const val A = 10

var keypad0 = A

fun part1(codes: List<List<Int>>) {
    val dirKeys = mutableListOf<List<DirKey>>()
    for (code in codes) {
        val codeKeys = type3(code)
        print(code, codeKeys)
        dirKeys.add(codeKeys)
    }
    var result = 0
    for (i in codes.indices) {
        val keyLength = dirKeys[i].size
        val codeNum = codes[i].subList(0, 3).joinToString("") { it.toString() }.toInt()
        println("$keyLength * $codeNum")
        result += (keyLength * codeNum)
    }
    println(result)
}

fun type1(c: Int): List<List<DirKey>> {
    val dirKeys = expand(numPaths2[keypad0][c])
    keypad0 = c
    return dirKeys
}

fun type1(code: List<Int>): List<List<DirKey>> {
    var keypad0 = A
    for (c in code) {
        expand(numPaths2[keypad0][c])
    }
    return TODO()
}

fun type2(c: Int, keypad1: DirKey): List<List<DirKey>> {
    val type2Options = mutableListOf<List<DirKey>>()
    val type1Options = type1(c)
    for (type1Option in type1Options) {
        for (type1DirKey in type1Option) {
//            type2Options += expand(dirPaths[keypad1.ordinal][type1DirKey.ordinal])
        }
    }
    return type2Options
}

fun type3(code: List<Int>): List<DirKey> {
    val optimalOption = mutableListOf<DirKey>()
    for (c in code) {
        val type2Options = type2(c, PRESS)
        for (type2Option in type2Options) {
            for (type2DirKey in type2Option) {
//                dirKeys += expand(dirPaths[keypad2.ordinal][type2DirKey.ordinal])
            }
        }
    }
    return optimalOption
}

fun typeX(code: List<Int>) {

}

fun print(code: List<Int>, dirKeys: List<DirKey>) {
    print("${code.subList(0, 3).joinToString("") { it.toString() } + 'A'}: ")
    for (dirKey in dirKeys) print(dirKey.ch)
    println()
}

fun expand(dirKeyPresses: List<List<DirKeyPress>>): List<List<DirKey>> {
    return dirKeyPresses.map { expand0(it) }
}

fun expand0(dirKeyPresses: List<DirKeyPress>): List<DirKey> {
    val dirKeys = mutableListOf<DirKey>()
    for (dirKeyPress in dirKeyPresses) {
        for (i in 0..<dirKeyPress.count) dirKeys += dirKeyPress.dirKey
    }
    return dirKeys
}

fun initializeKeyPaths() {
    numPaths[0][0] = emptyList()
    numPaths[0][1] = listOf(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[0][2] = listOf(DirKeyPress(UP))
    numPaths[0][3] = listOf(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[0][4] = listOf(DirKeyPress(UP, 2), DirKeyPress(LEFT))
    numPaths[0][5] = listOf(DirKeyPress(UP, 2))
    numPaths[0][6] = listOf(DirKeyPress(UP, 2), DirKeyPress(RIGHT))
    numPaths[0][7] = listOf(DirKeyPress(UP, 3), DirKeyPress(LEFT))
    numPaths[0][8] = listOf(DirKeyPress(UP, 3))
    numPaths[0][9] = listOf(DirKeyPress(UP, 3), DirKeyPress(RIGHT))
    numPaths[0][A] = listOf(DirKeyPress(RIGHT))

    numPaths[1][0] = listOf(DirKeyPress(RIGHT), DirKeyPress(DOWN))
    numPaths[1][1] = emptyList()
    numPaths[1][2] = listOf(DirKeyPress(RIGHT))
    numPaths[1][3] = listOf(DirKeyPress(RIGHT, 2))
    numPaths[1][4] = listOf(DirKeyPress(UP))
    numPaths[1][5] = listOf(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[1][6] = listOf(DirKeyPress(UP), DirKeyPress(RIGHT, 2))
    numPaths[1][7] = listOf(DirKeyPress(UP, 2))
    numPaths[1][8] = listOf(DirKeyPress(UP, 2), DirKeyPress(RIGHT))
    numPaths[1][9] = listOf(DirKeyPress(UP, 2), DirKeyPress(RIGHT, 2))
    numPaths[1][A] = listOf(DirKeyPress(RIGHT, 2), DirKeyPress(DOWN))

    numPaths[2][0] = listOf(DirKeyPress(DOWN))
    numPaths[2][1] = listOf(DirKeyPress(LEFT))
    numPaths[2][2] = emptyList()
    numPaths[2][3] = listOf(DirKeyPress(RIGHT))
    numPaths[2][4] = listOf(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[2][5] = listOf(DirKeyPress(UP))
    numPaths[2][6] = listOf(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[2][7] = listOf(DirKeyPress(UP, 2), DirKeyPress(LEFT))
    numPaths[2][8] = listOf(DirKeyPress(UP, 2))
    numPaths[2][9] = listOf(DirKeyPress(UP, 2), DirKeyPress(RIGHT))
    numPaths[2][A] = listOf(DirKeyPress(DOWN), DirKeyPress(RIGHT))

    numPaths[3][0] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[3][1] = listOf(DirKeyPress(LEFT, 2))
    numPaths[3][2] = listOf(DirKeyPress(LEFT))
    numPaths[3][3] = emptyList()
    numPaths[3][4] = listOf(DirKeyPress(UP), DirKeyPress(LEFT, 2))
    numPaths[3][5] = listOf(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[3][6] = listOf(DirKeyPress(UP))
    numPaths[3][7] = listOf(DirKeyPress(UP, 2), DirKeyPress(LEFT, 2))
    numPaths[3][8] = listOf(DirKeyPress(UP, 2), DirKeyPress(LEFT))
    numPaths[3][9] = listOf(DirKeyPress(UP, 2))
    numPaths[3][A] = listOf(DirKeyPress(DOWN))

    numPaths[4][0] = listOf(DirKeyPress(RIGHT), DirKeyPress(DOWN, 2))
    numPaths[4][1] = listOf(DirKeyPress(DOWN))
    numPaths[4][2] = listOf(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    numPaths[4][3] = listOf(DirKeyPress(DOWN), DirKeyPress(RIGHT, 2))
    numPaths[4][4] = emptyList()
    numPaths[4][5] = listOf(DirKeyPress(RIGHT))
    numPaths[4][6] = listOf(DirKeyPress(RIGHT, 2))
    numPaths[4][7] = listOf(DirKeyPress(UP))
    numPaths[4][8] = listOf(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[4][9] = listOf(DirKeyPress(UP), DirKeyPress(RIGHT, 2))
    numPaths[4][A] = listOf(DirKeyPress(RIGHT, 2), DirKeyPress(DOWN, 2))

    numPaths[5][0] = listOf(DirKeyPress(DOWN, 2))
    numPaths[5][1] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[5][2] = listOf(DirKeyPress(DOWN))
    numPaths[5][3] = listOf(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    numPaths[5][4] = listOf(DirKeyPress(LEFT))
    numPaths[5][5] = emptyList()
    numPaths[5][6] = listOf(DirKeyPress(RIGHT))
    numPaths[5][7] = listOf(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[5][8] = listOf(DirKeyPress(UP))
    numPaths[5][9] = listOf(DirKeyPress(UP), DirKeyPress(RIGHT))
    numPaths[5][A] = listOf(DirKeyPress(DOWN, 2), DirKeyPress(RIGHT))

    numPaths[6][0] = listOf(DirKeyPress(DOWN, 2), DirKeyPress(LEFT))
    numPaths[6][1] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT, 2))
    numPaths[6][2] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[6][3] = listOf(DirKeyPress(DOWN))
    numPaths[6][4] = listOf(DirKeyPress(LEFT, 2))
    numPaths[6][5] = listOf(DirKeyPress(LEFT))
    numPaths[6][6] = emptyList()
    numPaths[6][7] = listOf(DirKeyPress(UP), DirKeyPress(LEFT, 2))
    numPaths[6][8] = listOf(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[6][9] = listOf(DirKeyPress(UP))
    numPaths[6][A] = listOf(DirKeyPress(DOWN, 2))

    numPaths[7][0] = listOf(DirKeyPress(RIGHT), DirKeyPress(DOWN, 3))
    numPaths[7][1] = listOf(DirKeyPress(DOWN, 2))
    numPaths[7][2] = listOf(DirKeyPress(DOWN, 2), DirKeyPress(RIGHT))
    numPaths[7][3] = listOf(DirKeyPress(DOWN, 2), DirKeyPress(RIGHT, 2))
    numPaths[7][4] = listOf(DirKeyPress(DOWN))
    numPaths[7][5] = listOf(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    numPaths[7][6] = listOf(DirKeyPress(DOWN), DirKeyPress(RIGHT, 2))
    numPaths[7][7] = emptyList()
    numPaths[7][8] = listOf(DirKeyPress(RIGHT))
    numPaths[7][9] = listOf(DirKeyPress(RIGHT, 2))
    numPaths[7][A] = listOf(DirKeyPress(RIGHT, 2), DirKeyPress(DOWN, 3))

    numPaths[8][0] = listOf(DirKeyPress(DOWN, 3))
    numPaths[8][1] = listOf(DirKeyPress(DOWN, 2), DirKeyPress(LEFT))
    numPaths[8][2] = listOf(DirKeyPress(DOWN, 2))
    numPaths[8][3] = listOf(DirKeyPress(DOWN, 2), DirKeyPress(RIGHT))
    numPaths[8][4] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[8][5] = listOf(DirKeyPress(DOWN))
    numPaths[8][6] = listOf(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    numPaths[8][7] = listOf(DirKeyPress(LEFT))
    numPaths[8][8] = emptyList()
    numPaths[8][9] = listOf(DirKeyPress(RIGHT))
    numPaths[8][A] = listOf(DirKeyPress(DOWN, 3), DirKeyPress(RIGHT))

    numPaths[9][0] = listOf(DirKeyPress(DOWN, 3), DirKeyPress(LEFT))
    numPaths[9][1] = listOf(DirKeyPress(DOWN, 2), DirKeyPress(LEFT, 2))
    numPaths[9][2] = listOf(DirKeyPress(DOWN, 2), DirKeyPress(LEFT))
    numPaths[9][3] = listOf(DirKeyPress(DOWN, 2))
    numPaths[9][4] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT, 2))
    numPaths[9][5] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT))
    numPaths[9][6] = listOf(DirKeyPress(DOWN))
    numPaths[9][7] = listOf(DirKeyPress(LEFT, 2))
    numPaths[9][8] = listOf(DirKeyPress(LEFT))
    numPaths[9][9] = emptyList()
    numPaths[9][A] = listOf(DirKeyPress(DOWN, 3))

    numPaths[A][0] = listOf(DirKeyPress(LEFT))
    numPaths[A][1] = listOf(DirKeyPress(UP), DirKeyPress(LEFT, 2))
    numPaths[A][2] = listOf(DirKeyPress(UP), DirKeyPress(LEFT))
    numPaths[A][3] = listOf(DirKeyPress(UP))
    numPaths[A][4] = listOf(DirKeyPress(UP, 2), DirKeyPress(LEFT, 2))
    numPaths[A][5] = listOf(DirKeyPress(UP, 2), DirKeyPress(LEFT))
    numPaths[A][6] = listOf(DirKeyPress(UP, 2))
    numPaths[A][7] = listOf(DirKeyPress(UP, 3), DirKeyPress(LEFT, 2))
    numPaths[A][8] = listOf(DirKeyPress(UP, 3), DirKeyPress(LEFT))
    numPaths[A][9] = listOf(DirKeyPress(UP, 3))
    numPaths[A][A] = emptyList()

    dirPaths[UP.ordinal][UP.ordinal] = emptyList()
    dirPaths[UP.ordinal][DOWN.ordinal] = listOf(DirKeyPress(DOWN))
    dirPaths[UP.ordinal][LEFT.ordinal] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT))
    dirPaths[UP.ordinal][RIGHT.ordinal] = listOf(DirKeyPress(DOWN), DirKeyPress(RIGHT))
    dirPaths[UP.ordinal][PRESS.ordinal] = listOf(DirKeyPress(RIGHT))

    dirPaths[DOWN.ordinal][UP.ordinal] = listOf(DirKeyPress(UP))
    dirPaths[DOWN.ordinal][DOWN.ordinal] = emptyList()
    dirPaths[DOWN.ordinal][LEFT.ordinal] = listOf(DirKeyPress(LEFT))
    dirPaths[DOWN.ordinal][RIGHT.ordinal] = listOf(DirKeyPress(RIGHT))
    dirPaths[DOWN.ordinal][PRESS.ordinal] = listOf(DirKeyPress(UP), DirKeyPress(RIGHT))

    dirPaths[LEFT.ordinal][UP.ordinal] = listOf(DirKeyPress(RIGHT), DirKeyPress(UP))
    dirPaths[LEFT.ordinal][DOWN.ordinal] = listOf(DirKeyPress(RIGHT))
    dirPaths[LEFT.ordinal][LEFT.ordinal] = emptyList()
    dirPaths[LEFT.ordinal][RIGHT.ordinal] = listOf(DirKeyPress(RIGHT, 2))
    dirPaths[LEFT.ordinal][PRESS.ordinal] = listOf(DirKeyPress(RIGHT, 2), DirKeyPress(UP))

    dirPaths[RIGHT.ordinal][UP.ordinal] = listOf(DirKeyPress(UP), DirKeyPress(LEFT))
    dirPaths[RIGHT.ordinal][DOWN.ordinal] = listOf(DirKeyPress(LEFT))
    dirPaths[RIGHT.ordinal][LEFT.ordinal] = listOf(DirKeyPress(LEFT, 2))
    dirPaths[RIGHT.ordinal][RIGHT.ordinal] = emptyList()
    dirPaths[RIGHT.ordinal][PRESS.ordinal] = listOf(DirKeyPress(UP))

    dirPaths[PRESS.ordinal][UP.ordinal] = listOf(DirKeyPress(LEFT))
    dirPaths[PRESS.ordinal][DOWN.ordinal] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT))
    dirPaths[PRESS.ordinal][LEFT.ordinal] = listOf(DirKeyPress(DOWN), DirKeyPress(LEFT, 2))
    dirPaths[PRESS.ordinal][RIGHT.ordinal] = listOf(DirKeyPress(DOWN))
    dirPaths[PRESS.ordinal][PRESS.ordinal] = emptyList()

    for (i in numPaths.indices) {
        for (j in numPaths[i].indices) {
            val list = numPaths[i][j]
            if (list.size == 1) {
                numPaths2[i][j] = listOf(list + DirKeyPress(PRESS))
            } else if (list.size == 2) {
                numPaths2[i][j] =
                    listOf(list + DirKeyPress(PRESS), listOf(list[1], list[0]) + DirKeyPress(PRESS))
            } else if (list.isNotEmpty()) {
                throw IllegalStateException()
            }
        }
    }

    for (i in dirPaths.indices) {
        for (j in dirPaths[i].indices) {
            val list = dirPaths[i][j]
            if (list.size == 1) {
                dirPaths2[i][j] = listOf(list + DirKeyPress(PRESS))
            } else if (list.size == 2) {
                dirPaths2[i][j] =
                    listOf(list + DirKeyPress(PRESS), listOf(list[1], list[0]) + DirKeyPress(PRESS))
            } else if (list.isNotEmpty()) {
                throw IllegalStateException()
            }
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