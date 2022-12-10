package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.concurrent.LinkedBlockingQueue

class NoDebuffTest : FunSpec({
    val nullBuff1 = NoDebuff()
    val nullBuff2 = NoDebuff()
    context("A NoDebuff adverse effect should") {
        test("Be equal to itself and instances of itself") {
            nullBuff1 shouldBe nullBuff1
            nullBuff1 shouldBe nullBuff2
        }
        test("Not be equal to other adverse effects") {
            val otherEffects = listOf(
                Paralyzed(),
                Poisoned(1),
                Burned(1)
            )
            for (effect in otherEffects) {
                nullBuff1 shouldNotBe effect
                nullBuff2 shouldNotBe effect
            }
        }
        test("Have a string representation") {
            nullBuff1.toString() shouldBe "NoDebuff"
        }
        test("Do nothing to a character when its effect is rolled") {
            val character = Knight("TestKnight", 100, 10, LinkedBlockingQueue<GameCharacter>())
            // All characters start with this null effect
            character.addDebuff(nullBuff1)
            character.rollEffects() shouldBe true
            character.currentHp shouldBe character.maxHp
        }
    }
})
