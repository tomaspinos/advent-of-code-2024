package day17

fun main() {
    //Computer(202400, 0, 0, listOf(0, 3, 5, 4, 3, 0), 0).executeAndPrint()

    Computer(
        0,
        0,
        0,
        listOf(2, 4, 1, 3, 7, 5, 1, 5, 0, 3, 4, 3, 5, 5, 3, 0),
        0
    ).executeAndPrint()

    // 202400000000000 too low
    // 2024000000000000 too high

    var i = Int.MAX_VALUE
    while (i > 0) {
        println(i)
        Computer(
            i,
            0,
            0,
            listOf(2, 4, 1, 3, 7, 5, 1, 5, 0, 3, 4, 3, 5, 5, 3, 0),
            0
        ).executeAndPrint()
        i--
    }
}
