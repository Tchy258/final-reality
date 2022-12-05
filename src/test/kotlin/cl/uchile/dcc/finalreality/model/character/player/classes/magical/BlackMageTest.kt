package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.blackMageUnusableSpellCastCheck
import cl.uchile.dcc.finalreality.model.blackMagicNoStaffCastTest
import cl.uchile.dcc.finalreality.model.blackMagicStaffCastTest
import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.characterAttackTest
import cl.uchile.dcc.finalreality.model.characterQueueJoinCheck
import cl.uchile.dcc.finalreality.model.differentMageInequalityCheck
import cl.uchile.dcc.finalreality.model.equalityCheck
import cl.uchile.dcc.finalreality.model.hpDecreaseCheck
import cl.uchile.dcc.finalreality.model.hpIncreaseCheck
import cl.uchile.dcc.finalreality.model.insufficientMpSpellCastCheck
import cl.uchile.dcc.finalreality.model.invalidEquippableWeaponCheck
import cl.uchile.dcc.finalreality.model.mageInequalityCheck
import cl.uchile.dcc.finalreality.model.mageUnarmedActionCheck
import cl.uchile.dcc.finalreality.model.mageValidStatCheck
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder
import cl.uchile.dcc.finalreality.model.mpDecreaseCheck
import cl.uchile.dcc.finalreality.model.mpIncreaseCheck
import cl.uchile.dcc.finalreality.model.notNullCheck
import cl.uchile.dcc.finalreality.model.selfEqualityCheck
import cl.uchile.dcc.finalreality.model.validEquippableWeaponCheck
import cl.uchile.dcc.finalreality.model.weapon.AxeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.BowTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.KnifeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.NonMagicalWeaponData
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.StaffTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.SwordTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.WeaponTestingFactory
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

class BlackMageTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var testCharacter1: BlackMage
    lateinit var testCharacter2: BlackMage
    lateinit var testCharacter3: BlackMage
    lateinit var thisFactory: BlackMageTestingFactory
    lateinit var thisData: Arb<MageData>
    lateinit var weaponFactories: List<WeaponTestingFactory>
    lateinit var weaponData: Arb<NonMagicalWeaponData>
    lateinit var staffData: Arb<StaffData>

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        thisFactory = BlackMageTestingFactory(queue)
        thisData = MageData.validGenerator
        weaponData = NonMagicalWeaponData.validGenerator
        staffData = StaffData.validGenerator
        weaponFactories = listOf(
            AxeTestingFactory(),
            BowTestingFactory(),
            KnifeTestingFactory(),
            StaffTestingFactory(),
            SwordTestingFactory()
        )
        testCharacter1 = BlackMage("TestBlackMage", 15, 10, 10, queue)
        testCharacter2 = BlackMage("TestBlackMage", 15, 10, 10, queue)
        testCharacter3 = BlackMage("TestBlackMage2", 18, 11, 10, queue)
    }
    context("Two black mages with the same parameters should:") {
        test("Be equal") {
            equalityCheck(thisData, thisFactory)
            testCharacter1 shouldBe testCharacter2
        }
        test("Have the same hashcode") {
            testCharacter1.hashCode() shouldBe testCharacter2.hashCode()
        }
    }
    context("Two black mages with different parameters should:") {
        test("Not be equal") {
            mageInequalityCheck(thisFactory)
            testCharacter1 shouldNotBe testCharacter3
        }
    }
    context("Any BlackMage should:") {
        test("Have a string representation") {
            testCharacter1.toString() shouldBe "BlackMage { name: 'TestBlackMage', maxHp: 15, maxMp: 10, defense: 10, currentHp: 15, currentMp: 10 }"
            testCharacter2.toString() shouldBe "BlackMage { name: 'TestBlackMage', maxHp: 15, maxMp: 10, defense: 10, currentHp: 15, currentMp: 10 }"
            testCharacter3.toString() shouldBe "BlackMage { name: 'TestBlackMage2', maxHp: 18, maxMp: 11, defense: 10, currentHp: 18, currentMp: 11 }"
        }
        test("Not be null") {
            notNullCheck(thisData, thisFactory)
            testCharacter1 shouldNotBe null
            testCharacter2 shouldNotBe null
            testCharacter3 shouldNotBe null
        }
        test("Be equal to itself") {
            selfEqualityCheck(thisData, thisFactory)
            testCharacter1 shouldBe testCharacter1
            testCharacter2 shouldBe testCharacter2
            testCharacter3 shouldBe testCharacter3
        }
        test("Not be equal to other characters even with the same parameters") {
            differentMageInequalityCheck(
                thisFactory
            )
        }
        test("Have valid stats") {
            mageValidStatCheck(thisFactory)

            assertThrows<InvalidStatValueException> {
                BlackMage("", -1, -1, -1, queue)
            }
            assertDoesNotThrow {
                BlackMage("", 1, 1, 1, queue)
            }
        }
        test("Not be able to wait its turn, cast magic or attack unarmed") {
            mageUnarmedActionCheck(thisFactory)
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be unable to equip axes") {
            invalidEquippableWeaponCheck(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[0]
            )
        }
        test("Be unable to equip bows") {
            invalidEquippableWeaponCheck(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[1]
            )
        }
        test("Be able to equip knives") {
            validEquippableWeaponCheck(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[2]
            )
        }
        test("Be able to equip staves") {
            validEquippableWeaponCheck(
                thisData,
                staffData,
                thisFactory,
                weaponFactories[3]
            )
        }
        test("Be unable to equip swords") {
            invalidEquippableWeaponCheck(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[4]
            )
        }
        test("Be able to have its currentHp changed to non-negative values") {
            hpDecreaseCheck(thisData, thisFactory)
        }
        test("Not be able to have more currentHp than its maxHp") {
            hpIncreaseCheck(thisData, thisFactory)
        }
        test("Not be able to have more currentMp than its maxMp") {
            mpIncreaseCheck(thisFactory)
        }
        test("Be able to have its currentMp changed to non-negative values") {
            mpDecreaseCheck(thisFactory)
        }
        test("Be able to join the turns queue with a weapon equipped") {
            characterQueueJoinCheck(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[2]
            )
        }
        test("Be able to attack other characters") {
            characterAttackTest(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[2]
            )

            testCharacter1.equip(Staff("TestStaff", 11, 5, 10))

            val randomCharacters: List<GameCharacter> = listOf(
                testCharacter2,
                Engineer("TestCharacter", 20, 5, queue),
                Enemy("TestEnemy", 10, 10, 15, 4, queue),
                Knight("TestCharacter", 30, 10, queue),
                Thief("TestCharacter", 15, 2, queue),
                WhiteMage("TestMage", 10, 10, 0, queue),
            )
            for (gameCharacter in randomCharacters) {
                gameCharacter.currentHp shouldBe gameCharacter.maxHp
                testCharacter1.attack(gameCharacter)
                gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
                gameCharacter.currentHp shouldBe Integer.max(
                    0,
                    gameCharacter.maxHp - (testCharacter1.equippedWeapon.damage - gameCharacter.defense)
                )
            }
        }
        test("Be able to cast black magic spells") {
            blackMagicStaffCastTest(queue)
            blackMagicNoStaffCastTest(queue)
        }
        test("Be unable to cast spells with insufficient mp") {
            insufficientMpSpellCastCheck({ value: Int -> Thunder(value) }, thisData, thisFactory)
            insufficientMpSpellCastCheck({ value: Int -> Fire(value) }, thisData, thisFactory)
        }
        test("Be unable to cast white magic spells") {
            blackMageUnusableSpellCastCheck(queue)
        }
    }
})
