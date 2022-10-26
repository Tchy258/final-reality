package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.validCharacterGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData.Companion.validMageGenerator
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
            checkAll(validWeaponGenerator) { knife ->
                val randomKnife1 =
                    Knife(knife.name, knife.damage, knife.weight)
                val randomKnife2 =
                    Knife(knife.name, knife.damage, knife.weight)
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
                genA = validWeaponGenerator,
                genB = validWeaponGenerator
            ) { knife1, knife2 ->
                assume {
                    knife1.name != knife2.name ||
                        knife1.damage != knife2.damage ||
                        knife1.weight != knife2.weight
                }
                val randomKnife1 = Knife(knife1.name, knife1.damage, knife1.weight)
                val randomKnife2 = Knife(knife2.name, knife2.damage, knife2.weight)
                randomKnife1 shouldNotBe randomKnife2
            }
            knife1 shouldNotBe knife3
        }
    }
    context("Any Knife should:") {
        test("Not be null") {
            checkAll(validWeaponGenerator) { knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                randomKnife shouldNotBe null
            }
            knife1 shouldNotBe null
            knife2 shouldNotBe null
            knife3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(validWeaponGenerator) { knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                randomKnife shouldBe randomKnife
            }
            knife1 shouldBe knife1
            knife2 shouldBe knife2
        }
        test("Have valid stats") {
            checkAll(arbitraryWeaponGenerator) { knife ->
                if (knife.damage < 0 || knife.weight <= 0) {
                    assertThrows<InvalidStatValueException> {
                        Knife(knife.name, knife.damage, knife.weight)
                    }
                } else {
                    assertDoesNotThrow {
                        Knife(knife.name, knife.damage, knife.weight)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Knife("", -1, -1)
            }
            assertDoesNotThrow {
                Knife("", 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            knife1.toString() shouldBe "Knife { name: 'TestKnife', damage: 10, weight: 20 }"
            knife3.toString() shouldBe "Knife { name: 'TestKnife2', damage: 20, weight: 10 }"
        }
        // Tests for equipTo... methods
        test("Be unequippable to an Engineer") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) { knife, engineer ->
                // The queue is not relevant to the test so a fresh instance is made each time
                val testEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, LinkedBlockingQueue<GameCharacter>())
                val testKnife = Knife(knife.name, knife.damage, knife.weight)
                assertThrows<InvalidWeaponException> {
                    testKnife.equipToEngineer(testEngineer)
                }
            }
        }
        test("Be equippable to a Knight") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) { knife, knight ->
                val testKnight = Knight(knight.name, knight.maxHp, knight.defense, LinkedBlockingQueue<GameCharacter>())
                val testKnife = Knife(knife.name, knife.damage, knife.weight)
                assertDoesNotThrow {
                    testKnife.equipToKnight(testKnight)
                }
            }
        }
        test("Be equippable to a Thief") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) { knife, thief ->
                val testThief = Thief(thief.name, thief.maxHp, thief.defense, LinkedBlockingQueue<GameCharacter>())
                val testKnife = Knife(knife.name, knife.damage, knife.weight)
                assertDoesNotThrow {
                    testKnife.equipToThief(testThief)
                }
            }
        }
        test("Be equippable to a BlackMage") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validMageGenerator
            ) { knife, blackMage ->
                val testBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, LinkedBlockingQueue<GameCharacter>())
                val testKnife = Knife(knife.name, knife.damage, knife.weight)
                assertDoesNotThrow {
                    testKnife.equipToBlackMage(testBlackMage)
                }
            }
        }
        test("Be unequippable to a WhiteMage") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validMageGenerator
            ) { knife, whiteMage ->
                val testWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, LinkedBlockingQueue<GameCharacter>())
                val testKnife = Knife(knife.name, knife.damage, knife.weight)
                assertThrows<InvalidWeaponException> {
                    testKnife.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
