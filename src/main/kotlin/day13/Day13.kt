package day13

import common.resourceFile

fun main() {
    process("/day13.txt")
}

fun process(name: String) {
    val tasks = readInput(name)
    println(tasks.sumOf { solve(it) })
    println(tasks.sumOf { solve(Task(it.a, it.b, it.prize + XY(10000000000000, 10000000000000))) })
}

fun solve(task: Task): Long {
    val (ax, ay) = task.a
    val (bx, by) = task.b
    val (px, py) = task.prize

    val aNumerator = (bx*py) - (by*px)
    val aDenominator = (ay*bx) - (ax*by)

    if (aNumerator % aDenominator == 0L) {
        val a = aNumerator / aDenominator

        val bNumerator = px - (ax*a)
        val bDenominator = bx

        if (bNumerator % bDenominator == 0L) {
            val b = bNumerator / bDenominator
            return a * 3 + b
        }
    }

    return 0
}

val BUTTON_A_REGEX = "Button A: X\\+(\\d+), Y\\+(\\d+)".toRegex()
val BUTTON_B_REGEX = "Button B: X\\+(\\d+), Y\\+(\\d+)".toRegex()
val PRIZE_REGEX = "Prize: X=(\\d+), Y=(\\d+)".toRegex()

fun readInput(name: String): List<Task> {
    val lines = resourceFile(name).readLines().iterator()
    val tasks = mutableListOf<Task>()
    while (lines.hasNext()) {
        var a = lines.next()
        while (a.isBlank()) a = lines.next()
        tasks.add(
            Task(
                readXY(a, BUTTON_A_REGEX),
                readXY(lines.next(), BUTTON_B_REGEX),
                readXY(lines.next(), PRIZE_REGEX)
            )
        )
    }
    return tasks
}

fun readXY(s: String, regex: Regex): XY {
    val groups = regex.find(s)!!.groups
    return XY(groups[1]!!.value.toLong(), groups[2]!!.value.toLong())
}

data class XY(val x: Long, val y: Long) {
    operator fun plus(other: XY): XY = XY(x + other.x, y + other.y)
}

data class Task(val a: XY, val b: XY, val prize: XY)