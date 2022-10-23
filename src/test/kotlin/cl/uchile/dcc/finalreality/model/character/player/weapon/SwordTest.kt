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

class SwordTest : FunSpec({
    lateinit var sword1: Sword
    lateinit var sword2: Sword
    lateinit var sword3: Sword

    beforeEach {
        sword1 = Sword("TestSword", 10, 20)
        sword2 = Sword("TestSword", 10, 20)
        sword3 = Sword("TestSword2", 20, 10)
    }
    context("Two swords with the same parameters should:") {
        test("Be equal") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomSword1 =
                    Sword(name, damage, weight)
                val randomSword2 =
                    Sword(name, damage, weight)
                randomSword1 shouldBe randomSword2
            }
            sword1 shouldBe sword2
        }
        test("Have the same hashcode") {
            sword1.hashCode() shouldBe sword2.hashCode()
        }
    }
    context("Two swords with different parameters should:") {
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
                val randomSword1 =
                    Sword(name1, damage1, weight1)
                val randomSword2 =
                    Sword(name2, damage2, weight2)
                randomSword1 shouldNotBe randomSword2
            }
            sword1 shouldNotBe sword3
        }
    }
    context("Any Sword should:") {
        test("Not be null") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomSword = Sword(name, damage, weight)
                randomSword shouldNotBe null
            }
            sword1 shouldNotBe null
            sword2 shouldNotBe null
            sword3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomSword = Sword(name, damage, weight)
                randomSword shouldBe randomSword
            }
            sword1 shouldBe sword1
            sword2 shouldBe sword2
        }
        // Tests toString() method
        test("Have a string representation") {
            sword1.toString() shouldBe "Sword { name: 'TestSword', damage: 10, weight: 20 }"
            sword3.toString() shouldBe "Sword { name: 'TestSword2', damage: 20, weight: 10 }"
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
                val testSword = Sword(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testSword.equipToEngineer(testEngineer)
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
                val testSword = Sword(weapName, damage, weight)
                assertDoesNotThrow {
                    testSword.equipToKnight(testKnight)
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
                val testSword = Sword(weapName, damage, weight)
                assertDoesNotThrow {
                    testSword.equipToThief(testThief)
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
                val testSword = Sword(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testSword.equipToBlackMage(testBlackMage)
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
                val testSword = Sword(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testSword.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
