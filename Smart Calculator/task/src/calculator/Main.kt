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
                when {
                    line.startsWith("/") -> println("Unknown command")
                    isInvalidExpression(line) -> println("Invalid expression")
                    else -> println(Calculator.evaluate(line))
                }
            }
        }
    }
}

private fun isInvalidExpression(line: String) =
    !line.matches(Regex("(([\\+\\-]\\s*)*[0-9]+\\s?)+"))

object Calculator {

    fun evaluate(line: String): Int {
        val items = line.split(' ')
            .filter { it.isNotBlank() }
            .flatMap { s ->
                if (s.toIntOrNull() != null) listOf(s) else {
                    s.toCharArray().map { c -> c.toString() }
                }
            }

        var sum = 0
        var position = 0
        var currentOperation: String? = null
        while (position < items.size) {
            val item = items[position]
            item.toIntOrNull()
                ?.let {
                    if (currentOperation == "-") {
                        sum -= it
                    } else {
                        sum += it
                    }
                    currentOperation = null
                }
                ?: run {
                    currentOperation =
                        if (currentOperation == "-" && item == "-") {
                            "+"
                        } else {
                            item
                        }
                }
            position++
        }

        return sum
    }
}