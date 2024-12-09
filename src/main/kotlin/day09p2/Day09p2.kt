package day09p2

import common.resourceFile

fun main() {
    process("/day09.txt")
}

fun process(name: String) {
    val blocks = readInput(name)
    shuffle(blocks)
    println(checksum(blocks))
}

fun shuffle(blocks: ArrayList<Block>) {
    for (file in blocks.filterIsInstance<File>().reversed()) {
        val fileIndex = blocks.indexOf(file)
        val freeSpaceIndex = blocks.indexOfFirst { it is FreeSpace && it.length >= file.length }
        if (freeSpaceIndex in 0..<fileIndex) {
            val freeSpace = blocks[freeSpaceIndex] as FreeSpace
            if (freeSpace.length == file.length) {
                blocks[freeSpaceIndex] = file
                blocks[fileIndex] = freeSpace
            } else {
                blocks[fileIndex] = FreeSpace(file.length)
                blocks.add(freeSpaceIndex, file)
                freeSpace.length -= file.length
            }
        }
    }
}

fun checksum(blocks: List<Block>): Long {
    var result = 0L
    var i = 0
    for (block in blocks) {
        if (block is File) {
            for (j in i..<i + block.length) result += (j * block.id)
        }
        i += block.length
    }
    return result
}

fun readInput(name: String): ArrayList<Block> {
    val input = resourceFile(name).readLines()[0]
    val blocks = ArrayList<Block>()
    var fileId = 0
    var fileOverFreeSpace = true
    for (i in input.indices) {
        val ch = input[i]
        if (fileOverFreeSpace) {
            val fileLength = ch.digitToInt()
            blocks.add(File(fileLength, fileId))
            fileId++
        } else {
            val freeSpaceLength = ch.digitToInt()
            blocks.add(FreeSpace(freeSpaceLength))
        }
        fileOverFreeSpace = !fileOverFreeSpace
    }
    return blocks
}

fun print(blocks: List<Block>) = println(blocks.joinToString("") { it.toString() })

open class Block(var length: Int)

class File(length: Int, val id: Int) : Block(length) {
    override fun toString(): String = (0..<length).map { id }.joinToString("")
}

class FreeSpace(length: Int) : Block(length) {
    override fun toString(): String = (0..<length).map { '.' }.joinToString("")
}