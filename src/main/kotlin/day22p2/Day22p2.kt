package day22p2

import common.resourceFile

fun main() {
    part2("/day22.txt")
}

fun part2(name: String) {
    val input = readInput(name)

    val changeSequences = mutableMapOf<Sequence, Int>()

    for (num in input) {
        computeChangeSequences(num)
            .forEach { (sequence, price) ->
                changeSequences.merge(sequence, price.toInt()) { p1, p2 -> p1 + p2 }
            }
    }

    val (sequence, price) = changeSequences.entries.sortedByDescending { it.value }[0]
    println("$sequence, $price")
}

fun computeChangeSequences(initialNum: Long): Map<Sequence, Byte> {
    var num = initialNum
    var price = initialNum % 10
    val prices = Array(2000) { 0.toByte() }
    val difs = Array(2000) { 0.toByte() }
    for (i in 0..<2000) {
        val nextNum = nextSecretNumber(num)
        val nextPrice = nextNum % 10
        val priceDif = nextPrice - price
        prices[i] = nextPrice.toByte()
        difs[i] = priceDif.toByte()
        num = nextNum
        price = nextPrice
    }
    val map = mutableMapOf<Sequence, Byte>()
    for (i in 0..<(prices.size - 4)) {
        val sequence = Sequence(difs[i], difs[i + 1], difs[i + 2], difs[i + 3])
        if (map[sequence] != null) continue
        map[sequence] = prices[i + 3]
    }
    return map
}

fun nextSecretNumber(num: Long): Long {
    var next = ((num * 64) xor num) % 16777216
    next = ((next / 32) xor next) % 16777216
    next = ((next * 2048) xor next) % 16777216
    return next
}

fun readInput(name: String): List<Long> {
    return resourceFile(name).readLines().map(String::toLong)
}

data class Sequence(val a: Byte, val b: Byte, val c: Byte, val d: Byte)