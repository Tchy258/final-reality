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

class KnifeTest : FunSpec({
    lateinit var knife1: Knife
    lateinit var knife2: Knife
    lateinit var knife3: Knife

    beforeEach {
        knife1 = Knife("TestKnife", 10, 20)
        knife2 = Knife("TestKnife", 10, 20)
        knife3 = Knife("TestKnife2", 20, 10)
    }
    context("Two knifes with the same parameters should:") {
        test("Be equal") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomKnife1 =
                    Knife(name, damage, weight)
                val randomKnife2 =
                    Knife(name, damage, weight)
                randomKnife1 shouldBe randomKnife2
            }
            knife1 shouldBe knife2
        }
        test("Have the same hashcode") {
            knife1.hashCode() shouldBe knife2.hashCode()
        }
    }
    context("Two knifes with different parameters should:") {
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
                val randomKnife1 =
                    Knife(name1, damage1, weight1)
                val randomKnife2 =
                    Knife(name2, damage2, weight2)
                randomKnife1 shouldNotBe randomKnife2
            }
            knife1 shouldNotBe knife3
        }
    }
    context("Any Knife should:") {
        test("Not be null") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomKnife = Knife(name, damage, weight)
                randomKnife shouldNotBe null
            }
            knife1 shouldNotBe null
            knife2 shouldNotBe null
            knife3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomKnife = Knife(name, damage, weight)
                randomKnife shouldNotBe null
            }
            knife1 shouldBe knife1
            knife2 shouldBe knife2
        }
        // Tests toString() method
        test("Have a string representation") {
            knife1.toString() shouldBe "Knife { name: 'TestKnife', damage: 10, weight: 20 }"
            knife3.toString() shouldBe "Knife { name: 'TestKnife2', damage: 20, weight: 10 }"
        }
        // Tests for equipTo... methods
        test("Be unequippable to an Engineer") {
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
                val testKnife = Knife(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testKnife.equipToEngineer(testEngineer)
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
                val testKnife = Knife(weapName, damage, weight)
                assertDoesNotThrow {
                    testKnife.equipToKnight(testKnight)
                }
            }
        }
        test("Be equippable to a Thief") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val testThief = Thief(charName, maxHp, defense, LinkedBlockingQueue<GameCharacter>())
                val testKnife = Knife(weapName, damage, weight)
                assertDoesNotThrow {
                    testKnife.equipToThief(testThief)
                }
            }
        }
        test("Be equippable to a BlackMage") {
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
                val testKnife = Knife(weapName, damage, weight)
                assertDoesNotThrow {
                    testKnife.equipToBlackMage(testBlackMage)
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
                val testKnife = Knife(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testKnife.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
