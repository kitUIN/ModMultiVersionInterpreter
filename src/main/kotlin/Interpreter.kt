package io.github.kituin.modmultiversioninterpreter


class Interpreter(private val raw: String, private val goal: Map<String, String>) {
    fun interpret(): Boolean {
        val ast = Parser(Lexer(raw)).parse()
        return visit(ast)
    }

    private fun extractNumbers(input: String): List<Int> {
        return Regex("\\d+").findAll(input).map { it -> it.value.toInt() }.toList()
    }

    private fun removeNumbers(input: String): String {
        return input.replace(Regex("\\d+"), "")
    }

    private fun compareVersion(s1: String, s2: String): Int {
        val left = extractNumbers(s1)
        val right = extractNumbers(s2)
        val minSize = minOf(left.size, right.size)
        for (i in 0 until minSize) {
            if (left[i] > right[i]) return 1
            if (left[i] < right[i]) return -1
        }
        if (left.size == right.size) return 0
        else if (left.size > right.size) return 1
        else return -1
    }

    private fun checkAndCompare(ast: BinOp, tokenType: TokenType): Boolean {
        if ((ast.left is ASTString && ast.right is ASTString)) {
            var leftRaw = ast.left.toString()
            if (leftRaw.startsWith("$") && goal.containsKey(leftRaw))
                leftRaw = goal[leftRaw].toString()
            var rightRaw = ast.right.toString()
            if (rightRaw.startsWith("$") && goal.containsKey(rightRaw))
                rightRaw = goal[rightRaw].toString()
            val leftString = removeNumbers(leftRaw)
            val rightString = removeNumbers(rightRaw)
            if (leftString != rightString) return false
            val res = compareVersion(leftRaw, rightRaw)
            if (res > 0) {
                if (tokenType == TokenType.GREATER ||
                    tokenType == TokenType.GREATER_EQUAL ||
                    tokenType == TokenType.NOT_EQUAL
                ) return true
            } else if (res < 0) {
                if (tokenType == TokenType.LESS ||
                    tokenType == TokenType.LESS_EQUAL ||
                    tokenType == TokenType.NOT_EQUAL
                ) return true
            } else {
                if (tokenType == TokenType.EQUAL ||
                    tokenType == TokenType.LESS_EQUAL ||
                    tokenType == TokenType.GREATER_EQUAL
                ) return true
            }
            return false
        }
        throw RuntimeException("error ast, $ast: both sides must be strings")
    }

    private fun visit(ast: AST): Boolean {
        when (ast) {
            is BinOp -> {
                return when (ast.op.type) {
                    TokenType.OR -> {
                        visit(ast.left) || visit(ast.right)
                    }

                    TokenType.AND -> {
                        visit(ast.left) && visit(ast.right)
                    }

                    else -> {
                        checkAndCompare(ast, ast.op.type)
                    }
                }
            }

            is NotOp -> {
                return !visit(ast.operand)
            }

            else -> {
                throw RuntimeException("error ast")
            }
        }
    }
}

