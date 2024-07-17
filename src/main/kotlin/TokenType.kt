package io.github.kituin

enum class TokenType(val value: String) {
    STRING("STRING"),
    EQUAL("=="), ALSO_EQUAL("="),
    NOT_EQUAL("!="),
    GREATER(">"), GREATER_EQUAL(">="),
    LESS("<"), LESS_EQUAL("<="),
    NOT("!"),
    AND("&"), AND_ALSO("&&"),
    OR("|"), OR_ALSO("||"),
    LBRACKETS("("), RBRACKETS(")"),
    EOF("EOF")
}