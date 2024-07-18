import io.github.kituin.modmultiversioninterpreter.Lexer
import io.github.kituin.modmultiversioninterpreter.Parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParserTest {
    @Test
    fun run1() {
        val parser = Parser(Lexer("  "))
        println(parser.parse())
        assertTrue(true)
    }
    @Test
    fun run3() {
        val parser = Parser(Lexer(">= fabric-1.16.5 || fabric-1.17.1"))
        println(parser.parse())
        assertTrue(true)
    }
}