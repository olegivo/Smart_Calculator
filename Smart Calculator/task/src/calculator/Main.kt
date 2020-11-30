package calculator

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    while (scanner.hasNextLine()) {
        val line = scanner.nextLine()
        if (line == "/exit") {
            println("Bye!")
            break
        }
        if (line.isBlank()) continue

        val items = line.split(' ').map { it.toInt() }
        println(items.sum())
    }
}
