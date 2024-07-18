package io.github.kituin.modmultiversioninterpreter

class ParseException(message: String?,val token:Token) : Exception(message) {
}