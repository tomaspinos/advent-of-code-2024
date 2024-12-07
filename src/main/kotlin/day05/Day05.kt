package day05

import com.google.common.collect.HashMultimap
import common.resourceFile

fun main() {
    process("/day05.txt")
}

fun process(name: String) {
    readInput(name)

    println(sequences
        .filter { isSorted(it) }
        .sumOf { it[it.size / 2] })
}

fun isSorted(sequence: List<Int>): Boolean {
    return sequence == sequence.sortedWith(comparator)
}

fun readInput(name: String) {
    var readingRules = true
    resourceFile(name).forEachLine {
        if (it.isBlank()) {
            readingRules = false
        } else {
            if (readingRules) {
                val split = it.split("|")
                lessThanRules.put(split[0].toInt(), split[1].toInt())
            } else {
                val sequence = it.split(",").map(String::toInt).toList()
                sequences.add(sequence)
            }
        }
    }
}

val lessThanRules = HashMultimap.create<Int, Int>()
val sequences = ArrayList<List<Int>>()

val comparator = Comparator<Int> { a, b ->
    if ((lessThanRules[a] ?: emptySet()).contains(b)) {
        -1
    } else {
        if ((lessThanRules[b] ?: emptySet()).contains(a)) {
            1
        } else {
            println("${a} == ${b}")
            0
        }
    }
}
