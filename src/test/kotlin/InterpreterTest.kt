import io.github.kituin.modmultiversioninterpreter.Interpreter
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InterpreterTest {

    @Test
    fun run1() {
        val interpreter = Interpreter(">=fabric-1.20", mutableMapOf(
            "$$" to "fabric-1.20.3"
        ))
        assertTrue(interpreter.interpret())
    }
    @Test
    fun run2() {
        val interpreter = Interpreter(">=fabric-1.20", mutableMapOf(
            "$$" to "fabric-1.20----3"
        ))
        assertTrue(interpreter.interpret())
    }
    @Test
    fun run25() {
        val interpreter = Interpreter(">=fabric-1.20", mutableMapOf(
            "$$" to "fabric-1.20.3"
        ))
        assertTrue(interpreter.interpret())
    }
    @Test
    fun run3() {
        val interpreter = Interpreter("$$ == fabric-1.16.5", mutableMapOf(
            "$$" to "fabric-1.16.5"
        ))
        assertTrue(interpreter.interpret())
    }
    @Test
    fun run4() {
        val interpreter = Interpreter("fabric-1.20", mutableMapOf(
            "$$" to "fabric-1.20.3"
        ))
        assertFalse(interpreter.interpret())
    }
    @Test
    fun run5() {
        val interpreter = Interpreter("*-1.20", mutableMapOf(
            "$$" to "fabric-1.20"
        ))
        assertTrue(interpreter.interpret())
    }
    @Test
    fun run6() {
        val interpreter = Interpreter(">=*-1.20", mutableMapOf(
            "$$" to "forge-1.20.3"
        ))
        assertTrue(interpreter.interpret())
    }
}
