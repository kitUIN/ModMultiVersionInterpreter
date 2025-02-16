import io.github.kituin.modmultiversioninterpreter.Lexer
import kotlin.test.Test
import kotlin.test.assertEquals

class LexerTest {
    @Test
    fun run1() {
        val lex = Lexer("fabric-1.16.5")
        println(lex.getTokens())
        assertEquals("$$ == fabric-1.16.5", lex.toString())
    }
    @Test
    fun run2() {
        val lex = Lexer("> fabric-1.16.5")
        println(lex.getTokens())
        assertEquals("$$ > fabric-1.16.5", lex.toString())
    }
    @Test
    fun run3() {
        val lex = Lexer(">= fabric-1.16.5 | fabric-1.17.1")
        println(lex.getTokens())
        assertEquals("$$ >= fabric-1.16.5 | $$ == fabric-1.17.1", lex.toString())
    }
}