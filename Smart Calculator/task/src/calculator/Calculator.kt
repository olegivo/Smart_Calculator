package calculator

object Calculator {

    val variables = mutableMapOf<String, Int>()

    sealed class Token {
        abstract class Operation : Token()
        object Plus : Operation()
        object Minus : Operation()
        data class Variable(val name: String) : Token()
        data class Value(val value: Int) : Token()
    }

    fun evaluate(line: String): Int {
        val items = line.split(' ')
            .filter { it.isNotBlank() }
            .flatMap { s ->
                when {
                    s.matches(Regex("^\\d+$")) -> {
                        listOf(Token.Value(s.toInt()))
                    }
                    s.matches(Regex("^\\w$")) -> {
                        listOf(Token.Variable(s))
                    }
                    else -> {
                        s.toCharArray().map { c ->
                            when (c) {
                                '+' -> Token.Plus
                                '-' -> Token.Minus
                                else -> TODO()
                            }
                        }
                    }
                }
            }

        var sum = 0
        var position = 0
        var currentOperation: Token.Operation? = null
        while (position < items.size) {
            val item = items[position]

            if (item is Token.Operation) {
                currentOperation =
                    if (currentOperation is Token.Minus && item is Token.Minus) {
                        Token.Plus
                    } else {
                        item
                    }
            } else {
                val value = (item as? Token.Value)?.value
                    ?: (item as? Token.Variable)?.let { variables.getValue(it.name) }!!

                if (currentOperation is Token.Minus) {
                    sum -= value
                } else {
                    sum += value
                }
                currentOperation = null
            }

            position++
        }

        return sum
    }

    fun processVariable(line: String) {
        line.split('=')
            .let {
                when (it.size) {
                    1 -> {
                        println(variables[it.single().trim()] ?: "Unknown variable")
                    }
                    2 -> {
                        val valueToAssign = it.last().trim()
                        variables[it.first().trim()] =
                            if (valueToAssign.matches(Regex("\\d+"))) {
                                valueToAssign.toInt()
                            } else {
                                variables[valueToAssign]!!
                            }
                    }
                }
            }
    }

    fun assign(line: String) {
        val parts = line.split('=').map { it.trim() }
        try {
            if (parts.size != 2)
                throw CalculatorException("Invalid assignment")

            val assignee = parts.first()
            if (!isCorrectVariableName(assignee))
                throw CalculatorException("Invalid identifier")

            val valueToAssign = parts.last()
            variables[assignee] =
                if (valueToAssign.matches(Regex("\\d+"))) {
                    valueToAssign.toInt()
                } else {
                    if (!isCorrectVariableName(valueToAssign))
                        throw CalculatorException("Invalid identifier")

                    if (!variables.containsKey(valueToAssign))
                        throw CalculatorException("Unknown variable")

                    variables[valueToAssign]!!
                }

        } catch (ex: CalculatorException) {
            println(ex.message)
        }
    }

    private fun isCorrectVariableName(name: String): Boolean =
        name.matches(Regex("^[a-zA-Z]+$", RegexOption.MULTILINE))

    private class CalculatorException(message: String) : RuntimeException(message)
}