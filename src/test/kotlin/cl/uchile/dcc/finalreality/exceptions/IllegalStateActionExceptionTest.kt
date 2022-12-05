package cl.uchile.dcc.finalreality.exceptions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows

class IllegalActionExceptionTest : FunSpec({
    val prefix = "Tried to "
    val mid = " while on the "

    test("An IllegalActionException can be thrown with a message") {
        checkAll<String, String> { action, someStateName ->
            assertThrows<IllegalStateActionException> {
                throw IllegalStateActionException(
                    action,
                    someStateName
                )
            }.message shouldBe prefix + action + mid + someStateName
        }
    }
})
