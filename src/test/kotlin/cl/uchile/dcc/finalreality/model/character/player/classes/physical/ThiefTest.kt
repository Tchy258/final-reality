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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomThief1 = Thief(name, maxHp, defense, queue)
                val randomThief2 = Thief(name, maxHp, defense, queue)
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
                val randomThief1 = Thief(name1, maxHp1, defense1, queue)
                val randomThief2 = Thief(name2, maxHp2, defense2, queue)
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomThief1 = Thief(name, maxHp, defense, queue)
                randomThief1 shouldNotBe null
            }
            thief1 shouldNotBe null
            thief2 shouldNotBe null
            thief3 shouldNotBe null
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomThief1 = Thief(name, maxHp, defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomThief1.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be unable to equip axes") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomAxe = Axe(weapName, damage, weight)
                val randomThief = Thief(charName, maxHp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomThief.equip(randomAxe)
                }
            }
        }
        test("Be able to equip bows") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomBow = Bow(weapName, damage, weight)
                val randomThief = Thief(charName, maxHp, defense, queue)
                assertDoesNotThrow {
                    randomThief.equip(randomBow)
                }
                randomThief.equippedWeapon shouldBe randomBow
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
                val randomThief = Thief(charName, maxHp, defense, queue)
                assertDoesNotThrow {
                    randomThief.equip(randomKnife)
                }
                randomThief.equippedWeapon shouldBe randomKnife
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
                val randomThief = Thief(charName, maxHp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomThief.equip(randomStaff)
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
                val randomThief = Thief(charName, maxHp, defense, queue)
                assertDoesNotThrow {
                    randomThief.equip(randomSword)
                }
                randomThief.equippedWeapon shouldBe randomSword
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.positiveInt()
            ) { name, maxHp, defense, randomDamage ->
                val randomThief = Thief(name, maxHp, defense, queue)
                randomThief.currentHp shouldBe maxHp
                randomThief.currentHp = Integer.max(0, randomThief.currentHp - randomDamage)
                randomThief.currentHp shouldNotBe maxHp
                randomThief.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
    }
})
