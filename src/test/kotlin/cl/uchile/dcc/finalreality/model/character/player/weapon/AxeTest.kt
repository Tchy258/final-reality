package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

class AxeTest : FunSpec({
    lateinit var axe1: Axe
    lateinit var axe2: Axe
    lateinit var axe3: Axe

    beforeEach {
        axe1 = Axe("TestAxe", 10, 20)
        axe2 = Axe("TestAxe", 10, 20)
        axe3 = Axe("TestAxe2", 20, 10)
    }
    context("Two axes with the same parameters should:") {
        test("Be equal") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
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
        test("Be equal to themselves") {
            axe1 shouldBe axe1
            axe2 shouldBe axe2
        }
    }
    context("Two axes with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt()
            ) { name1, damage1, weight1, name2, damage2, weight2 ->
                assume {
                    name1 != name2 ||
                        damage1 != damage2 ||
                        weight1 != weight2
                }
                val randomAxe1 =
                    Axe(name1, damage1, weight1)
                val randomAxe2 =
                    Axe(name2, damage2, weight2)
                randomAxe1 shouldNotBe randomAxe2
            }
            axe1 shouldNotBe axe3
        }
    }
    context("Any Axe should:") {
        test("Not be null") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomAxe = Axe(name, damage, weight)
                randomAxe shouldNotBe null
            }
            axe1 shouldNotBe null
            axe2 shouldNotBe null
            axe3 shouldNotBe null
        }
        // Tests toString() method
        test("Have a string representation") {
            axe1.toString() shouldBe "Axe { name: 'TestAxe', damage: 10, weight: 20 }"
            axe3.toString() shouldBe "Axe { name: 'TestAxe2', damage: 20, weight: 10 }"
        }
        // Tests for equipTo... methods
        test("Be equippable to an Engineer") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt()
            ) {
                charName, maxHp, defense, weapName, damage, weight ->
                // The queue is not relevant to the test so a fresh instance is made each time
                val testEngineer = Engineer(charName, maxHp, defense, LinkedBlockingQueue<GameCharacter>())
                val testAxe = Axe(weapName, damage, weight)
                assertDoesNotThrow {
                    testAxe.equipToEngineer(testEngineer)
                }
            }
        }
        test("Be equippable to a Knight") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val testKnight = Knight(charName, maxHp, defense, LinkedBlockingQueue<GameCharacter>())
                val testAxe = Axe(weapName, damage, weight)
                assertDoesNotThrow {
                    testAxe.equipToKnight(testKnight)
                }
            }
        }
        test("Be unequippable to a Thief") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val testThief = Thief(charName, maxHp, defense, LinkedBlockingQueue<GameCharacter>())
                val testAxe = Axe(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testAxe.equipToThief(testThief)
                }
            }
        }
        test("Be unequippable to a BlackMage") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.string(),
                genF = Arb.nonNegativeInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight ->
                val testBlackMage = BlackMage(charName, maxHp, maxMp, defense, LinkedBlockingQueue<GameCharacter>())
                val testAxe = Axe(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testAxe.equipToBlackMage(testBlackMage)
                }
            }
        }
        test("Be unequippable to a WhiteMage") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.string(),
                genF = Arb.nonNegativeInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight ->
                val testWhiteMage = WhiteMage(charName, maxHp, maxMp, defense, LinkedBlockingQueue<GameCharacter>())
                val testAxe = Axe(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testAxe.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
