package cl.uchile.dcc.finalreality.exceptions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows

class InvalidWeaponExceptionTest : FunSpec({
    val prefix = "Attempted to equip a "
    val infix = " to a "

    test("An InvalidWeaponException can be thrown with the character and the weapon's name in its message") {
        checkAll<String, String> {
            weapName, charName ->
            assertThrows<InvalidWeaponException> {
                throw InvalidWeaponException(charName, weapName)
            }.message shouldBe prefix + weapName + infix + charName
        }
    }
})
