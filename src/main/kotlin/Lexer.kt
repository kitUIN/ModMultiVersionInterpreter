package io.github.kituin.modmultiversioninterpreter


class Lexer(private val text: String) {

    private var nextPos = 0

    private val tokenMap = mutableMapOf<String, TokenType>()
    private val tokenList = mutableListOf<Token>()
    private var nextChar: Char? = null

    init {
        TokenType.entries.forEach {
            if (it != TokenType.STRING) {
                tokenMap[it.value] = it
            }
        }
        nextChar = text.getOrNull(nextPos)
    }

    private fun createToken(char: String): Token? {
        val tokenType = tokenMap[char] ?: return null
        val pos = nextPos - 1
        return Token(tokenType, tokenType.value, pos)
    }

    private fun checkNextEqual(): Boolean {
        val oldChar = nextChar
        advance()
        val eq = nextChar == '='
        val token = createToken(if (eq) "$oldChar=" else oldChar.toString())
        if (token != null) {
            if (eq) advance()
            tokenList.add(token)
            if (!(oldChar == '!' && !eq)) checkBeforeAndAutoAdd()
            return true
        }
        return false
    }

    private fun checkNextAlso(): Boolean {
        val oldChar = nextChar
        advance()
        val eq = nextChar == oldChar
        val token = createToken(if (eq) "$oldChar$oldChar" else oldChar.toString())
        if (token != null) {
            if (eq) advance()
            tokenList.add(token)
            return true
        }
        return false
    }

    fun getTokens(): List<Token> {
        while (nextChar != null) {
            when {
                nextChar == ' ' -> skipWhiteSpace()
                tokenMap.containsKey(nextChar.toString()) -> processToken()
                else -> {
                    val pos = nextPos
                    tokenList.add(Token(TokenType.STRING, getString(), pos))
                    checkBeforeAndAutoAdd(isString = true)
                }
            }
        }
        return tokenList
    }

    private fun processToken() {
        when (nextChar) {
            '>', '<', '=', '!' -> checkNextEqual()
            '|', '&' -> checkNextAlso()
            else -> {
                createToken(nextChar.toString())?.let {
                    advance()
                    tokenList.add(it)
                }
            }
        }
    }

    private fun getString(): String {
        var item = ""
        while ((nextChar != null) && !(tokenMap.containsKey(nextChar!!.toString()))) {
            item += nextChar
            advance()
        }
        return item.trimEnd()
    }

    private fun skipWhiteSpace() {
        var nextChar = text.getOrNull(nextPos)
        while (nextChar != null && ' ' == nextChar) {
            advance()
            nextChar = text.getOrNull(nextPos)
        }
    }

    private fun checkBefore(): Boolean {
        val beforeToken = tokenList.getOrNull(tokenList.lastIndex - 1) ?: return true
        val currentToken = tokenList.getOrNull(tokenList.lastIndex)
        if (currentToken != null && currentToken.value == "$$") return false
        return beforeToken.type == TokenType.OR ||
                beforeToken.type == TokenType.AND ||
                beforeToken.type == TokenType.OR_ALSO ||
                beforeToken.type == TokenType.AND_ALSO ||
                beforeToken.type == TokenType.LBRACKETS ||
                beforeToken.type == TokenType.RBRACKETS
    }

    private fun checkBeforeAndAutoAdd(isString: Boolean = false) {
        if (checkBefore()) {
            tokenList.add(tokenList.lastIndex, Token(TokenType.STRING, "$$", -1))
            if (isString) tokenList.add(tokenList.lastIndex, Token(TokenType.EQUAL, TokenType.EQUAL.value, -1))
        }
    }

    private fun advance() {
        nextChar = text.getOrNull(++nextPos)
    }

    override fun toString(): String {
        return getTokens().joinToString(separator = " ") { token -> token.value }
    }
}

