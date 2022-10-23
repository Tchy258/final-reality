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

class BowTest : FunSpec({
    lateinit var bow1: Bow
    lateinit var bow2: Bow
    lateinit var bow3: Bow

    beforeEach {
        bow1 = Bow("TestBow", 10, 20)
        bow2 = Bow("TestBow", 10, 20)
        bow3 = Bow("TestBow2", 20, 10)
    }
    context("Two bows with the same parameters should:") {
        test("Be equal") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomBow1 =
                    Bow(name, damage, weight)
                val randomBow2 =
                    Bow(name, damage, weight)
                randomBow1 shouldBe randomBow2
            }
            bow1 shouldBe bow2
        }
        test("Have the same hashcode") {
            bow1.hashCode() shouldBe bow2.hashCode()
        }
    }
    context("Two bows with different parameters should:") {
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
                val randomBow1 =
                    Bow(name1, damage1, weight1)
                val randomBow2 =
                    Bow(name2, damage2, weight2)
                randomBow1 shouldNotBe randomBow2
            }
            bow1 shouldNotBe bow3
        }
    }
    context("Any Bow should:") {
        test("Not be null") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomBow = Bow(name, damage, weight)
                randomBow shouldNotBe null
            }
            bow1 shouldNotBe null
            bow2 shouldNotBe null
            bow3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt()
            ) { name, damage, weight ->
                val randomBow = Bow(name, damage, weight)
                randomBow shouldBe randomBow
            }
            bow1 shouldBe bow1
            bow2 shouldBe bow2
        }
        // Tests toString() method
        test("Have a string representation") {
            bow1.toString() shouldBe "Bow { name: 'TestBow', damage: 10, weight: 20 }"
            bow3.toString() shouldBe "Bow { name: 'TestBow2', damage: 20, weight: 10 }"
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
                val testBow = Bow(weapName, damage, weight)
                assertDoesNotThrow {
                    testBow.equipToEngineer(testEngineer)
                }
            }
        }
        test("Be unequippable to a Knight") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val testKnight = Knight(charName, maxHp, defense, LinkedBlockingQueue<GameCharacter>())
                val testBow = Bow(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testBow.equipToKnight(testKnight)
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
                val testBow = Bow(weapName, damage, weight)
                assertDoesNotThrow {
                    testBow.equipToThief(testThief)
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
                val testBow = Bow(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testBow.equipToBlackMage(testBlackMage)
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
                val testBow = Bow(weapName, damage, weight)
                assertThrows<InvalidWeaponException> {
                    testBow.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
