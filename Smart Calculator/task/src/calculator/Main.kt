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
                    isAssignment(line) -> Calculator.assign(line)
                    isVariable(line) -> Calculator.processVariable(line)
                    isInvalidExpression(line) -> println("Invalid expression")
                    else -> println(Calculator.evaluate(line))
                }
            }
        }
    }
}

private fun isVariable(line: String) =
    line.matches(Regex("\\s*[a-zA-Z]+\\s*(=\\s*[0-9\\w]+\\s*)?"))

private fun isAssignment(line: String) =
    line.contains("=")

private fun isInvalidExpression(line: String) =
    !line.matches(Regex("(([\\+\\-]\\s*)*[0-9\\w]+\\s?)+"))
