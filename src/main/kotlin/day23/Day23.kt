package day23

import common.resourceFile

fun main() {
    part1("/day23.txt")
    part2("/day23.txt")
}

fun part1(name: String) {
    val (comps, conMap) = getComputersAndConnectionMap(readInput(name))

    val triples = mutableSetOf<Triple<Computer, Computer, Computer>>()

    for (a in comps) {
        for (b in comps) {
            if (a == b) continue
            for (c in comps) {
                if (a == c || b == c) continue
                if (!(a.name.startsWith("t") || b.name.startsWith("t") || c.name.startsWith("t"))) continue
                if (conMap[a]!!.contains(b) && conMap[a]!!.contains(c)
                    && conMap[b]!!.contains(a) && conMap[b]!!.contains(c)
                    && conMap[c]!!.contains(a) && conMap[c]!!.contains(b)
                ) {
                    val (aa, bb, cc) = listOf(a, b, c).sortedBy { it.name }
                    triples.add(Triple(aa, bb, cc))
                }
            }
        }
    }

    for (triple in triples) {
        println("${triple.first.name},${triple.second.name},${triple.third.name}")
    }

    println(triples.size)
}

fun part2(name: String) {
    val (comps, conMap) = getComputersAndConnectionMap(readInput(name))

    val cliques = mutableSetOf<MutableSet<Computer>>()
    for (comp in comps) {
        cliques.add(mutableSetOf(comp))
    }

    for (i in 0..<comps.size) {
        val comp = comps[i]
        println("${i + 1} / ${comps.size}")
        for (clique in cliques) {
            if (clique.all { compInClique -> conMap[compInClique]!!.contains(comp) }) {
                clique.add(comp)
            }
        }
    }

    val maxClique = cliques.sortedByDescending { it.size }[0]
    println(maxClique.map { it.name }.sorted().joinToString(","))
}

fun getComputersAndConnectionMap(cons: List<Connection>): Pair<List<Computer>, Map<Computer, Set<Computer>>> {
    val comps = cons.flatMap { con -> listOf(con.a, con.b) }.distinct()

    val conMap = mutableMapOf<Computer, Set<Computer>>()
    for (con in cons) {
        conMap.merge(con.a, setOf(con.b)) { s1, s2 -> s1 + s2 }
        conMap.merge(con.b, setOf(con.a)) { s1, s2 -> s1 + s2 }
    }

    return Pair(comps, conMap)
}

fun readInput(name: String): List<Connection> {
    return resourceFile(name).readLines()
        .map { line ->
            val (a, b) = line.split("-")
            Connection(Computer(a), Computer(b))
        }
}

data class Computer(val name: String)

data class Connection(val a: Computer, val b: Computer)