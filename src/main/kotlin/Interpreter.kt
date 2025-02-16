package io.github.kituin.modmultiversioninterpreter

/**
 * 原始划分
 *
 * fabric-1.16.5 划分为 fabric 与 1.16.5
 */
class SplitRaw(
    val prefix: String,
    val version: String
)


class Interpreter(private val raw: String, private val goal: Map<String, String>) {
    fun interpret(): Boolean {
        val ast = Parser(Lexer(raw)).parse()
        return visit(ast)
    }

    private fun extractNumbers(input: String): List<Int> {
        return Regex("\\d+").findAll(input).map { it.value.toInt() }.toList()
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
        return if (left.size == right.size) 0
        else if (left.size > right.size) 1
        else -1
    }

    private fun checkAndCompare(ast: BinOp, tokenType: TokenType): Boolean {
        if (ast.left !is ASTString || ast.right !is ASTString) {
            throw RuntimeException("error ast, $ast: both sides must be strings")
        }

        val leftRaw = splitPrefixAndVersion(resolveVariable(ast.left.toString()))
        val rightRaw = splitPrefixAndVersion(resolveVariable(ast.right.toString()))

        if (!haveSamePrefix(leftRaw.prefix, rightRaw.prefix)) return false

        return when {
            compareVersion(leftRaw.version, rightRaw.version) > 0 -> tokenType in setOf(
                TokenType.GREATER,
                TokenType.GREATER_EQUAL,
                TokenType.NOT_EQUAL
            )

            compareVersion(leftRaw.version, rightRaw.version) < 0 -> tokenType in setOf(
                TokenType.LESS,
                TokenType.LESS_EQUAL,
                TokenType.NOT_EQUAL
            )

            else -> tokenType in setOf(
                TokenType.EQUAL,
                TokenType.ALSO_EQUAL,
                TokenType.LESS_EQUAL,
                TokenType.GREATER_EQUAL
            )
        }
    }

    /**
     * 移除自动变量$
     */
    private fun resolveVariable(value: String): String =
        if (value.startsWith("$") && goal.containsKey(value)) goal[value].toString() else value

    /**
     * 划分前缀和版本号
     */
    private fun splitPrefixAndVersion(value: String): SplitRaw {
        val sp = value.split("-", limit = 2)
        if (sp.size == 2) {
            return SplitRaw(sp[0], sp[1])
        } else if (sp.size == 1) {
            return SplitRaw(sp[0], "")
        }
        return SplitRaw("", "")
    }

    /**
     * 检查相同前缀，支持通配符*
     */
    private fun haveSamePrefix(prefix1: String, prefix2: String): Boolean {
        if (prefix2 == "*") return true
        return prefix1 == prefix2
    }

    private fun visit(ast: AST): Boolean {
        when (ast) {
            is BinOp -> {
                return when (ast.op.type) {
                    TokenType.OR, TokenType.OR_ALSO -> {
                        visit(ast.left) || visit(ast.right)
                    }

                    TokenType.AND, TokenType.AND_ALSO -> {
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

