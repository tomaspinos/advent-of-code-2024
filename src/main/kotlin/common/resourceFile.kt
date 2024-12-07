package common

import java.io.File

fun resourceFile(name: String): File {
    return File(object {}.javaClass.getResource(name).toURI())
}