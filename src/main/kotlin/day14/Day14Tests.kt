package day14

import common.XY

fun main() {
    var robot = Robot(XY(2, 4), XY(2, -3))
    for (i in 0..5) {
        robot = robot.move(11, 7)
        println(robot)
    }

    process("/day14sample1.txt", 11, 7, 100)
}