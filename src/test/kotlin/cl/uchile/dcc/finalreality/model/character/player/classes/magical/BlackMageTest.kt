package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData.Companion.arbitraryMageGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData.Companion.validMageGenerator
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.StaffData.Companion.validStaffGenerator
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
            checkAll(validMageGenerator) { blackMage1 ->
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
                genA = validMageGenerator,
                genB = validMageGenerator
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
            checkAll(validMageGenerator) { blackMage ->
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage shouldNotBe null
            }
            blackMage1 shouldNotBe null
            blackMage2 shouldNotBe null
            blackMage3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(validMageGenerator) { blackMage ->
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage shouldBe randomBlackMage
            }
            blackMage1 shouldBe blackMage1
            blackMage2 shouldBe blackMage2
            blackMage3 shouldBe blackMage3
        }
        test("Have valid stats") {
            checkAll(arbitraryMageGenerator) {
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
            checkAll(validMageGenerator) { blackMage ->
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomBlackMage.waitTurn()
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be unable to equip axes") {
            checkAll(
                genA = validMageGenerator,
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
                genA = validMageGenerator,
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
                genA = validMageGenerator,
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
                genA = validMageGenerator,
                genB = validStaffGenerator
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
                genA = validMageGenerator,
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
                PropTestConfig(maxDiscardPercentage = 55),
                genA = validMageGenerator,
                genB = Arb.positiveInt()
            ) { blackMage, randomDamage ->
                assume {
                    randomDamage shouldBeGreaterThan blackMage.defense
                }
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage.currentHp shouldBe blackMage.maxHp
                assertDoesNotThrow {
                    randomBlackMage.receiveAttack(randomDamage)
                }
                randomBlackMage.currentHp shouldNotBe blackMage.maxHp
                randomBlackMage.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
        test("Not be able to have more currentHp than its maxHp") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 55),
                genA = validMageGenerator,
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt()
            ) { blackMage, randomHealing, randomDamage ->
                assume {
                    randomDamage shouldBeGreaterThan blackMage.defense
                }
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage.currentHp shouldBe blackMage.maxHp
                randomBlackMage.receiveAttack(randomDamage)
                randomBlackMage.currentHp shouldNotBe blackMage.maxHp
                assertDoesNotThrow {
                    randomBlackMage.receiveHealing(randomHealing)
                }
                randomBlackMage.currentHp shouldBeLessThanOrEqual randomBlackMage.maxHp
                randomBlackMage.currentHp shouldBeGreaterThan 0
            }
        }
        test("Not be able to have more currentMp than its maxMp") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 55),
                genA = validMageGenerator,
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt()
            ) { blackMage, randomRestoration, randomCost ->
                assume {
                    randomCost shouldBeLessThanOrEqual blackMage.maxMp
                }
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage.currentMp shouldBe blackMage.maxMp
                randomBlackMage.useMp(randomCost)
                randomBlackMage.currentMp shouldNotBe blackMage.maxMp
                assertDoesNotThrow {
                    randomBlackMage.restoreMp(randomRestoration)
                }
                randomBlackMage.currentMp shouldBeLessThanOrEqual randomBlackMage.maxMp
                randomBlackMage.currentMp shouldBeGreaterThan 0
            }
        }
        test("Be able to have its currentMp changed to non-negative values") {
            checkAll(
                genA = validMageGenerator,
                genB = Arb.positiveInt()
            ) { blackMage, randomCost ->
                val randomBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, queue)
                randomBlackMage.currentMp shouldBe blackMage.maxMp
                if (randomBlackMage.useMp(randomCost)) {
                    randomBlackMage.currentMp shouldNotBe blackMage.maxMp
                    randomBlackMage.currentMp shouldBeGreaterThanOrEqualTo 0
                } else {
                    randomBlackMage.currentMp shouldBe blackMage.maxMp
                }
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = validMageGenerator,
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
