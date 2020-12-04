package calculator

import java.math.BigInteger

sealed class Token {
    abstract class Operand : Token()

    data class Number(val value: String) : Operand()
    data class Variable(val name: String) : Operand()

    abstract class SpecialToken(val value: Char) : Token() {
        companion object {
            val All = listOf(
                GapOpen,
                GapClose,
                Plus,
                Minus,
                Product,
                Division,
                Assign
            )
        }
    }

    object GapOpen : SpecialToken('(')
    object GapClose : SpecialToken(')')
    object Assign : SpecialToken('=')

    abstract class Operator(value: Char, private val priority: Int) : SpecialToken(value) {
        operator fun compareTo(other: Operator): Int = priority.compareTo(other.priority)
        abstract fun evaluate(v1: String, v2: String): String
    }

    object Plus : Operator('+', 1) {
        override fun evaluate(v1: String, v2: String) =
            (BigInteger(v1) + BigInteger(v2)).toString()
    }

    object Minus : Operator('-', 1) {
        override fun evaluate(v1: String, v2: String) =
            (BigInteger(v1) - BigInteger(v2)).toString()
    }

    object Product : Operator('*', 2) {
        override fun evaluate(v1: String, v2: String) =
            (BigInteger(v1) * BigInteger(v2)).toString()
    }

    object Division : Operator('/', 2) {
        override fun evaluate(v1: String, v2: String) =
            (BigInteger(v1) / BigInteger(v2)).toString()
    }
}
