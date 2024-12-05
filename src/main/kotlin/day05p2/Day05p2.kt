package day05p2

import com.google.common.collect.HashMultimap
import java.io.File

fun main() {
    process("/day05.txt")
}

fun process(name: String) {
    readInput(name)

    println(sequences
        .filter { !check(it) }
        .map { it.sortedWith(comparator) }
        .sumOf { it[it.size / 2] })
}

fun check(sequence: List<Int>): Boolean {
    return sequence == sequence.sortedWith(comparator)
}

fun readInput(name: String) {
    var readingRules = true
    File(object {}.javaClass.getResource(name).toURI()).forEachLine {
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
            0
        }
    }
}
