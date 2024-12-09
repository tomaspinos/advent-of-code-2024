package day09

import common.resourceFile

const val FREE_SPACE = -1

fun main() {
    process("/day09.txt")
}

fun process(name: String) {
    val exploded = explode(readInput(name))
    shuffle(exploded)
    println(checksum(exploded))
}

fun shuffle(exploded: MutableList<Int>) {
    var freeSpaceIndex = -1
    var fileIndex = exploded.size

    while (freeSpaceIndex < fileIndex) {
        freeSpaceIndex++
        while (exploded[freeSpaceIndex] != FREE_SPACE) freeSpaceIndex++

        fileIndex--
        while (exploded[fileIndex] == FREE_SPACE) fileIndex--

        if (freeSpaceIndex < fileIndex) {
            exploded[freeSpaceIndex] = exploded[fileIndex]
            exploded[fileIndex] = FREE_SPACE
        }
    }
}

fun checksum(exploded: List<Int>): Long {
    var result = 0L
    var i = 0
    while (exploded[i] != FREE_SPACE) {
        result += (i * exploded[i])
        i++
    }
    return result
}

fun explode(input: String): MutableList<Int> {
    val exploded = mutableListOf<Int>()
    var fileId = 0
    var fileOverFreeSpace = true
    for (i in input.indices) {
        val ch = input[i]
        if (fileOverFreeSpace) {
            val fileLength = ch.digitToInt()
            for (j in 0..<fileLength) {
                exploded.add(fileId)
            }
            fileId++
        } else {
            val freeSpaceLength = ch.digitToInt()
            for (j in 0..<freeSpaceLength) {
                exploded.add(FREE_SPACE)
            }
        }
        fileOverFreeSpace = !fileOverFreeSpace
    }
    return exploded
}

fun readInput(name: String): String {
    return resourceFile(name).readLines()[0]
}

fun print(exploded: List<Int>) {
    for (d in exploded) {
        if (d != FREE_SPACE) print(d) else print('.')
    }
    println()
}