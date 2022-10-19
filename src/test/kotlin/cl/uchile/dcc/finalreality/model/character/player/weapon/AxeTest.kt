package cl.uchile.dcc.finalreality.model.character.player.weapon

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AxeTest: FunSpec( {
    lateinit var axe1: Axe
    lateinit var axe2: Axe
    lateinit var axe3: Axe

    beforeEach {
        axe1 = Axe("TestAxe", 10, 20)
        axe2 = Axe("TestAxe", 10, 20)
    }

    test("Two axes with the same parameters should be equal") {
        axe1 shouldBe axe2
    }
})