package day24

import common.resourceFile

fun main() {
    part1("/day24.txt")
}

fun part1(name: String) {
    val (valueAssignments, gateAssignments) = readInput(name)

    val valueSuppliers = mutableMapOf<Wire, ValueSupplier>()
    valueAssignments.forEach { valueSuppliers[it.wire] = FromValueAssignment(it, valueSuppliers) }
    gateAssignments.forEach { valueSuppliers[it.wire] = FromGateAssignment(it, valueSuppliers) }
    val zValueSuppliers = valueSuppliers.filterKeys { it.name.startsWith("z") }

    val values = mutableMapOf<Wire, Int>()

    zValueSuppliers.forEach { (wire, supplier) -> values[wire] = supplier.getValue() }

    val binaryString = values.keys.sortedDescending().map { values[it]!! }.joinToString("")

    println(binaryString)
    println(binaryString.toLong(2))

    part2(valueAssignments, gateAssignments, valueSuppliers, zValueSuppliers)
}

fun part2(
    valueAssignments: List<ValueAssignment>,
    gateAssignments: List<GateAssignment>,
    valueSuppliers: Map<Wire, ValueSupplier>,
    zValueSuppliers: Map<Wire, ValueSupplier>
) {
    gateAssignments.forEach {
        if (it.left.name.startsWith("y") && it.right.name.startsWith("x")) {
            val left = it.left
            val right = it.right
            it.left = right
            it.right = left
        }
    }

    val expSubst = mutableMapOf<Triple<String, String, LogicalOp>, String>()

    zValueSuppliers.keys.sorted()
        .forEach { println("${it} = ${zValueSuppliers[it]!!.expression(expSubst)}") }
}

fun readInput(name: String): Pair<List<ValueAssignment>, List<GateAssignment>> {
    val valueAssignments = mutableListOf<ValueAssignment>()
    val gateAssignments = mutableListOf<GateAssignment>()

    var literalValues = true
    resourceFile(name).forEachLine { line ->
        if (line.isNotBlank()) {
            if (literalValues) {
                val (wire, value) = line.split(": ")
                valueAssignments += ValueAssignment(Wire(wire), value.toInt())
            } else {
                val (operation, wire) = line.split(" -> ")
                val (leftWire, op, rightWire) = operation.split(" ")
                gateAssignments += GateAssignment(
                    Wire(wire),
                    Wire(leftWire),
                    Wire(rightWire),
                    LogicalOp.valueOf(op)
                )
            }
        } else {
            literalValues = false
        }
    }

    return Pair(valueAssignments, gateAssignments)
}

enum class LogicalOp(val apply: (Int, Int) -> Int) {
    AND({ a, b -> a and b }),
    OR({ a, b -> a or b }),
    XOR({ a, b -> a xor b })
}

data class Wire(val name: String) : Comparable<Wire> {
    override fun compareTo(other: Wire): Int = name.compareTo(other.name)

    override fun toString(): String = name
}

data class ValueAssignment(val wire: Wire, val value: Int)

data class GateAssignment(val wire: Wire, var left: Wire, var right: Wire, val op: LogicalOp) {
    override fun toString(): String = "$wire = ${left} ${op} ${right}"
}

abstract class ValueSupplier(val suppliers: Map<Wire, ValueSupplier>) {
    abstract fun getValue(): Int
    abstract fun expression(subst: MutableMap<Triple<String, String, LogicalOp>, String>): String
}

class FromValueAssignment(val assignment: ValueAssignment, suppliers: Map<Wire, ValueSupplier>) :
    ValueSupplier(suppliers) {
    override fun getValue(): Int = assignment.value
    override fun expression(subst: MutableMap<Triple<String, String, LogicalOp>, String>): String = assignment.wire.name
}

class FromGateAssignment(val assignment: GateAssignment, suppliers: Map<Wire, ValueSupplier>) :
    ValueSupplier(suppliers) {
    override fun getValue(): Int {
        val left = suppliers[assignment.left]!!.getValue()
        val right = suppliers[assignment.right]!!.getValue()
        return assignment.op.apply(left, right)
    }

    override fun expression(subst: MutableMap<Triple<String, String, LogicalOp>, String>): String {
        val left = suppliers[assignment.left]!!.expression(subst)
        val right = suppliers[assignment.right]!!.expression(subst)
//        if (left.startsWith("x") && right.startsWith("y")) {
//            val xyIndex = left.substring(1)
//            if (right.endsWith(xyIndex)) {
//                return subst.getOrPut(Triple(left, right, assignment.op)) { "${assignment.op.name.lowercase()}${xyIndex}" }
//            }
//        }
        return "${assignment.op.name.lowercase()}($left, $right)"
        //return "($left ${assignment.op.name.lowercase()} $right)"
    }
}
