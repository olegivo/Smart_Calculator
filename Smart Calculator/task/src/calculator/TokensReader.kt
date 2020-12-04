package calculator

class TokensReader(private val line: String) {

    val result by lazy {
        val currentToken = StringBuilder()
        val tokens = mutableListOf<Token>()
        var wasAssign = false
        var wasVariable = false
        var openGapsCount = 0
        var closeGapsCount = 0
        var prevSymbol: Char? = null

        fun saveCurrentToken() {
            if (currentToken.isNotEmpty()) {
                tokens.add(fromString(currentToken))
                currentToken.clear()
                if (prevSymbol!! in symbols) wasVariable = true
            }
        }

        for (c in line) {
            var setPrevSymbolInTheEnd = true
            when (c) {
                !in allowedSymbols -> {
                    throw CalculatorException("Invalid expression")
                }
                in symbols -> {
                    if (prevSymbol != null && prevSymbol !in symbols) {
                        if (prevSymbol in digits) throw CalculatorException("Invalid identifier")
                        saveCurrentToken()
                    }
                    currentToken.append(c)
                }
                in digits -> {
                    if (prevSymbol != null && prevSymbol !in digits) {
                        if (prevSymbol in symbols) throw CalculatorException("Invalid identifier")
                        saveCurrentToken()
                    }
                    currentToken.append(c)
                }
                assign -> {
                    saveCurrentToken()
                    if (!wasVariable) throw CalculatorException("Invalid expression")
                    if (wasAssign) throw CalculatorException("Invalid expression")
                    wasAssign = true
                    currentToken.append(c)
                }
                gapOpen -> {
                    saveCurrentToken()
                    currentToken.append(c)
                    openGapsCount++
                }
                gapClose -> {
                    saveCurrentToken()
                    currentToken.append(c)
                    closeGapsCount++
                    if (prevSymbol == gapOpen) throw CalculatorException("Invalid expression")
                    if (closeGapsCount > openGapsCount) throw CalculatorException("Invalid expression")
                }
                plus -> {
                    when (prevSymbol) {
                        minus -> throw CalculatorException("Invalid expression")
                        plus -> {
                            // 2 times '+', do nothing
                        }
                        else -> {
                            saveCurrentToken()
                            currentToken.append(c)
                        }
                    }
                }
                minus -> {
                    when (prevSymbol) {
                        minus -> {
                            currentToken.append(c)
                        }
                        plus -> {
                            // '-' '+' is '-', do not change prev value, just skip current
                            setPrevSymbolInTheEnd = false
                        }
                        else -> {
                            saveCurrentToken()
                            currentToken.append(c)
                        }
                    }
                }
                product, division -> {
                    if (prevSymbol == c) throw CalculatorException("Invalid expression")
                    saveCurrentToken()
                    currentToken.append(c)
                }
                in special -> {
                    saveCurrentToken()
                    currentToken.append(c)
                }
                space -> {
                    if (prevSymbol in symbols || prevSymbol == assign) saveCurrentToken()
                }
            }

            if (setPrevSymbolInTheEnd) prevSymbol = c
        }
        saveCurrentToken()
        if (openGapsCount != closeGapsCount) throw CalculatorException("Invalid expression")

        tokens.toList()
    }

    companion object {
        private val symbols = ('a'..'z') + ('A'..'Z')
        private val digits = '0'..'9'
        private const val gapOpen = '('
        private const val gapClose = ')'
        private const val plus = '+'
        private const val minus = '-'
        private const val product = '*'
        private const val division = '/'
        private const val assign = '='
        private const val space = ' '
        private val special = listOf(gapOpen, gapClose, plus, minus, product, division, assign)
        private val allowedSymbols =
            symbols + digits + gapOpen + gapClose + plus + minus + product + division + assign + space

        fun fromString(s: StringBuilder): Token =
            when (val lastSymbol = s.last()) {
                in digits -> Token.Number(s.toString())
                in symbols -> Token.Variable(s.toString())
                minus -> if (s.length % 2 == 0) Token.Plus else Token.Minus
                else -> Token.SpecialToken.All.single { it.value == lastSymbol }
            }
    }
}
