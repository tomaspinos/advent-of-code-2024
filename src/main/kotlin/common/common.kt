package common

import java.io.File

fun resourceFile(name: String): File {
    return File(object {}.javaClass.getResource(name).toURI())
}

data class XY(val x: Int, val y: Int) {
    operator fun plus(other: XY) = XY(x + other.x, y + other.y)
    operator fun minus(other: XY) = XY(x - other.x, y - other.y)
    fun up(): XY = XY(x, y - 1)
    fun down(): XY = XY(x, y + 1)
    fun left(): XY = XY(x - 1, y)
    fun right(): XY = XY(x + 1, y)
    fun <T> isValid(array: Array<Array<T>>): Boolean = y in array.indices && x in array[0].indices
    fun isValid(width: Int, height: Int): Boolean = y in 0..<height && x in 0..<width
}

data class XLY(val x: Long, val y: Long) {
    operator fun plus(other: XLY): XLY = XLY(x + other.x, y + other.y)
}

val directions: Array<XY> = arrayOf(
    /* left  */ XY(-1, 0),
    /* up    */ XY(0, -1),
    /* right */ XY(1, 0),
    /* down  */ XY(0, 1)
)
