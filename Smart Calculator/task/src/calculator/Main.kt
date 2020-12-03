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
                if (line.startsWith("/")) {
                    println("Unknown command")
                } else {
                    Calculator.process(line)
                }
            }
        }
    }
}

