package cl.uchile.dcc.finalreality.exceptions

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMageTestingFactory
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Cure
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

class InvalidSpellCastExceptionTest : FunSpec({
    val prefix = "A "
    val mid = " tried to cast "
    val suffix = ", magic type mismatch"

    test("An InvalidSpellCastException can be thrown with a mage and spell in its message") {
        checkAll(MageData.validGenerator) {
            mage ->
            val queue = LinkedBlockingQueue<GameCharacter>()
            val randomBlackMage = BlackMageTestingFactory(queue).create(mage)
            val randomWhiteMage = WhiteMageTestingFactory(queue).create(mage)
            assertThrows<InvalidSpellCastException> {
                throw InvalidSpellCastException(
                    randomBlackMage::class.simpleName!!,
                    Cure()::class.simpleName!!
                )
            }.message shouldBe prefix + "BlackMage" + mid + "Cure" + suffix
            assertThrows<InvalidSpellCastException> {
                throw InvalidSpellCastException(
                    randomWhiteMage::class.simpleName!!,
                    Thunder()::class.simpleName!!
                )
            }.message shouldBe prefix + "WhiteMage" + mid + "Thunder" + suffix
        }
    }
})
