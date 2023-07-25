package cl.uchile.dcc.finalreality.exceptions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows

class InconsistentReturnStateExceptionTest : FunSpec({
    val prefix = "Tried to transition from WeaponEquipState to "
    val mid = " but "
    val suffix = " was expected"

    test("An InconsistentReturnStateException can be thrown with a message") {
        checkAll<String, String> { someStateName, anotherStateName ->
            assertThrows<InconsistentReturnStateException> {
                throw InconsistentReturnStateException(
                    someStateName,
                    anotherStateName
                )
            }.message shouldBe prefix + someStateName + mid + anotherStateName + suffix
        }
    }
})
