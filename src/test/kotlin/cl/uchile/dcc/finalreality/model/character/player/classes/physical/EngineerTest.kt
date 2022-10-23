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
import java.lang.Integer.max
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomEngineer1 = Engineer(name, maxHp, defense, queue)
                val randomEngineer2 = Engineer(name, maxHp, defense, queue)
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
                val randomEngineer1 = Engineer(name1, maxHp1, defense1, queue)
                val randomEngineer2 = Engineer(name2, maxHp2, defense2, queue)
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomEngineer1 = Engineer(name, maxHp, defense, queue)
                randomEngineer1 shouldNotBe null
            }
            engineer1 shouldNotBe null
            engineer2 shouldNotBe null
            engineer3 shouldNotBe null
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomEngineer1 = Engineer(name, maxHp, defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomEngineer1.waitTurn()
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
                val randomEngineer = Engineer(charName, maxHp, defense, queue)
                assertDoesNotThrow {
                    randomEngineer.equip(randomAxe)
                }
                randomEngineer.equippedWeapon shouldBe randomAxe
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
                val randomEngineer = Engineer(charName, maxHp, defense, queue)
                assertDoesNotThrow {
                    randomEngineer.equip(randomBow)
                }
                randomEngineer.equippedWeapon shouldBe randomBow
            }
        }
        test("Be unable to equip knives") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomKnife = Knife(weapName, damage, weight)
                val randomEngineer = Engineer(charName, maxHp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomKnife)
                }
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
                val randomEngineer = Engineer(charName, maxHp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomStaff)
                }
            }
        }
        test("Be unable to equip swords") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomSword = Sword(weapName, damage, weight)
                val randomEngineer = Engineer(charName, maxHp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomSword)
                }
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.positiveInt()
            ) { name, maxHp, defense, randomDamage ->
                val randomEngineer = Engineer(name, maxHp, defense, queue)
                randomEngineer.currentHp shouldBe maxHp
                randomEngineer.currentHp = max(0, randomEngineer.currentHp - randomDamage)
                randomEngineer.currentHp shouldNotBe maxHp
                randomEngineer.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt(),
                genD = Arb.string(),
                genE = Arb.positiveInt(),
                genF = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight ->
                val randomAxe = Axe(weapName, damage, weight)
                val randomEngineer = Engineer(charName, maxHp, defense, queue)
                randomEngineer.equip(randomAxe)
                assertDoesNotThrow {
                    randomEngineer.waitTurn()
                }
            }
        }
    }
})
