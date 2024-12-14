package day15

import common.resourceFile

fun main() {
    part1()
}

fun part1() = process("/day15.txt")

fun process(name: String) {
    readInput(name)
}

fun readInput(name: String) {
    resourceFile(name)
}
