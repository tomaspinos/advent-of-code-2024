package day21

fun main() {
    initializeKeyPaths()
    val paths = type1(listOf(0, 2, 9, A), 0, A, listOf())
    paths.forEach { it.print(listOf(0, 2, 9, A)) }
//    part1(
//        listOf(
//            listOf(0, 2, 9, A),
//            listOf(9, 8, 0, A),
//            listOf(1, 7, 9, A),
//            listOf(4, 5, 6, A),
//            listOf(3, 7, 9, A)
//        )
//    )
}
