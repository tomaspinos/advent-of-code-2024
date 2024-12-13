package day08

import common.XY

fun main() {
    println(City.computeAntinodesXY(XY(4, 3), XY(5, 5)))
    println(City.computeAntinodesXY(XY(5, 5), XY(4, 3)))
    process("/day08sample1.txt")
}
