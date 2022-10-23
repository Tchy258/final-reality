package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import io.kotest.core.spec.style.FunSpec
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

class StaffTest : FunSpec({
    lateinit var staff1: Staff
    lateinit var staff2: Staff
    lateinit var staff3: Staff

    beforeEach {
        staff1 = Staff("TestStaff", 10, 20, 5)
        staff2 = Staff("TestStaff", 10, 20, 5)
        staff3 = Staff("TestStaff2", 20, 10, 8)
    }
    context("Two staffs with the same parameters should:") {
        test("Be equal") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt()
            ) { name, damage, weight, magicDamage ->
                val randomStaff1 =
                    Staff(name, damage, weight, magicDamage)
                val randomStaff2 =
                    Staff(name, damage, weight, magicDamage)
                randomStaff1 shouldBe randomStaff2
            }
            staff1 shouldBe staff2
        }
        test("Have the same hashcode") {
            staff1.hashCode() shouldBe staff2.hashCode()
        }
    }
    context("Two staffs with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.string(),
                genF = Arb.nonNegativeInt(),
                genG = Arb.positiveInt(),
                genH = Arb.positiveInt()
            ) { name1, damage1, weight1, magicDamage1, name2, damage2, weight2, magicDamage2 ->
                assume {
                    name1 != name2 ||
                        damage1 != damage2 ||
                        weight1 != weight2 ||
                        magicDamage1 != magicDamage2
                }
                val randomStaff1 =
                    Staff(name1, damage1, weight1, magicDamage1)
                val randomStaff2 =
                    Staff(name2, damage2, weight2, magicDamage2)
                randomStaff1 shouldNotBe randomStaff2
            }
            staff1 shouldNotBe staff3
        }
    }
    context("Any Staff should:") {
        test("Not be null") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt()
            ) { name, damage, weight, magicDamage ->
                val randomStaff = Staff(name, damage, weight, magicDamage)
                randomStaff shouldNotBe null
            }
            staff1 shouldNotBe null
            staff2 shouldNotBe null
            staff3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.nonNegativeInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt()
            ) { name, damage, weight, magicDamage ->
                val randomStaff = Staff(name, damage, weight, magicDamage)
                randomStaff shouldBe randomStaff
            }
            staff1 shouldBe staff1
            staff2 shouldBe staff2
        }
        // Tests toString() method
        test("Have a string representation") {
            staff1.toString() shouldBe "Staff { name: 'TestStaff', damage: 10, weight: 20, magicDamage: 5 }"
            staff3.toString() shouldBe "Staff { name: 'TestStaff2', damage: 20, weight: 10, magicDamage: 8 }"
        }
        // Tests for equipTo... methods
        test("Be unequippable to an Engineer") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) {
                charName, maxHp, defense, weapName, damage, weight, magicDamage ->
                // The queue is not relevant to the test so a fresh instance is made each time
                val testEngineer = Engineer(charName, maxHp, defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(weapName, damage, weight, magicDamage)
                assertThrows<InvalidWeaponException> {
                    testStaff.equipToEngineer(testEngineer)
                }
            }
        }
        test("Be unequippable to a Knight") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight, magicDamage ->
                val testKnight = Knight(charName, maxHp, defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(weapName, damage, weight, magicDamage)
                assertThrows<InvalidWeaponException> {
                    testStaff.equipToKnight(testKnight)
                }
            }
        }
        test("Be unequippable to a Thief") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.string(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt(),
                genG = Arb.positiveInt()
            ) { charName, maxHp, defense, weapName, damage, weight, magicDamage ->
                val testThief = Thief(charName, maxHp, defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(weapName, damage, weight, magicDamage)
                assertThrows<InvalidWeaponException> {
                    testStaff.equipToThief(testThief)
                }
            }
        }
        test("Be equippable to a BlackMage") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.string(),
                genF = Arb.nonNegativeInt(),
                genG = Arb.positiveInt(),
                genH = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight, magicDamage ->
                val testBlackMage = BlackMage(charName, maxHp, maxMp, defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(weapName, damage, weight, magicDamage)
                assertDoesNotThrow {
                    testStaff.equipToBlackMage(testBlackMage)
                }
            }
        }
        test("Be equippable to a WhiteMage") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.string(),
                genF = Arb.nonNegativeInt(),
                genG = Arb.positiveInt(),
                genH = Arb.positiveInt()
            ) { charName, maxHp, maxMp, defense, weapName, damage, weight, magicDamage ->
                val testWhiteMage = WhiteMage(charName, maxHp, maxMp, defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(weapName, damage, weight, magicDamage)
                assertDoesNotThrow {
                    testStaff.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
