package calculator

import java.util.*

object InfixToPostfixConverter {

    /*
    * 1. Add operands (numbers and variables) to the result (postfix notation) as they arrive.
    * 2. If the stack is empty or contains a left parenthesis on top, push the incoming operator on the stack.
    * 3. If the incoming operator has higher precedence than the top of the stack, push it on the stack.
    * 4. If the incoming operator has lower or equal precedence than or to the top of the stack, pop the stack and add operators to the result until you see an operator that has a smaller precedence or a left parenthesis on the top of the stack; then add the incoming operator to the stack.
    * 5. If the incoming element is a left parenthesis, push it on the stack.
    * 6. If the incoming element is a right parenthesis, pop the stack and add operators to the result until you see a left parenthesis. Discard the pair of parentheses.
    * 7. At the end of the expression, pop the stack and add all operators to the result.
    * */
    fun convert(tokens: List<Token>): List<Token> {
        val stack = Stack<Token.SpecialToken>()
        val result = mutableListOf<Token>()

        for (token in tokens) {
            if (token is Token.Operand) {
                result += token // 1. Add operands (numbers and variables) to the result (postfix notation) as they arrive.
            } else if (token is Token.SpecialToken) {
                if (stack.isEmpty()) {
                    stack.push(token) // 2. If the stack is empty, push the incoming operator on the stack.
                } else {
                    val onTop = stack.peek()
                    when {
                        onTop is Token.GapOpen -> {
                            stack.push(token) // 2. If the stack contains a left parenthesis on top, push the incoming operator on the stack.
                        }
                        token is Token.Operator -> {
                            if (onTop is Token.Operator) {
                                if (token > onTop) {
                                    stack.push(token) // 3. If the incoming operator has higher precedence than the top of the stack, push it on the stack.
                                } else {
                                    /* 4. If the incoming operator has lower or equal precedence than or to the top of the stack,
                                                pop the stack and add operators to the result
                                                until you see an operator that has a smaller precedence or a left parenthesis on the top of the stack;
                                                then add the incoming operator to the stack.*/
                                    while (stack.isNotEmpty() &&
                                        !stack.peek()
                                            .let { it is Token.GapOpen || (it is Token.Operator && token > it) }
                                    ) {
                                        result += stack.pop()
                                    }
                                    stack.push(token)
                                }
                            }
                        }
                        token is Token.GapOpen -> {
                            // 5. If the incoming element is a left parenthesis, push it on the stack.
                            stack.push(token)
                        }
                        token is Token.GapClose -> {
                            /* 6. If the incoming element is a right parenthesis,
                            pop the stack and add operators to the result until you see a left parenthesis.
                            Discard the pair of parentheses.*/
                            while (stack.isNotEmpty() && !stack.peek().let { it is Token.GapOpen }) {
                                result += stack.pop()
                            }
                            stack.pop()
                        }
                    }
                }

            }
        }

        // 7. At the end of the expression, pop the stack and add all operators to the result.
        while (stack.isNotEmpty()) result += stack.pop()

        return result
    }

}
