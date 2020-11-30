package calculator

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    while (scanner.hasNextLine()) {
        when (val line = scanner.nextLine()) {
            "/exit" -> {
                println("Bye!")
                break
            }
            "/help" -> {
                println("The program calculates the sum of numbers")
            }
            "" -> continue
            else -> {
                val items = line.split(' ').filter { it.isNotBlank() }.map { it.toInt() }
                println(items.sum())
            }
        }
    }
}
