package cl.uchile.dcc.finalreality.model.character.player.classes.physical

import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.weapon.Axe
import cl.uchile.dcc.finalreality.model.character.player.weapon.Bow
import cl.uchile.dcc.finalreality.model.character.player.weapon.Knife
import cl.uchile.dcc.finalreality.model.character.player.weapon.Staff
import cl.uchile.dcc.finalreality.model.character.player.weapon.Sword
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomKnight1 = Knight(name, maxHp, defense, queue)
                val randomKnight2 = Knight(name, maxHp, defense, queue)
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
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.nonNegativeInt()
            ) { name1, maxHp1, defense1, name2, maxHp2, defense2 ->
                assume(
                    name1 != name2 ||
                        maxHp1 != maxHp2 ||
                        defense1 != defense2
                )
                val randomKnight1 = Knight(name1, maxHp1, defense1, queue)
                val randomKnight2 = Knight(name2, maxHp2, defense2, queue)
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomKnight1 = Knight(name, maxHp, defense, queue)
                randomKnight1 shouldNotBe null
            }
            knight1 shouldNotBe null
            knight2 shouldNotBe null
            knight3 shouldNotBe null
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomKnight1 = Knight(name, maxHp, defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomKnight1.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be able to equip axes") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomAxe = Axe(weapName, damage, weight)
                val randomKnight = Knight(charName, maxHp, defense, queue)
                assertDoesNotThrow {
                    randomKnight.equip(randomAxe)
                }
                randomKnight.equippedWeapon shouldBe randomAxe
            }
        }
        test("Be unable to equip bows") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomBow = Bow(weapName, damage, weight)
                val randomKnight = Knight(charName, maxHp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomKnight.equip(randomBow)
                }
            }
        }
        test("Be able to equip knives") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomKnife = Knife(weapName, damage, weight)
                val randomKnight = Knight(charName, maxHp, defense, queue)
                assertDoesNotThrow {
                    randomKnight.equip(randomKnife)
                }
                randomKnight.equippedWeapon shouldBe randomKnife
            }
        }
        test("Be unable to equip staves") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight, magicDamage ->
                val randomStaff = Staff(weapName, damage, weight, magicDamage)
                val randomKnight = Knight(charName, maxHp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomKnight.equip(randomStaff)
                }
            }
        }
        test("Be able to equip swords") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomSword = Sword(weapName, damage, weight)
                val randomKnight = Knight(charName, maxHp, defense, queue)
                assertDoesNotThrow {
                    randomKnight.equip(randomSword)
                }
                randomKnight.equippedWeapon shouldBe randomSword
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.positiveInt()
            ) { name, maxHp, defense, randomDamage ->
                val randomKnight = Knight(name, maxHp, defense, queue)
                randomKnight.currentHp shouldBe maxHp
                randomKnight.currentHp = Integer.max(0, randomKnight.currentHp - randomDamage)
                randomKnight.currentHp shouldNotBe maxHp
                randomKnight.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
    }
})
