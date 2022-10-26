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

class KnightTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var knight1: Knight
    lateinit var knight2: Knight
    lateinit var knight3: Knight

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        knight1 = Knight("TestKnight", 15, 10, queue)
        knight2 = Knight("TestKnight", 15, 10, queue)
        knight3 = Knight("TestKnight2", 18, 11, queue)
    }
    context("Two knights with the same parameters should:") {
        test("Be equal") {
            checkAll(validCharacterGenerator) { knight1 ->
                val randomKnight1 = Knight(knight1.name, knight1.maxHp, knight1.defense, queue)
                val randomKnight2 = Knight(knight1.name, knight1.maxHp, knight1.defense, queue)
                randomKnight1 shouldBe randomKnight2
            }
            knight1 shouldBe knight2
        }
        test("Have the same hashcode") {
            knight1.hashCode() shouldBe knight2.hashCode()
        }
    }
    context("Two knights with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validCharacterGenerator
            ) { knight1, knight2 ->
                assume(
                    knight1.name != knight2.name ||
                        knight1.maxHp != knight2.maxHp ||
                        knight1.defense != knight2.defense
                )
                val randomKnight1 = Knight(knight1.name, knight1.maxHp, knight1.defense, queue)
                val randomKnight2 = Knight(knight2.name, knight2.maxHp, knight2.defense, queue)
                randomKnight1 shouldNotBe randomKnight2
            }
            knight1 shouldNotBe knight3
        }
    }
    context("Any Knight should:") {
        test("Have a string representation") {
            knight1.toString() shouldBe "Knight { name: 'TestKnight', maxHp: 15, defense: 10, currentHp: 15 }"
            knight2.toString() shouldBe "Knight { name: 'TestKnight', maxHp: 15, defense: 10, currentHp: 15 }"
            knight3.toString() shouldBe "Knight { name: 'TestKnight2', maxHp: 18, defense: 11, currentHp: 18 }"
        }
        test("Not be null") {
            checkAll(validCharacterGenerator) { knight ->
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                randomKnight shouldNotBe null
            }
            knight1 shouldNotBe null
            knight2 shouldNotBe null
            knight3 shouldNotBe null
        }
        test("Have valid stats") {
            checkAll(arbitraryCharacterGenerator) {
                knight ->
                if (knight.maxHp <= 0 || knight.defense < 0) {
                    assertThrows<InvalidStatValueException> {
                        Knight(knight.name, knight.maxHp, knight.defense, queue)
                    }
                } else {
                    assertDoesNotThrow {
                        Knight(knight.name, knight.maxHp, knight.defense, queue)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Knight("", -1, -1, queue)
            }
            assertDoesNotThrow {
                Knight("", 1, 1, queue)
            }
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(validCharacterGenerator) { knight ->
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomKnight.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be able to equip axes") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { knight, axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                assertDoesNotThrow {
                    randomKnight.equip(randomAxe)
                }
                randomKnight.equippedWeapon shouldBe randomAxe
            }
        }
        test("Be unable to equip bows") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { knight, bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomKnight.equip(randomBow)
                }
            }
        }
        test("Be able to equip knives") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { knight, knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                assertDoesNotThrow {
                    randomKnight.equip(randomKnife)
                }
                randomKnight.equippedWeapon shouldBe randomKnife
            }
        }
        test("Be unable to equip staves") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { knight, staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomKnight.equip(randomStaff)
                }
            }
        }
        test("Be able to equip swords") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { knight, sword ->
                val randomSword = Sword(sword.name, sword.damage, sword.weight)
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                assertDoesNotThrow {
                    randomKnight.equip(randomSword)
                }
                randomKnight.equippedWeapon shouldBe randomSword
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = validCharacterGenerator,
                genB = Arb.positiveInt()
            ) { knight, randomDamage ->
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                randomKnight.currentHp shouldBe knight.maxHp
                if (randomDamage> knight.maxHp) {
                    assertThrows<InvalidStatValueException> {
                        randomKnight.currentHp -= randomDamage
                    }
                } else {
                    assertDoesNotThrow {
                        randomKnight.currentHp -= randomDamage
                    }
                    randomKnight.currentHp shouldNotBe knight.maxHp
                    randomKnight.currentHp shouldBeGreaterThanOrEqualTo 0
                }
            }
        }
        test("Not be able to have more current hp than its maxHp") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 55),
                genA = validCharacterGenerator,
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt()
            ) {knight, randomHealing, randomDamage ->
                assume {
                    randomDamage shouldBeLessThanOrEqual knight.maxHp
                }
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                randomKnight.currentHp shouldBe knight.maxHp
                randomKnight.currentHp -= randomDamage
                randomKnight.currentHp shouldNotBe knight.maxHp
                if (randomHealing > randomKnight.maxHp - randomKnight.currentHp) {
                    assertThrows<InvalidStatValueException> {
                        randomKnight.currentHp += randomHealing
                    }
                }
                else {
                    assertDoesNotThrow {
                        randomKnight.currentHp += randomHealing
                    }
                    randomKnight.currentHp shouldBeLessThanOrEqual randomKnight.maxHp
                    randomKnight.currentHp shouldBeGreaterThan 0
                }
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { knight, sword ->
                val randomSword = Sword(sword.name, sword.damage, sword.weight)
                val randomKnight = Knight(knight.name, knight.maxHp, knight.defense, queue)
                randomKnight.equip(randomSword)
                assertDoesNotThrow {
                    randomKnight.waitTurn()
                }
            }
        }
    }
})
