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

class WhiteMageTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var whiteMage1: WhiteMage
    lateinit var whiteMage2: WhiteMage
    lateinit var whiteMage3: WhiteMage

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        whiteMage1 = WhiteMage("TestWhiteMage", 15, 10, 10, queue)
        whiteMage2 = WhiteMage("TestWhiteMage", 15, 10, 10, queue)
        whiteMage3 = WhiteMage("TestWhiteMage2", 18, 11, 10, queue)
    }
    context("Two black mages with the same parameters should:") {
        test("Be equal") {
            checkAll(validCharacterGenerator) { whiteMage1 ->
                val randomWhiteMage1 = WhiteMage(whiteMage1.name, whiteMage1.maxHp, whiteMage1.maxMp, whiteMage1.defense, queue)
                val randomWhiteMage2 = WhiteMage(whiteMage1.name, whiteMage1.maxHp, whiteMage1.maxMp, whiteMage1.defense, queue)
                randomWhiteMage1 shouldBe randomWhiteMage2
            }
            whiteMage1 shouldBe whiteMage2
        }
        test("Have the same hashcode") {
            whiteMage1.hashCode() shouldBe whiteMage2.hashCode()
        }
    }
    context("Two black mages with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validCharacterGenerator
            ) { whiteMage1, whiteMage2 ->
                assume(
                    whiteMage1.name != whiteMage2.name ||
                        whiteMage1.maxHp != whiteMage2.maxHp ||
                        whiteMage1.maxMp != whiteMage2.maxMp ||
                        whiteMage1.defense != whiteMage2.defense
                )
                val randomWhiteMage1 = WhiteMage(whiteMage1.name, whiteMage1.maxHp, whiteMage1.maxMp, whiteMage1.defense, queue)
                val randomWhiteMage2 = WhiteMage(whiteMage2.name, whiteMage2.maxHp, whiteMage2.maxMp, whiteMage2.defense, queue)
                randomWhiteMage1 shouldNotBe randomWhiteMage2
            }
            whiteMage1 shouldNotBe whiteMage3
        }
    }
    context("Any WhiteMage should:") {
        test("Have a string representation") {
            whiteMage1.toString() shouldBe "WhiteMage { name: 'TestWhiteMage', maxHp: 15, maxMp: 10, defense: 10, currentHp: 15, currentMp: 10 }"
            whiteMage2.toString() shouldBe "WhiteMage { name: 'TestWhiteMage', maxHp: 15, maxMp: 10, defense: 10, currentHp: 15, currentMp: 10 }"
            whiteMage3.toString() shouldBe "WhiteMage { name: 'TestWhiteMage2', maxHp: 18, maxMp: 11, defense: 10, currentHp: 18, currentMp: 11 }"
        }
        test("Not be null") {
            checkAll(validCharacterGenerator) { whiteMage ->
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                randomWhiteMage shouldNotBe null
            }
            whiteMage1 shouldNotBe null
            whiteMage2 shouldNotBe null
            whiteMage3 shouldNotBe null
        }
        test("Have valid stats") {
            checkAll(arbitraryCharacterGenerator) {
                whiteMage ->
                if (whiteMage.maxHp <= 0 || whiteMage.maxMp <= 0 || whiteMage.defense < 0) {
                    assertThrows<InvalidStatValueException> {
                        WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                    }
                } else {
                    assertDoesNotThrow {
                        WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                WhiteMage("", -1, -1, -1, queue)
            }
            assertDoesNotThrow {
                WhiteMage("", 1, 1, 1, queue)
            }
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(validCharacterGenerator) { whiteMage ->
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomWhiteMage.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be unable to equip axes") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { whiteMage, axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomWhiteMage.equip(randomAxe)
                }
            }
        }
        test("Be unable to equip bows") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { whiteMage, bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomWhiteMage.equip(randomBow)
                }
            }
        }
        test("Be unable to equip knives") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { whiteMage, knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomWhiteMage.equip(randomKnife)
                }
            }
        }
        test("Be able to equip staves") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { whiteMage, staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                assertDoesNotThrow {
                    randomWhiteMage.equip(randomStaff)
                }
                randomWhiteMage.equippedWeapon shouldBe randomStaff
            }
        }
        test("Be unable to equip swords") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { whiteMage, sword ->
                val randomSword = Sword(sword.name, sword.damage, sword.weight)
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomWhiteMage.equip(randomSword)
                }
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = validCharacterGenerator,
                genB = Arb.positiveInt()
            ) { whiteMage, randomDamage ->
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                randomWhiteMage.currentHp shouldBe whiteMage.maxHp
                randomWhiteMage.currentHp = Integer.max(0, randomWhiteMage.currentHp - randomDamage)
                randomWhiteMage.currentHp shouldNotBe whiteMage.maxHp
                randomWhiteMage.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
        test("Be able to have its currentMp changed to non-negative values") {
            checkAll(
                genA = validCharacterGenerator,
                genB = Arb.positiveInt()
            ) { whiteMage, randomCost ->
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                randomWhiteMage.currentMp shouldBe whiteMage.maxMp
                randomWhiteMage.currentMp = Integer.max(0, randomWhiteMage.currentMp - randomCost)
                randomWhiteMage.currentMp shouldNotBe whiteMage.maxMp
                randomWhiteMage.currentMp shouldBeGreaterThanOrEqualTo 0
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { whiteMage, staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                val randomWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, queue)
                randomWhiteMage.equip(randomStaff)
                assertDoesNotThrow {
                    randomWhiteMage.waitTurn()
                }
            }
        }
    }
})
