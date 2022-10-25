package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
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
            checkAll(WeaponData.validWeaponGenerator) { sword ->
                val randomSword1 =
                    Sword(sword.name, sword.damage, sword.weight)
                val randomSword2 =
                    Sword(sword.name, sword.damage, sword.weight)
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
                genA = WeaponData.validWeaponGenerator,
                genB = WeaponData.validWeaponGenerator
            ) { sword1, sword2 ->
                assume {
                    sword1.name != sword2.name ||
                        sword1.damage != sword2.damage ||
                        sword1.weight != sword2.weight
                }
                val randomSword1 = Sword(sword1.name, sword1.damage, sword1.weight)
                val randomSword2 = Sword(sword2.name, sword2.damage, sword2.weight)
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
            checkAll(WeaponData.validWeaponGenerator) { sword ->
                val randomSword = Sword(sword.name, sword.damage, sword.weight)
                randomSword shouldBe randomSword
            }
            sword1 shouldBe sword1
            sword2 shouldBe sword2
        }
        test("Have valid stats") {
            checkAll(WeaponData.arbitraryWeaponGenerator) { sword ->
                if (sword.damage < 0 || sword.weight <= 0) {
                    assertThrows<InvalidStatValueException> {
                        Sword(sword.name, sword.damage, sword.weight)
                    }
                } else {
                    assertDoesNotThrow {
                        Sword(sword.name, sword.damage, sword.weight)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Sword("", -1, -1)
            }
            assertDoesNotThrow {
                Sword("", 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            sword1.toString() shouldBe "Sword { name: 'TestSword', damage: 10, weight: 20 }"
            sword3.toString() shouldBe "Sword { name: 'TestSword2', damage: 20, weight: 10 }"
        }
        // Tests for equipTo... methods
        test("Be unequippable to an Engineer") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = CharacterData.validCharacterGenerator
            ) { sword, engineer ->
                // The queue is not relevant to the test so a fresh instance is made each time
                val testEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, LinkedBlockingQueue<GameCharacter>())
                val testSword = Sword(sword.name, sword.damage, sword.weight)
                assertThrows<InvalidWeaponException> {
                    testSword.equipToEngineer(testEngineer)
                }
            }
        }
        test("Be equippable to a Knight") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = CharacterData.validCharacterGenerator
            ) { sword, knight ->
                val testKnight = Knight(knight.name, knight.maxHp, knight.defense, LinkedBlockingQueue<GameCharacter>())
                val testSword = Sword(sword.name, sword.damage, sword.weight)
                assertDoesNotThrow {
                    testSword.equipToKnight(testKnight)
                }
            }
        }
        test("Be equippable to a Thief") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = CharacterData.validCharacterGenerator
            ) { sword, thief ->
                val testThief = Thief(thief.name, thief.maxHp, thief.defense, LinkedBlockingQueue<GameCharacter>())
                val testSword = Sword(sword.name, sword.damage, sword.weight)
                assertDoesNotThrow {
                    testSword.equipToThief(testThief)
                }
            }
        }
        test("Be unequippable to a BlackMage") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = CharacterData.validCharacterGenerator
            ) { sword, blackMage ->
                val testBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, LinkedBlockingQueue<GameCharacter>())
                val testSword = Sword(sword.name, sword.damage, sword.weight)
                assertThrows<InvalidWeaponException> {
                    testSword.equipToBlackMage(testBlackMage)
                }
            }
        }
        test("Be unequippable to a WhiteMage") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = CharacterData.validCharacterGenerator
            ) { sword, whiteMage ->
                val testWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, LinkedBlockingQueue<GameCharacter>())
                val testSword = Sword(sword.name, sword.damage, sword.weight)
                assertThrows<InvalidWeaponException> {
                    testSword.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
