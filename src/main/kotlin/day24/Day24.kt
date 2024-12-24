package day24

import common.resourceFile

fun main() {
    part1("/day24.txt")
}

fun part1(name: String) {
    val (valueAssignments, gateAssignments) = readInput(name)

    val valueSuppliers = mutableMapOf<Wire, ValueSupplier>()

    for (assignment in valueAssignments) {
        valueSuppliers[assignment.wire] = FromValueAssignment(assignment, valueSuppliers)
    }
    for (assignment in gateAssignments) {
        valueSuppliers[assignment.wire] = FromGateAssignment(assignment, valueSuppliers)
    }

    val values = mutableMapOf<Wire, Int>()

    valueSuppliers.filterKeys { it.name.startsWith("z") }
        .forEach { (wire, supplier) -> values[wire] = supplier.getValue() }

    val binaryString = values.keys.sortedDescending().map { values[it]!! }.joinToString("")

    println(binaryString)
    println(binaryString.toLong(2))
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
                    Gate(Wire(leftWire), Wire(rightWire), LogicalOp.valueOf(op))
                )
            }
        } else {
            literalValues = false
        }
    }

    return Pair(valueAssignments, gateAssignments)
}

enum class LogicalOp { AND, OR, XOR }

data class Wire(val name: String) : Comparable<Wire> {
    override fun compareTo(other: Wire): Int {
        return name.compareTo(other.name)
    }

    override fun toString(): String {
        return name
    }
}

data class Gate(val left: Wire, val right: Wire, val op: LogicalOp)

data class ValueAssignment(val wire: Wire, val value: Int)

data class GateAssignment(val wire: Wire, val gate: Gate) {
    override fun toString(): String = "$wire = ${gate.left} ${gate.op} ${gate.right}"
}

abstract class ValueSupplier(val suppliers: Map<Wire, ValueSupplier>) {
    abstract fun getValue(): Int
}

class FromValueAssignment(val assignment: ValueAssignment, suppliers: Map<Wire, ValueSupplier>) :
    ValueSupplier(suppliers) {
    override fun getValue(): Int {
        return assignment.value
    }
}

class FromGateAssignment(val assignment: GateAssignment, suppliers: Map<Wire, ValueSupplier>) :
    ValueSupplier(suppliers) {
    override fun getValue(): Int {
        val left = suppliers[assignment.gate.left]!!.getValue()
        val right = suppliers[assignment.gate.right]!!.getValue()
        return when (assignment.gate.op) {
            LogicalOp.AND -> left and right
            LogicalOp.OR -> left or right
            LogicalOp.XOR -> left xor right
        }
    }
}
