package day07

import common.resourceFile

fun main() {
    val input = readInput("/day07.txt")
    println(part1(input))
    println(part2(input))
}

fun readInput(name: String): List<Pair<Long, List<Long>>> =
    resourceFile(name).readLines()
        .map {
            val (expectedResultStr, numbersStr) = it.split(": ")
            Pair(expectedResultStr.toLong(), numbersStr.split(" ").map(String::toLong))
        }

fun part1(input: List<Pair<Long, List<Long>>>): Long =
    input.sumOf { (expectedResult, numbers) ->
        calc(expectedResult, numbers, listOf(Operator.PLUS, Operator.MULTIPLY))
    }

fun part2(input: List<Pair<Long, List<Long>>>) =
    input.sumOf { (expectedResult, numbers) ->
        calc(
            expectedResult,
            numbers,
            listOf(Operator.PLUS, Operator.MULTIPLY, Operator.CONCAT)
        )
    }

fun calc(expectedResult: Long, numbers: List<Long>, operators: List<Operator>): Long =
    calc(expectedResult, numbers[0], 1, operators, numbers)

fun calc(
    expectedResult: Long,
    intermediateResult: Long,
    index: Int,
    operators: List<Operator>,
    numbers: List<Long>
): Long {
    if (intermediateResult > expectedResult) return 0
    return if (index < numbers.size) {
        for (operator in operators) {
            val inc = operator.calc(intermediateResult, numbers[index])
            val possiblyResult = calc(expectedResult, inc, index + 1, operators, numbers)
            if (possiblyResult > 0) return possiblyResult
        }
        return 0
    } else {
        if (expectedResult == intermediateResult) expectedResult else 0
    }
}

enum class Operator(val operator: (Long, Long) -> Long) {
    PLUS({ a, b -> a + b }),
    MULTIPLY({ a, b -> a * b }),
    CONCAT({ a, b -> (a.toString() + b.toString()).toLong() });

    fun calc(a: Long, b: Long): Long = operator(a, b)
}