package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.validCharacterGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData.Companion.validMageGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.character.player.weapon.WeaponData.Companion.arbitraryWeaponGenerator
import cl.uchile.dcc.finalreality.model.character.player.weapon.WeaponData.Companion.validWeaponGenerator
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
            checkAll(validWeaponGenerator) { staff ->
                val randomStaff1 =
                    Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                val randomStaff2 =
                    Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
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
                genA = validWeaponGenerator,
                genB = validWeaponGenerator
            ) { staff1, staff2 ->
                assume {
                    staff1.name != staff2.name ||
                        staff1.damage != staff2.damage ||
                        staff1.weight != staff2.weight
                }
                val randomStaff1 = Staff(staff1.name, staff1.damage, staff1.weight, staff1.magicDamage)
                val randomStaff2 = Staff(staff2.name, staff2.damage, staff2.weight, staff2.magicDamage)
                randomStaff1 shouldNotBe randomStaff2
            }
            staff1 shouldNotBe staff3
        }
    }
    context("Any Staff should:") {
        test("Not be null") {
            checkAll(validWeaponGenerator) { staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                randomStaff shouldNotBe null
            }
            staff1 shouldNotBe null
            staff2 shouldNotBe null
            staff3 shouldNotBe null
        }
        test("Be equal to itself") {
            checkAll(validWeaponGenerator) { staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                randomStaff shouldBe randomStaff
            }
            staff1 shouldBe staff1
            staff2 shouldBe staff2
        }
        test("Have valid stats") {
            checkAll(arbitraryWeaponGenerator) { staff ->
                if (staff.damage < 0 || staff.weight <= 0 || staff.magicDamage <0) {
                    assertThrows<InvalidStatValueException> {
                        Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                    }
                } else {
                    assertDoesNotThrow {
                        Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Staff("", -1, -1, -1)
            }
            assertDoesNotThrow {
                Staff("", 1, 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            staff1.toString() shouldBe "Staff { name: 'TestStaff', damage: 10, weight: 20, magicDamage: 5 }"
            staff3.toString() shouldBe "Staff { name: 'TestStaff2', damage: 20, weight: 10, magicDamage: 8 }"
        }
        // Tests for equipTo... methods
        test("Be unequippable to an Engineer") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) {
                staff, engineer ->
                // The queue is not relevant to the test so a fresh instance is made each time
                val testEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                assertThrows<InvalidWeaponException> {
                    testStaff.equipToEngineer(testEngineer)
                }
            }
        }
        test("Be unequippable to a Knight") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) { staff, knight ->
                val testKnight = Knight(knight.name, knight.maxHp, knight.defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                assertThrows<InvalidWeaponException> {
                    testStaff.equipToKnight(testKnight)
                }
            }
        }
        test("Be unequippable to a Thief") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validCharacterGenerator
            ) { staff, thief ->
                val testThief = Thief(thief.name, thief.maxHp, thief.defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                assertThrows<InvalidWeaponException> {
                    testStaff.equipToThief(testThief)
                }
            }
        }
        test("Be equippable to a BlackMage") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validMageGenerator
            ) { staff, blackMage ->
                val testBlackMage = BlackMage(blackMage.name, blackMage.maxHp, blackMage.maxMp, blackMage.defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                assertDoesNotThrow {
                    testStaff.equipToBlackMage(testBlackMage)
                }
            }
        }
        test("Be equippable to a WhiteMage") {
            checkAll(
                genA = validWeaponGenerator,
                genB = validMageGenerator
            ) { staff, whiteMage ->
                val testWhiteMage = WhiteMage(whiteMage.name, whiteMage.maxHp, whiteMage.maxMp, whiteMage.defense, LinkedBlockingQueue<GameCharacter>())
                val testStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                assertDoesNotThrow {
                    testStaff.equipToWhiteMage(testWhiteMage)
                }
            }
        }
    }
})
