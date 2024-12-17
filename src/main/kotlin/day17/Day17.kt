package day17

import common.resourceFile

val pow2 = Array<Long>(63) { 0 }

fun main() {
    initPow2()
    process("/day17.txt")
}

fun initPow2() {
    pow2[0] = 1
    for (i in 0..<62) pow2[i + 1] = pow2[i] * 2
}

fun process(name: String) {
    val (a, b, c, program) = readInput(name)
    val computer = Computer(a, b, c, program, 0)
    val output = computer.executeAndPrint()
    println(output)
}

fun readInput(name: String): Input {
    val lines = resourceFile(name).readLines()
    val a = lines[0].split(": ")[1].toLong()
    val b = lines[1].split(": ")[1].toLong()
    val c = lines[2].split(": ")[1].toLong()
    val program = lines[4].split(": ")[1].split(",").map { it.toInt() }
    return Input(a, b, c, program)
}

data class Input(val a: Long, val b: Long, val c: Long, val program: List<Int>)

class Computer(var a: Long, var b: Long, var c: Long, val program: List<Int>, var pointer: Int) {
    val output = mutableListOf<Int>()

    fun executeAndPrint(): String {
        while (!isFinished()) {
            executeInstruction()
        }
        return output.joinToString(",")
    }

    fun isFinished(): Boolean = pointer >= program.size

    fun executeInstruction() {
        val instruction = program[pointer]
        val operand = program[pointer + 1]
        when (instruction) {
            0 -> {
                // adv
                val comboOperand = comboOperand(operand)
                if (comboOperand > 62) throw RuntimeException("Combo operand too big: $comboOperand")
                a  = a / pow2[comboOperand.toInt()]
                pointer += 2
            }
            1 -> {
                // bxl
                b = (b xor operand.toLong())
                pointer += 2
            }
            2 -> {
                // bst
                b = comboOperand(operand) % 8
                pointer += 2
            }
            3 -> {
                // jnz
                if (a == 0L) {
                    pointer += 2
                } else {
                    pointer = operand
                }
            }
            4 -> {
                // bxc
                b = (b xor c)
                pointer += 2
            }
            5 -> {
                // out
                output.add((comboOperand(operand) % 8).toInt())
                pointer += 2
            }
            6 -> {
                // bdv
                val comboOperand = comboOperand(operand)
                if (comboOperand > 62) throw RuntimeException("Combo operand too big: $comboOperand")
                b  = a / pow2[comboOperand.toInt()]
                pointer += 2
            }
            7 -> {
                // cdv
                val comboOperand = comboOperand(operand)
                if (comboOperand > 62) throw RuntimeException("Combo operand too big: $comboOperand")
                c  = a / pow2[comboOperand.toInt()]
                pointer += 2
            }
        }
    }

    fun comboOperand(operand: Int): Long {
        return when (operand) {
            0 -> 0
            1 -> 1
            2 -> 2
            3 -> 3
            4 -> a
            5 -> b
            6 -> c
            else -> throw IllegalArgumentException("Invalid operand $operand")
        }
    }
}