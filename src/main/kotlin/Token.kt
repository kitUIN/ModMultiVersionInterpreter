package io.github.kituin.modmultiversioninterpreter

import io.github.kituin.modmultiversioninterpreter.TokenType.*

data class Token(val type: TokenType, val value: String, val startPos: Int) {
    val belong: TokenBelong = when (type) {
        STRING -> if (value.startsWith("$")) TokenBelong.VARIANT else TokenBelong.STRING
        EQUAL, ALSO_EQUAL, NOT_EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL, NOT, AND, AND_ALSO, OR, OR_ALSO -> TokenBelong.OPERATOR
        LBRACKETS, RBRACKETS -> TokenBelong.BRACKETS
        EOF -> TokenBelong.EOF
    }
}
