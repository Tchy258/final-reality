package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.validCharacterGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.character.player.weapon.WeaponData.Companion.arbitraryWeaponGenerator
import cl.uchile.dcc.finalreality.model.character.player.weapon.WeaponData.Companion.validWeaponGenerator
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
            checkAll(validWeaponGenerator) { bow ->
                val randomBow1 = Bow(bow.name, bow.damage, bow.weight)
                val randomBow2 = Bow(bow.name, bow.damage, bow.weight)
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
                genA = validWeaponGenerator,
                genB = validWeaponGenerator
            ) { bow1, bow2 ->
                assume {
                    bow1.name != bow2.name ||
                        bow1.damage != bow2.damage ||
                        bow1.weight != bow2.weight
                }
                val randomBow1 = Bow(bow1.name, bow1.damage, bow1.weight)
                val randomBow2 = Bow(bow2.name, bow2.damage, bow2.weight)
                randomBow1 shouldNotBe randomBow2
            }
            bow1 shouldNotBe bow3
        }
    }
    context("Any Bow should:") {
        test("Not be null") {
            checkAll(validWeaponGenerator) { bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                randomBow shouldNotBe null
            }
            bow1 shouldNotBe null
            bow2 shouldNotBe null
            bow3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(validWeaponGenerator) { bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                randomBow shouldBe randomBow
            }
            bow1 shouldBe bow1
            bow2 shouldBe bow2
        }
        test("Have valid stats") {
            checkAll(arbitraryWeaponGenerator) { bow ->
                if (bow.damage < 0 || bow.weight <= 0) {
                    assertThrows<InvalidStatValueException> {
                        Bow(bow.name, bow.damage, bow.weight)
                    }
                } else {
                    assertDoesNotThrow {
                        Bow(bow.name, bow.damage, bow.weight)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Bow("", -1, -1)
            }
            assertDoesNotThrow {
                Bow("", 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            bow1.toString() shouldBe "Bow { name: 'TestBow', damage: 10, weight: 20 }"
            bow3.toString() shouldBe "Bow { name: 'TestBow2', damage: 20, weight: 10 }"
        }
        // Tests for equipTo... methods
        test("Be equippable to an Engineer") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) {
                bow, engineer ->
                // The queue is not relevant to the test so a fresh instance is made each time
                val testEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, LinkedBlockingQueue<GameCharacter>())
                val testBow = Bow(bow.name, bow.damage, bow.weight)
                assertDoesNotThrow {
                    testBow.equipToEngineer(testEngineer)
                }
            }
        }
        test("Be unequippable to a Knight") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) {
                bow, knight ->
                val testKnight = Knight(knight.name, knight.maxHp, knight.defense, LinkedBlockingQueue<GameCharacter>())
                val testBow = Bow(bow.name, bow.damage, bow.weight)
                assertThrows<InvalidWeaponException> {
                    testBow.equipToKnight(testKnight)
                }
            }
        }
        test("Be equippable to a Thief") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) { bow, thief ->
                val testThief = Thief(thief.name, thief.maxHp, thief.defense, LinkedBlockingQueue<GameCharacter>())
                val testBow = Bow(bow.name, bow.damage, bow.weight)
                assertDoesNotThrow {
                    testBow.equipToThief(testThief)
                }
            }
        }
        test("Be unequippable to a BlackMage") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) { bow, blackMage ->
                val testBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, LinkedBlockingQueue<GameCharacter>())
                val testBow = Bow(bow.name, bow.damage, bow.weight)
                assertThrows<InvalidWeaponException> {
                    testBow.equipToBlackMage(testBlackMage)
                }
            }
        }
        test("Be unequippable to a WhiteMage") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) { bow, whiteMage ->
                val testWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, LinkedBlockingQueue<GameCharacter>())
                val testBow = Bow(bow.name, bow.damage, bow.weight)
                assertThrows<InvalidWeaponException> {
                    testBow.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
