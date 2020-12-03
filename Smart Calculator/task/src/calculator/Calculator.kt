package calculator

import java.util.*

object Calculator {

    private val variables = mutableMapOf<String, Int>()

    fun process(line: String) {
        try {
            val tokens = TokensReader(line).result
            if (tokens.size > 2 && tokens.contains(Token.Assign)) {
                val token0 = tokens[0]
                if (token0 is Token.Variable) {
                    setVariable(token0, tokens.drop(2))
                } else {
                    throw CalculatorException("Invalid expression")
                }
            } else {
                println(evaluate(tokens))
            }
        } catch (ex: CalculatorException) {
            println(ex.message)
        }
    }

    private fun setVariable(variable: Token.Variable, tokens: List<Token>) {
        if (!isCorrectVariableName(variable.name))
            throw CalculatorException("Invalid identifier")
        variables[variable.name] = evaluate(tokens)
    }

    private fun evaluate(tokens: List<Token>): Int {
        val infixNotation = InfixToPostfixConverter.convert(tokens)
        val stack = Stack<Int>()
        val debug = StringBuilder()
        fun StringBuilder.addLog(s: String) {
            appendLine(s)
        }

        for (token in infixNotation) {
            when (token) {
                is Token.Number -> {
                    stack.push(token.value)
                }
                is Token.Variable -> {
                    stack.push(variables[token.name] ?: throw CalculatorException("Unknown variable"))
                }
                is Token.Operator -> {
                    debug.appendLine("$token")
                    val v2 = stack.pop()
                    val v1 = if (token is Token.Minus && stack.isEmpty()) {
                        0 // unary minus
                    } else {
                        stack.pop()
                    }
                    val result = token.evaluate(v1, v2)
                    stack.push(result)
                    debug.addLog("$v1 ${token.value} $v2 = $result")
                }
                else -> TODO("unknown token $token")
            }
            debug.appendLine()
        }

        return stack.pop()
    }

    private fun isCorrectVariableName(name: String): Boolean =
        name.matches(Regex("^[a-zA-Z]+$", RegexOption.MULTILINE))

}

