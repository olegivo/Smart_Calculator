package calculator

sealed class Token {
    abstract class Operand : Token()

    data class Number(val value: Int) : Operand()
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
        abstract fun evaluate(v1: Int, v2: Int): Int
    }

    object Plus : Operator('+', 1) {
        override fun evaluate(v1: Int, v2: Int): Int = v1 + v2
    }

    object Minus : Operator('-', 1) {
        override fun evaluate(v1: Int, v2: Int): Int = v1 - v2
    }

    object Product : Operator('*', 2) {
        override fun evaluate(v1: Int, v2: Int): Int = v1 * v2
    }

    object Division : Operator('/', 2) {
        override fun evaluate(v1: Int, v2: Int): Int = v1 / v2
    }
}
