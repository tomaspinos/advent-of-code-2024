package day19

import common.resourceFile

fun main() {
    process("/day19.txt")
}

fun process(name: String) {
    process(readInput(name))
}

fun process(input: Input) {
    val (patterns, words) = input

    val engine = Engine()

    for (pattern in patterns) {
        engine.addPattern(pattern)
    }

    var matchCount = 0
    var allOptions = 0L

    for (word in words) {
        println(word)
        val match = engine.matchWord(word)
        println("  $match")
        if (match) {
            matchCount++
            val options = engine.countOptions(word)
            println("  options: $options")
            allOptions += options
        }
    }

    println(matchCount)
    println(allOptions)
}

fun readInput(name: String): Input {
    val lines = resourceFile(name).readLines()
    val patterns = lines.first().split(", ")
    val words = lines.subList(2, lines.size)
    return Input(patterns, words)
}

class Engine {
    val nodes: MutableMap<Char, Node> = mutableMapOf()

    fun addPattern(pattern: String) {
        val ch = pattern[0]
        val matchingNode = nodes[ch]
        if (matchingNode != null) {
            matchingNode.addPattern(pattern, 1)
        } else {
            val childNode = Node(ch, 1, false, mutableMapOf())
            nodes[ch] = childNode
            childNode.addPattern(pattern, 1)
        }
    }

    fun findMatches(word: String): List<Node> {
        val matchingNode = nodes[word[0]]
        return if (matchingNode != null) {
            matchingNode.findMatches(word, 1)
        } else {
            listOf()
        }
    }

    fun matchWord(word: String): Boolean {
        val matches = findMatches(word)
        for (match in matches) {
            if (match.length == word.length) {
                return true
            } else if (matchWord(word.substring(match.length))) {
                return true
            }
        }
        return false
    }

    /**
     * a,aa,b
     * aabaa
     *   a|a|b|a|a
     *   a|a|b|aa
     *   aa|b|a|a
     *   aa|b|aa
     */
    fun countOptions(word: String): Long {
        val tailCounts = Array(word.length) { 0L }
        countOptions(word, tailCounts)
        return tailCounts[0]
    }

    fun countOptions(word: String, tailCounts: Array<Long>) {
        val count = tailCounts[tailCounts.size - word.length]
        if (count > 0) {
            return
        }
        val matches = findMatches(word)
        for (match in matches) {
            if (match.length == word.length) {
                tailCounts[tailCounts.size - word.length]++
            } else {
                val tail = word.substring(match.length)
                countOptions(tail, tailCounts)
                val tailCount = tailCounts[tailCounts.size - tail.length]
                tailCounts[tailCounts.size - word.length] += tailCount
            }
        }
    }
}

data class Node(
    val ch: Char,
    val length: Int,
    var patternEnd: Boolean,
    val childNodes: MutableMap<Char, Node>
) {

    fun addPattern(pattern: String, index: Int) {
        if (index == pattern.length) {
            patternEnd = true
        } else {
            val ch = pattern[index]
            val matchingNode = childNodes[ch]
            if (matchingNode != null) {
                matchingNode.addPattern(pattern, index + 1)
            } else {
                val childNode = Node(ch, index + 1, false, mutableMapOf())
                childNodes[ch] = childNode
                childNode.addPattern(pattern, index + 1)
            }
        }
    }

    fun findMatches(word: String, index: Int): List<Node> {
        if (index == word.length) {
            if (patternEnd) {
                return listOf(this)
            }
        } else {
            val resultingNodes = mutableListOf<Node>()
            if (patternEnd) {
                resultingNodes += this
            }
            val matchingNode = childNodes[word[index]]
            if (matchingNode != null) {
                resultingNodes += matchingNode.findMatches(word, index + 1)
            }
            return resultingNodes
        }
        return emptyList()
    }
}

data class Input(val patterns: List<String>, val words: List<String>)
