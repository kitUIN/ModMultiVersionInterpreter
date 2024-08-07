package io.github.kituin.modmultiversioninterpreter


open class AST

class BinOp(val left: AST, val op: Token, val right: AST) : AST() {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("op:").append(op.value).append("\n左:").append(left.toString()).append("\n右:")
            .append(right.toString())
        return sb.toString()
    }
}

class NotOp(val operand: AST) : AST() {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("op:!").append("\noperand:").append(operand.toString())
        return sb.toString()
    }
}

class ASTString(private val token: Token) : AST() {
    override fun toString() = token.value
}

class Parser(lexer: Lexer) {
    private var index: Int = -1
    val tokenList: List<Token> = lexer.getTokens()
    private var curToken = next()
    fun parse(): AST = exp()
    private fun next(): Token {
        return tokenList.getOrNull(++index) ?: Token(TokenType.EOF, TokenType.EOF.value, -1)
    }

    private fun eat(tokenType: TokenType) {
        if (tokenType == curToken.type) {
            curToken = next()
        } else {
            throw ParseException("${curToken.value}:grammatical formatting error [position: ${curToken.startPos}]", curToken)
        }
    }

    private fun exp(): AST {
        var node = term()
        while (TokenType.AND == curToken.type ||
            TokenType.AND_ALSO == curToken.type ||
            TokenType.OR == curToken.type ||
            TokenType.OR_ALSO == curToken.type) {
            val tmpToken = curToken
            eat(tmpToken.type)
            node = BinOp(node, tmpToken, term())
        }
        return node
    }

    fun term(): AST {
        var node: AST = factor()
        while (TokenType.EQUAL == curToken.type ||
            TokenType.ALSO_EQUAL == curToken.type ||
            TokenType.NOT_EQUAL == curToken.type ||
            TokenType.LESS == curToken.type || TokenType.GREATER == curToken.type ||
            TokenType.LESS_EQUAL == curToken.type || TokenType.GREATER_EQUAL == curToken.type
        ) {
            val tmpToken = curToken
            eat(tmpToken.type)
            node = BinOp(node, tmpToken, factor())
        }
        return node
    }

    fun factor(): AST {
        val tmpToken = curToken
        return when {
            TokenType.STRING == curToken.type -> {
                eat(TokenType.STRING)
                ASTString(tmpToken)
            }

            TokenType.LBRACKETS == curToken.type -> {
                eat(TokenType.LBRACKETS)
                val node = exp()
                eat(TokenType.RBRACKETS)
                node
            }

            TokenType.NOT == curToken.type -> {
                eat(TokenType.NOT)
                NotOp(factor())
            }

            else -> {
                throw ParseException("${curToken.value}:grammatical formatting error [position: ${curToken.startPos}]", curToken)
            }
        }
    }

}

