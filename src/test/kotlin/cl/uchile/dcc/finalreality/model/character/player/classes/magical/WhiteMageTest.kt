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
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt()
            ) { name, maxHp, maxMp, defense ->
                val randomWhiteMage1 = WhiteMage(name, maxHp, maxMp, defense, queue)
                val randomWhiteMage2 = WhiteMage(name, maxHp, maxMp, defense, queue)
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
                val randomWhiteMage1 = WhiteMage(name1, maxHp1, maxMp1, defense1, queue)
                val randomWhiteMage2 = WhiteMage(name2, maxHp2, maxMp2, defense2, queue)
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt()
            ) { name, maxHp, maxMp, defense ->
                val randomWhiteMage1 = WhiteMage(name, maxHp, maxMp, defense, queue)
                randomWhiteMage1 shouldNotBe null
            }
            whiteMage1 shouldNotBe null
            whiteMage2 shouldNotBe null
            whiteMage3 shouldNotBe null
        }
        test("Not be able to wait its turn unarmed") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt()
            ) { name, maxHp, maxMp, defense ->
                val randomWhiteMage1 = WhiteMage(name, maxHp, maxMp, defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomWhiteMage1.waitTurn()
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
                val randomWhiteMage = WhiteMage(charName, maxHp, maxMp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomWhiteMage.equip(randomAxe)
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
                val randomWhiteMage = WhiteMage(charName, maxHp, maxMp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomWhiteMage.equip(randomBow)
                }
            }
        }
        test("Be unable to equip knives") {
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
                val randomWhiteMage = WhiteMage(charName, maxHp, maxMp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomWhiteMage.equip(randomKnife)
                }
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
                val randomWhiteMage = WhiteMage(charName, maxHp, maxMp, defense, queue)
                assertDoesNotThrow {
                    randomWhiteMage.equip(randomStaff)
                }
                randomWhiteMage.equippedWeapon shouldBe randomStaff
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
                val randomWhiteMage = WhiteMage(charName, maxHp, maxMp, defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomWhiteMage.equip(randomSword)
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
                val randomWhiteMage = WhiteMage(name, maxHp, maxMp, defense, queue)
                randomWhiteMage.currentHp shouldBe maxHp
                randomWhiteMage.currentHp = Integer.max(0, randomWhiteMage.currentHp - randomDamage)
                randomWhiteMage.currentHp shouldNotBe maxHp
                randomWhiteMage.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
        test("Be able to have its currentMp changed to non-negative values") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 55),
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.nonNegativeInt(),
                genE = Arb.positiveInt()
            ) { name, maxHp, maxMp, defense, randomCost ->
                assume(randomCost <= maxMp)
                val randomWhiteMage = WhiteMage(name, maxHp, maxMp, defense, queue)
                randomWhiteMage.currentMp shouldBe maxMp
                randomWhiteMage.currentMp = Integer.max(0, randomWhiteMage.currentMp - randomCost)
                randomWhiteMage.currentMp shouldNotBe maxMp
                randomWhiteMage.currentMp shouldBeGreaterThanOrEqualTo 0
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
                genG = Arb.positiveInt(),
                genH = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight, magicDamage ->
                val randomStaff = Staff(weapName, damage, weight, magicDamage)
                val randomWhiteMage = WhiteMage(charName, maxHp, maxMp, defense, queue)
                randomWhiteMage.equip(randomStaff)
                assertDoesNotThrow {
                    randomWhiteMage.waitTurn()
                }
            }
        }
    }
})
