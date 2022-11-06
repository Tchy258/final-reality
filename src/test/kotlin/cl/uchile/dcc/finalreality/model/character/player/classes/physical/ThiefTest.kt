package cl.uchile.dcc.finalreality.model.character.player.classes.physical

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.validCharacterGenerator
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import cl.uchile.dcc.finalreality.model.weapon.WeaponData.Companion.validWeaponGenerator
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

class ThiefTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var thief1: Thief
    lateinit var thief2: Thief
    lateinit var thief3: Thief

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        thief1 = Thief("TestThief", 15, 10, queue)
        thief2 = Thief("TestThief", 15, 10, queue)
        thief3 = Thief("TestThief2", 18, 11, queue)
    }
    context("Two thieves with the same parameters should:") {
        test("Be equal") {
            checkAll(validCharacterGenerator) { thief1 ->
                val randomThief1 = Thief(thief1.name, thief1.maxHp, thief1.defense, queue)
                val randomThief2 = Thief(thief1.name, thief1.maxHp, thief1.defense, queue)
                randomThief1 shouldBe randomThief2
            }
            thief1 shouldBe thief2
        }
        test("Have the same hashcode") {
            thief1.hashCode() shouldBe thief2.hashCode()
        }
    }
    context("Two thieves with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validCharacterGenerator
            ) { thief1, thief2 ->
                assume(
                    thief1.name != thief2.name ||
                        thief1.maxHp != thief2.maxHp ||
                        thief1.defense != thief2.defense
                )
                val randomThief1 = Thief(thief1.name, thief1.maxHp, thief1.defense, queue)
                val randomThief2 = Thief(thief2.name, thief2.maxHp, thief2.defense, queue)
                randomThief1 shouldNotBe randomThief2
            }
            thief1 shouldNotBe thief3
        }
    }
    context("Any Thief should:") {
        test("Have a string representation") {
            thief1.toString() shouldBe "Thief { name: 'TestThief', maxHp: 15, defense: 10, currentHp: 15 }"
            thief2.toString() shouldBe "Thief { name: 'TestThief', maxHp: 15, defense: 10, currentHp: 15 }"
            thief3.toString() shouldBe "Thief { name: 'TestThief2', maxHp: 18, defense: 11, currentHp: 18 }"
        }
        test("Not be null") {
            checkAll(validCharacterGenerator) { thief ->
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                randomThief shouldNotBe null
            }
            thief1 shouldNotBe null
            thief2 shouldNotBe null
            thief3 shouldNotBe null
        }
        test("Have valid stats") {
            checkAll(CharacterData.arbitraryCharacterGenerator) {
                thief ->
                if (thief.maxHp <= 0 || thief.defense < 0) {
                    assertThrows<InvalidStatValueException> {
                        Thief(thief.name, thief.maxHp, thief.defense, queue)
                    }
                } else {
                    assertDoesNotThrow {
                        Thief(thief.name, thief.maxHp, thief.defense, queue)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Thief("", -1, -1, queue)
            }
            assertDoesNotThrow {
                Thief("", 1, 1, queue)
            }
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(validCharacterGenerator) { thief ->
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomThief.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be unable to equip axes") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { thief, axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomThief.equip(randomAxe)
                }
            }
        }
        test("Be able to equip bows") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { thief, bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                assertDoesNotThrow {
                    randomThief.equip(randomBow)
                }
                randomThief.equippedWeapon shouldBe randomBow
            }
        }
        test("Be able to equip knives") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { thief, knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                assertDoesNotThrow {
                    randomThief.equip(randomKnife)
                }
                randomThief.equippedWeapon shouldBe randomKnife
            }
        }
        test("Be unable to equip staves") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { thief, staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomThief.equip(randomStaff)
                }
            }
        }
        test("Be able to equip swords") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { thief, sword ->
                val randomSword = Sword(sword.name, sword.damage, sword.weight)
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                assertDoesNotThrow {
                    randomThief.equip(randomSword)
                }
                randomThief.equippedWeapon shouldBe randomSword
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = validCharacterGenerator,
                genB = Arb.positiveInt()
            ) { thief, randomDamage ->
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                randomThief.currentHp shouldBe thief.maxHp
                if (randomDamage> thief.maxHp) {
                    assertThrows<InvalidStatValueException> {
                        randomThief.currentHp -= randomDamage
                    }
                } else {
                    assertDoesNotThrow {
                        randomThief.currentHp -= randomDamage
                    }
                    randomThief.currentHp shouldNotBe thief.maxHp
                    randomThief.currentHp shouldBeGreaterThanOrEqualTo 0
                }
            }
        }
        test("Not be able to have more current hp than its maxHp") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 55),
                genA = validCharacterGenerator,
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt()
            ) { thief, randomHealing, randomDamage ->
                assume {
                    randomDamage shouldBeLessThanOrEqual thief.maxHp
                }
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                randomThief.currentHp shouldBe thief.maxHp
                randomThief.currentHp -= randomDamage
                randomThief.currentHp shouldNotBe thief.maxHp
                if (randomHealing > randomThief.maxHp - randomThief.currentHp) {
                    assertThrows<InvalidStatValueException> {
                        randomThief.currentHp += randomHealing
                    }
                } else {
                    assertDoesNotThrow {
                        randomThief.currentHp += randomHealing
                    }
                    randomThief.currentHp shouldBeLessThanOrEqual randomThief.maxHp
                    randomThief.currentHp shouldBeGreaterThan 0
                }
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { thief, bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                val randomThief = Thief(thief.name, thief.maxHp, thief.defense, queue)
                randomThief.equip(randomBow)
                assertDoesNotThrow {
                    randomThief.waitTurn()
                }
            }
        }
    }
})
