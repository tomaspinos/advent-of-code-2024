package day05p2

import com.google.common.collect.HashMultimap
import java.io.File

fun main() {
    process("/day05.txt")
}

fun process(name: String) {
    readInput(name)

    var result = 0

    for (sequence in sequences) {
        if (!check(sequence)) {
            val ordered = sequence.sortedWith(comparator)
            result += ordered[ordered.size / 2]
        }
    }

    println(result)
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
    val lessThanA = lessThanRules[a] ?: emptySet()
    if (lessThanA.contains(b)) {
        -1
    } else {
        val lessThanB = lessThanRules[b] ?: emptySet()
        if (lessThanB.contains(a)) {
            1
        } else {
            0
        }
    }
}
