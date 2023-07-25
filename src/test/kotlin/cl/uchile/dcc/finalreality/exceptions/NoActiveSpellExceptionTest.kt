package cl.uchile.dcc.finalreality.exceptions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows

class NoActiveSpellExceptionTest : FunSpec({
    val prefix = "The mage "
    val suffix = " tried to cast a spell with no active spell"

    test("A NoActiveSpellException can be thrown with a character's name in its message") {
        checkAll<String> {
            name ->
            assertThrows<NoActiveSpellException> {
                throw NoActiveSpellException(name)
            }.message shouldBe prefix + name + suffix
        }
    }
})
