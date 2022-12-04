package cl.uchile.dcc.finalreality.model.character.magic

import cl.uchile.dcc.finalreality.model.magic.Magic
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Cure
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Paralysis
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Poison
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

/*
 * Since each specific magic's effect and damage is tested elsewhere,
 * only base methods like equals and to string are tested here
 */
class MagicTest : FunSpec({
    lateinit var thunder1: Thunder
    lateinit var thunder2: Thunder
    lateinit var fire1: Fire
    lateinit var fire2: Fire
    lateinit var poison1: Poison
    lateinit var poison2: Poison
    lateinit var paralysis1: Paralysis
    lateinit var paralysis2: Paralysis
    lateinit var cure1: Cure
    lateinit var cure2: Cure

    beforeEach {
        thunder1 = Thunder(20)
        thunder2 = Thunder(20)
        fire1 = Fire(10)
        fire2 = Fire(10)
        poison1 = Poison(5)
        poison2 = Poison(5)
        paralysis1 = Paralysis()
        paralysis2 = Paralysis()
        cure1 = Cure()
        cure2 = Cure()
    }
    context("Any magic spell should") {
        test("Be equal to other instances of itself and itself") {
            thunder1 shouldBe thunder1
            thunder1 shouldBe thunder2
            thunder2 shouldBe thunder2
            fire1 shouldBe fire1
            fire1 shouldBe fire2
            fire2 shouldBe fire2
            poison1 shouldBe poison1
            poison1 shouldBe poison2
            poison2 shouldBe poison2
            paralysis1 shouldBe paralysis1
            paralysis1 shouldBe paralysis2
            paralysis2 shouldBe paralysis2
            cure1 shouldBe cure1
            cure1 shouldBe cure2
            cure2 shouldBe cure2
        }
        test("Not be equal to other spells") {
            val spells = listOf<Magic>(
                thunder1,
                fire1,
                poison1,
                paralysis1,
                cure1
            )
            for (i in spells.indices) {
                for (j in i + 1 until spells.size) {
                    spells[i] shouldNotBe spells[j]
                }
            }
        }
        test("Have a string representation") {
            thunder1.toString() shouldBe "Thunder { cost: 15 }"
            fire1.toString() shouldBe "Fire { cost: 15 }"
            poison1.toString() shouldBe "Poison { cost: 40 }"
            paralysis1.toString() shouldBe "Paralysis { cost: 25 }"
            cure1.toString() shouldBe "Cure { cost: 15 }"
        }
    }
})
