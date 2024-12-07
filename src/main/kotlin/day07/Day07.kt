package day07

import common.resourceFile
import day07.Operator.*

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
    input.sumOf { (expectedResult, numbers) -> calc(expectedResult, numbers, PLUS, MULTIPLY) }

fun part2(input: List<Pair<Long, List<Long>>>) =
    input.sumOf { (expectedResult, numbers) -> calc(expectedResult, numbers, PLUS, MULTIPLY, CONCAT) }

fun calc(expectedResult: Long, numbers: List<Long>, vararg operators: Operator): Long =
    calcR(expectedResult, numbers[0], 1, numbers, *operators)

fun calcR(
    expectedResult: Long,
    intermediateResult: Long,
    index: Int,
    numbers: List<Long>,
    vararg operators: Operator
): Long {
    if (intermediateResult > expectedResult) return 0
    return if (index < numbers.size) {
        for (operator in operators) {
            val inc = operator.calc(intermediateResult, numbers[index])
            val possiblyResult = calcR(expectedResult, inc, index + 1, numbers, *operators)
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