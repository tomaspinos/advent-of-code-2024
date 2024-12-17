package day17

import common.resourceFile
import kotlin.math.pow

fun main() {
    process("/day17.txt")
}

fun process(name: String) {
    val (a, b, c, program) = readInput(name)
    val computer = Computer(a, b, c, program, 0)
    while (!computer.isFinished()) {
        computer.executeInstruction()
    }
    println(computer.output.joinToString(","))
}

fun readInput(name: String): Input {
    val lines = resourceFile(name).readLines()
    val a = lines[0].split(": ")[1].toInt()
    val b = lines[1].split(": ")[1].toInt()
    val c = lines[2].split(": ")[1].toInt()
    val program = lines[4].split(": ")[1].split(",").map { it.toInt() }
    return Input(a, b, c, program)
}

data class Input(val a: Int, val b: Int, val c: Int, val program: List<Int>)

class Computer(var a: Int, var b: Int, var c: Int, val program: List<Int>, var pointer: Int) {
    val output = mutableListOf<Int>()

    fun isFinished(): Boolean = pointer >= program.size

    fun executeInstruction() {
        val instruction = program[pointer]
        val operand = program[pointer + 1]
        when (instruction) {
            0 -> {
                // adv
                a  = (a.toDouble() / ((2.0).pow(comboOperand(operand)))).toInt()
                pointer += 2
            }
            1 -> {
                // bxl
                b = (b xor operand)
                pointer += 2
            }
            2 -> {
                // bst
                b = comboOperand(operand) % 8
                pointer += 2
            }
            3 -> {
                // jnz
                if (a == 0) {
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
                output.add(comboOperand(operand) % 8)
                pointer += 2
            }
            6 -> {
                // bdv
                b  = (a.toDouble() / ((2.0).pow(comboOperand(operand)))).toInt()
                pointer += 2
            }
            7 -> {
                // cdv
                c  = (a.toDouble() / ((2.0).pow(comboOperand(operand)))).toInt()
                pointer += 2
            }
        }
    }

    fun comboOperand(operand: Int): Int {
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