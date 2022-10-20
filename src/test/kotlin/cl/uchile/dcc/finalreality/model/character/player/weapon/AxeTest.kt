package cl.uchile.dcc.finalreality.model.character.player.weapon

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class AxeTest: WordSpec( {
    lateinit var axe1: Axe
    lateinit var axe2: Axe
    lateinit var axe3: Axe

    beforeEach {
        axe1 = Axe("TestAxe", 10, 20)
        axe2 = Axe("TestAxe", 10, 20)
        axe3 = Axe("TestAxe2", 20, 10)
    }

    "Two axes with the same parameters" should {
        "Be equal" {
            checkAll<String, Int, Int> { name, damage, weight ->
                val axet1 =
                    Axe(name, damage, weight)
                val axet2 =
                    Axe(name, damage, weight)
                axet1 shouldBe axet2
            }
            axe1 shouldBe axe2
        }
        "Have the same hashcode" {
            axe1.hashCode() shouldBe axe2.hashCode()
        }
        "Be equal to themselves" {
            axe1 shouldBe axe1
            axe2 shouldBe axe2
        }
    }
    "Any Axe" should {
        "Have a string representation" {
            axe1.toString() shouldBe "Axe { name: 'TestAxe', damage: 10, weight: 20 }"
            axe3.toString() shouldBe "Axe { name: 'TestAxe2', damage: 20, weight: 10 }"
        }
    }

})