package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
            checkAll(WeaponData.validWeaponGenerator) { axe ->
                val randomAxe1 = Axe(axe.name, axe.damage, axe.weight)
                val randomAxe2 = Axe(axe.name, axe.damage, axe.weight)
                randomAxe1 shouldBe randomAxe2
            }
            axe1 shouldBe axe2
        }
        test("Have the same hashcode") {
            axe1.hashCode() shouldBe axe2.hashCode()
        }
    }
    context("Two axes with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = WeaponData.validWeaponGenerator
            ) { axe1, axe2 ->
                assume {
                    axe1.name != axe2.name ||
                        axe1.damage != axe2.damage ||
                        axe1.weight != axe2.weight
                }
                val randomAxe1 = Axe(axe1.name, axe1.damage, axe1.weight)
                val randomAxe2 = Axe(axe2.name, axe2.damage, axe2.weight)
                randomAxe1 shouldNotBe randomAxe2
            }
            axe1 shouldNotBe axe3
        }
    }
    context("Any Axe should:") {
        test("Not be null") {
            checkAll(WeaponData.validWeaponGenerator) { axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                randomAxe shouldNotBe null
            }
            axe1 shouldNotBe null
            axe2 shouldNotBe null
            axe3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(WeaponData.validWeaponGenerator) { axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                randomAxe shouldBe randomAxe
            }
            axe1 shouldBe axe1
            axe2 shouldBe axe2
        }
        test("Have valid stats") {
            checkAll(WeaponData.arbitraryWeaponGenerator) { axe ->
                if (axe.damage < 0 || axe.weight <= 0) {
                    assertThrows<InvalidStatValueException> {
                        Axe(axe.name, axe.damage, axe.weight)
                    }
                } else {
                    assertDoesNotThrow {
                        Axe(axe.name, axe.damage, axe.weight)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Axe("", -1, -1)
            }
            assertDoesNotThrow {
                Axe("", 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            axe1.toString() shouldBe "Axe { name: 'TestAxe', damage: 10, weight: 20 }"
            axe3.toString() shouldBe "Axe { name: 'TestAxe2', damage: 20, weight: 10 }"
        }
        // Tests for equipTo... methods
        test("Be equippable to an Engineer") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = CharacterData.validCharacterGenerator
            ) { axe, engineer ->
                // The queue is not relevant to the test so a fresh instance is made each time
                val testEngineer = Engineer(
                    engineer.name,
                    engineer.maxHp,
                    engineer.defense,
                    LinkedBlockingQueue<GameCharacter>()
                )
                val testAxe = Axe(axe.name, axe.damage, axe.weight)
                assertDoesNotThrow {
                    testAxe.equipToEngineer(testEngineer)
                }
            }
        }
        test("Be equippable to a Knight") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = CharacterData.validCharacterGenerator
            ) { axe, knight ->
                // The queue is not relevant to the test so a fresh instance is made each time
                val testKnight = Knight(
                    knight.name,
                    knight.maxHp,
                    knight.defense,
                    LinkedBlockingQueue<GameCharacter>()
                )
                val testAxe = Axe(axe.name, axe.damage, axe.weight)
                assertDoesNotThrow {
                    testAxe.equipToKnight(testKnight)
                }
            }
        }
        test("Be unequippable to a Thief") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = CharacterData.validCharacterGenerator
            ) { axe, thief ->
                val testThief = Thief(
                    thief.name,
                    thief.maxHp,
                    thief.defense,
                    LinkedBlockingQueue<GameCharacter>()
                )
                val testAxe = Axe(axe.name, axe.damage, axe.weight)
                assertThrows<InvalidWeaponException> {
                    testAxe.equipToThief(testThief)
                }
            }
        }
        test("Be unequippable to a BlackMage") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = MageData.validMageGenerator
            ) { axe, blackMage ->
                val testBlackMage = BlackMage(
                    blackMage.name,
                    blackMage.maxHp,
                    blackMage.maxMp,
                    blackMage.defense,
                    LinkedBlockingQueue<GameCharacter>()
                )
                val testAxe = Axe(axe.name, axe.damage, axe.weight)
                assertThrows<InvalidWeaponException> {
                    testAxe.equipToBlackMage(testBlackMage)
                }
            }
        }
        test("Be unequippable to a WhiteMage") {
            checkAll(
                genA = WeaponData.validWeaponGenerator,
                genB = MageData.validMageGenerator
            ) { axe, whiteMage ->
                val testWhiteMage = WhiteMage(
                    whiteMage.name,
                    whiteMage.maxHp,
                    whiteMage.maxMp,
                    whiteMage.defense,
                    LinkedBlockingQueue<GameCharacter>()
                )
                val testAxe = Axe(axe.name, axe.damage, axe.weight)
                assertThrows<InvalidWeaponException> {
                    testAxe.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})