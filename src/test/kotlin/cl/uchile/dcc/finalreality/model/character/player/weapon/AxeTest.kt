package cl.uchile.dcc.finalreality.model.character.player.weapon

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class AxeTest : FunSpec({
    lateinit var axe1: Axe
    lateinit var axe2: Axe
    lateinit var axe3: Axe

    beforeEach {
        axe1 = Axe("TestAxe", 10, 20)
        axe2 = Axe("TestAxe", 10, 20)
        axe3 = Axe("TestAxe2", 20, 10)
    }
    context("Two axes should") {
        test("Be equal") {
            checkAll<String, Int, Int> { name, damage, weight ->
                val randomAxe1 =
                    Axe(name, damage, weight)
                val randomAxe2 =
                    Axe(name, damage, weight)
                randomAxe1 shouldBe randomAxe2
            }
            axe1 shouldBe axe2
        }
        test("Have the same hashcode") {
            axe1.hashCode() shouldBe axe2.hashCode()
        }
    }
    test("Any Axe should have a string representation") {
        axe1.toString() shouldBe "Axe { name: 'TestAxe', damage: 10, weight: 20 }"
        axe3.toString() shouldBe "Axe { name: 'TestAxe2', damage: 20, weight: 10 }"
    }
})
