package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.arbitraryCharacterGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.validCharacterGenerator
import cl.uchile.dcc.finalreality.model.character.player.weapon.Axe
import cl.uchile.dcc.finalreality.model.character.player.weapon.Bow
import cl.uchile.dcc.finalreality.model.character.player.weapon.Knife
import cl.uchile.dcc.finalreality.model.character.player.weapon.Staff
import cl.uchile.dcc.finalreality.model.character.player.weapon.Sword
import cl.uchile.dcc.finalreality.model.character.player.weapon.WeaponData.Companion.validWeaponGenerator
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

class BlackMageTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var blackMage1: BlackMage
    lateinit var blackMage2: BlackMage
    lateinit var blackMage3: BlackMage

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        blackMage1 = BlackMage("TestBlackMage", 15, 10, 10, queue)
        blackMage2 = BlackMage("TestBlackMage", 15, 10, 10, queue)
        blackMage3 = BlackMage("TestBlackMage2", 18, 11, 10, queue)
    }
    context("Two black mages with the same parameters should:") {
        test("Be equal") {
            checkAll(validCharacterGenerator) { blackMage1 ->
                val randomBlackMage1 = BlackMage(blackMage1.name, blackMage1.maxHp, blackMage1.maxMp, blackMage1.defense, queue)
                val randomBlackMage2 = BlackMage(blackMage1.name, blackMage1.maxHp, blackMage1.maxMp, blackMage1.defense, queue)
                randomBlackMage1 shouldBe randomBlackMage2
            }
            blackMage1 shouldBe blackMage2
        }
        test("Have the same hashcode") {
            blackMage1.hashCode() shouldBe blackMage2.hashCode()
        }
    }
    context("Two black mages with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validCharacterGenerator
            ) { blackMage1, blackMage2 ->
                assume(
                    blackMage1.name != blackMage2.name ||
                        blackMage1.maxHp != blackMage2.maxHp ||
                        blackMage1.maxMp != blackMage2.maxMp ||
                        blackMage1.defense != blackMage2.defense
                )
                val randomBlackMage1 = BlackMage(blackMage1.name, blackMage1.maxHp, blackMage1.maxMp, blackMage1.defense, queue)
                val randomBlackMage2 = BlackMage(blackMage2.name, blackMage2.maxHp, blackMage2.maxMp, blackMage2.defense, queue)
                randomBlackMage1 shouldNotBe randomBlackMage2
            }
            blackMage1 shouldNotBe blackMage3
        }
    }
    context("Any BlackMage should:") {
        test("Have a string representation") {
            blackMage1.toString() shouldBe "BlackMage { name: 'TestBlackMage', maxHp: 15, maxMp: 10, defense: 10, currentHp: 15, currentMp: 10 }"
            blackMage2.toString() shouldBe "BlackMage { name: 'TestBlackMage', maxHp: 15, maxMp: 10, defense: 10, currentHp: 15, currentMp: 10 }"
            blackMage3.toString() shouldBe "BlackMage { name: 'TestBlackMage2', maxHp: 18, maxMp: 11, defense: 10, currentHp: 18, currentMp: 11 }"
        }
        test("Not be null") {
            checkAll(validCharacterGenerator) { blackMage ->
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage shouldNotBe null
            }
            blackMage1 shouldNotBe null
            blackMage2 shouldNotBe null
            blackMage3 shouldNotBe null
        }
        test("Have valid stats") {
            checkAll(arbitraryCharacterGenerator) {
                blackMage ->
                if (blackMage.maxHp <= 0 || blackMage.maxMp <= 0 || blackMage.defense < 0) {
                    assertThrows<InvalidStatValueException> {
                        BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                    }
                } else {
                    assertDoesNotThrow {
                        BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                BlackMage("", -1, -1, -1, queue)
            }
            assertDoesNotThrow {
                BlackMage("", 1, 1, 1, queue)
            }
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(validCharacterGenerator) { blackMage ->
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomBlackMage.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be unable to equip axes") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { blackMage, axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomBlackMage.equip(randomAxe)
                }
            }
        }
        test("Be unable to equip bows") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { blackMage, bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomBlackMage.equip(randomBow)
                }
            }
        }
        test("Be able to equip knives") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { blackMage, knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                assertDoesNotThrow {
                    randomBlackMage.equip(randomKnife)
                }
                randomBlackMage.equippedWeapon shouldBe randomKnife
            }
        }
        test("Be able to equip staves") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { blackMage, staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                assertDoesNotThrow {
                    randomBlackMage.equip(randomStaff)
                }
                randomBlackMage.equippedWeapon shouldBe randomStaff
            }
        }
        test("Be unable to equip swords") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { blackMage, sword ->
                val randomSword = Sword(sword.name, sword.damage, sword.weight)
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomBlackMage.equip(randomSword)
                }
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = validCharacterGenerator,
                genB = Arb.positiveInt()
            ) { blackMage, randomDamage ->
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage.currentHp shouldBe blackMage.maxHp
                if (randomDamage> randomBlackMage.maxHp) {
                    assertThrows<InvalidStatValueException> {
                        randomBlackMage.currentHp -= randomDamage
                    }
                } else {
                    assertDoesNotThrow {
                        randomBlackMage.currentHp -= randomDamage
                    }
                    randomBlackMage.currentHp shouldNotBe blackMage.maxHp
                    randomBlackMage.currentHp shouldBeGreaterThanOrEqualTo 0
                }
            }
        }
        test("Be able to have its currentMp changed to non-negative values") {
            checkAll(
                genA = validCharacterGenerator,
                genB = Arb.positiveInt()
            ) { blackMage, randomCost ->
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage.currentMp shouldBe blackMage.maxMp
                if (randomCost> randomBlackMage.maxMp) {
                    assertThrows<InvalidStatValueException> {
                        randomBlackMage.currentMp -= randomCost
                    }
                } else {
                    assertDoesNotThrow {
                        randomBlackMage.currentMp -= randomCost
                    }
                    randomBlackMage.currentMp shouldNotBe blackMage.maxMp
                    randomBlackMage.currentMp shouldBeGreaterThanOrEqualTo 0
                }
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { blackMage, knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage.equip(randomKnife)
                assertDoesNotThrow {
                    randomBlackMage.waitTurn()
                }
            }
        }
    }
})
