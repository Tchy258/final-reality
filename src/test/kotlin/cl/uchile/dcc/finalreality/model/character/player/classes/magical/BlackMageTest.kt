package cl.uchile.dcc.finalreality.model.character.player.classes.magical

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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt()
            ) { name, maxHp, maxMp, defense ->
                val randomBlackMage1 = BlackMage(name, maxHp, maxMp, defense, queue)
                val randomBlackMage2 = BlackMage(name, maxHp, maxMp, defense, queue)
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
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.string(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt(),
                genH = Arb.nonNegativeInt()
            ) { name1, maxHp1, maxMp1, defense1, name2, maxHp2, maxMp2, defense2 ->
                assume(
                    name1 != name2 ||
                        maxHp1 != maxHp2 ||
                        defense1 != defense2
                )
                val randomBlackMage1 = BlackMage(name1, maxHp1, maxMp1, defense1, queue)
                val randomBlackMage2 = BlackMage(name2, maxHp2, maxMp2, defense2, queue)
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt()
            ) { name, maxHp, maxMp, defense ->
                val randomBlackMage1 = BlackMage(name, maxHp, maxMp, defense, queue)
                randomBlackMage1 shouldNotBe null
            }
            blackMage1 shouldNotBe null
            blackMage2 shouldNotBe null
            blackMage3 shouldNotBe null
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt()
            ) { name, maxHp, maxMp, defense ->
                val randomBlackMage1 = BlackMage(name, maxHp, maxMp, defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomBlackMage1.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be unable to equip axes") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.string(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight ->
                val randomAxe = Axe(weapName, damage, weight)
                val randomBlackMage = BlackMage(charName, maxHp, maxMp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomBlackMage.equip(randomAxe)
                }
            }
        }
        test("Be unable to equip bows") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.string(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight ->
                val randomBow = Bow(weapName, damage, weight)
                val randomBlackMage = BlackMage(charName, maxHp, maxMp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomBlackMage.equip(randomBow)
                }
            }
        }
        test("Be able to equip knives") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.string(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight ->
                val randomKnife = Knife(weapName, damage, weight)
                val randomBlackMage = BlackMage(charName, maxHp, maxMp, defense, queue)
                assertDoesNotThrow {
                    randomBlackMage.equip(randomKnife)
                }
                randomBlackMage.equippedWeapon shouldBe randomKnife
            }
        }
        test("Be able to equip staves") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.string(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt(),
                genH = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight, magicDamage ->
                val randomStaff = Staff(weapName, damage, weight, magicDamage)
                val randomBlackMage = BlackMage(charName, maxHp, maxMp, defense, queue)
                assertDoesNotThrow {
                    randomBlackMage.equip(randomStaff)
                }
                randomBlackMage.equippedWeapon shouldBe randomStaff
            }
        }
        test("Be unable to equip swords") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.string(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight ->
                val randomSword = Sword(weapName, damage, weight)
                val randomBlackMage = BlackMage(charName, maxHp, maxMp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomBlackMage.equip(randomSword)
                }
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.positiveInt()
            ) { name, maxHp, maxMp, defense, randomDamage ->
                val randomBlackMage = BlackMage(name, maxHp, maxMp, defense, queue)
                randomBlackMage.currentHp shouldBe maxHp
                randomBlackMage.currentHp = Integer.max(0, randomBlackMage.currentHp - randomDamage)
                randomBlackMage.currentHp shouldNotBe maxHp
                randomBlackMage.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.string(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight ->
                val randomKnife = Knife(weapName, damage, weight)
                val randomBlackMage = BlackMage(charName, maxHp, maxMp, defense, queue)
                randomBlackMage.equip(randomKnife)
                assertDoesNotThrow {
                    randomBlackMage.waitTurn()
                }
            }
        }
    }
})
