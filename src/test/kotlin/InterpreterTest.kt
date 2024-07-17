import io.github.kituin.modmultiversioninterpreter.Interpreter
import org.junit.jupiter.api.Test;
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
}
