package cl.uchile.dcc.finalreality.model.character.player.classes.physical

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

class EngineerTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var engineer1: Engineer
    lateinit var engineer2: Engineer
    lateinit var engineer3: Engineer

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        engineer1 = Engineer("TestEngineer", 15, 10, queue)
        engineer2 = Engineer("TestEngineer", 15, 10, queue)
        engineer3 = Engineer("TestEngineer2", 18, 11, queue)
    }
    context("Two engineers with the same parameters should:") {
        test("Be equal") {
            checkAll(validCharacterGenerator) { engineer1 ->
                val randomEngineer1 = Engineer(engineer1.name, engineer1.maxHp, engineer1.defense, queue)
                val randomEngineer2 = Engineer(engineer1.name, engineer1.maxHp, engineer1.defense, queue)
                randomEngineer1 shouldBe randomEngineer2
            }
            engineer1 shouldBe engineer2
        }
        test("Have the same hashcode") {
            engineer1.hashCode() shouldBe engineer2.hashCode()
        }
    }
    context("Two engineers with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validCharacterGenerator
            ) { engineer1, engineer2 ->
                assume(
                    engineer1.name != engineer2.name ||
                        engineer1.maxHp != engineer2.maxHp ||
                        engineer1.maxMp != engineer2.maxMp ||
                        engineer1.defense != engineer2.defense
                )
                val randomEngineer1 = Engineer(engineer1.name, engineer1.maxHp, engineer1.defense, queue)
                val randomEngineer2 = Engineer(engineer2.name, engineer2.maxHp, engineer2.defense, queue)
                randomEngineer1 shouldNotBe randomEngineer2
            }
            engineer1 shouldNotBe engineer3
        }
    }
    context("Any Engineer should:") {
        test("Have a string representation") {
            engineer1.toString() shouldBe "Engineer { name: 'TestEngineer', maxHp: 15, defense: 10, currentHp: 15 }"
            engineer2.toString() shouldBe "Engineer { name: 'TestEngineer', maxHp: 15, defense: 10, currentHp: 15 }"
            engineer3.toString() shouldBe "Engineer { name: 'TestEngineer2', maxHp: 18, defense: 11, currentHp: 18 }"
        }
        test("Not be null") {
            checkAll(validCharacterGenerator) { engineer ->
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                randomEngineer shouldNotBe null
            }
            engineer1 shouldNotBe null
            engineer2 shouldNotBe null
            engineer3 shouldNotBe null
        }
        test("Have valid stats") {
            checkAll(arbitraryCharacterGenerator) {
                engineer ->
                if (engineer.maxHp <= 0 || engineer.defense < 0) {
                    assertThrows<InvalidStatValueException> {
                        Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                    }
                } else {
                    assertDoesNotThrow {
                        Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Engineer("", -1, -1, queue)
            }
            assertDoesNotThrow {
                Engineer("", 1, 1, queue)
            }
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(validCharacterGenerator) { engineer ->
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomEngineer.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be able to equip axes") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertDoesNotThrow {
                    randomEngineer.equip(randomAxe)
                }
                randomEngineer.equippedWeapon shouldBe randomAxe
            }
        }
        test("Be able to equip bows") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertDoesNotThrow {
                    randomEngineer.equip(randomBow)
                }
                randomEngineer.equippedWeapon shouldBe randomBow
            }
        }
        test("Be unable to equip knives") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomKnife)
                }
            }
        }
        test("Be unable to equip staves") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomStaff)
                }
            }
        }
        test("Be unable to equip swords") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, sword ->
                val randomSword = Sword(sword.name, sword.damage, sword.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomSword)
                }
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = validCharacterGenerator,
                genB = Arb.positiveInt()
            ) { engineer, randomDamage ->
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                randomEngineer.currentHp shouldBe engineer.maxHp
                if (randomDamage> engineer.maxHp) {
                    assertThrows<InvalidStatValueException> {
                        randomEngineer.currentHp -= randomDamage
                    }
                } else {
                    assertDoesNotThrow {
                        randomEngineer.currentHp -= randomDamage
                    }
                    randomEngineer.currentHp shouldNotBe engineer.maxHp
                    randomEngineer.currentHp shouldBeGreaterThanOrEqualTo 0
                }
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                randomEngineer.equip(randomAxe)
                assertDoesNotThrow {
                    randomEngineer.waitTurn()
                }
            }
        }
    }
})
