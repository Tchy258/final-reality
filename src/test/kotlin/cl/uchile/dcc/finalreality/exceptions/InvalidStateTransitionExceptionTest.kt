package cl.uchile.dcc.finalreality.exceptions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows

class InvalidStateTransitionExceptionTest : FunSpec({
    val prefix = "Invalid state transition from "
    val mid = " to "

    test("An InvalidStateTransitionException can be thrown with a message") {
        checkAll<String, String> { someStateName, anotherStateName ->
            assertThrows<InvalidStateTransitionException> {
                throw InvalidStateTransitionException(
                    someStateName,
                    anotherStateName
                )
            }.message shouldBe prefix + someStateName + mid + anotherStateName
        }
    }
})
