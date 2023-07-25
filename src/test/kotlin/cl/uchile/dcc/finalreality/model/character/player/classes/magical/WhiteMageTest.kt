package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
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
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Cure
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Paralysis
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Poison
import cl.uchile.dcc.finalreality.model.mpDecreaseCheck
import cl.uchile.dcc.finalreality.model.mpIncreaseCheck
import cl.uchile.dcc.finalreality.model.noActiveSpellCheck
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
import cl.uchile.dcc.finalreality.model.whiteMageUnusableSpellCastCheck
import cl.uchile.dcc.finalreality.model.whiteMagicCastTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

class WhiteMageTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var whiteMage1: WhiteMage
    lateinit var whiteMage2: WhiteMage
    lateinit var whiteMage3: WhiteMage
    lateinit var thisFactory: WhiteMageTestingFactory
    lateinit var thisData: Arb<MageData>
    lateinit var weaponFactories: List<WeaponTestingFactory>
    lateinit var weaponData: Arb<NonMagicalWeaponData>
    lateinit var staffData: Arb<StaffData>

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        thisFactory = WhiteMageTestingFactory(queue)
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
        whiteMage1 = WhiteMage("TestWhiteMage", 15, 10, 10, queue)
        whiteMage2 = WhiteMage("TestWhiteMage", 15, 10, 10, queue)
        whiteMage3 = WhiteMage("TestWhiteMage2", 18, 11, 10, queue)
    }
    context("Two white mages with the same parameters should:") {
        test("Be equal") {
            equalityCheck(thisData, thisFactory)
        }
        test("Have the same hashcode") {
            whiteMage1.hashCode() shouldBe whiteMage2.hashCode()
        }
    }
    context("Two white mages with different parameters should:") {
        test("Not be equal") {
            mageInequalityCheck(thisFactory)
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
            notNullCheck(thisData, thisFactory)
            whiteMage1 shouldNotBe null
            whiteMage2 shouldNotBe null
            whiteMage3 shouldNotBe null
        }
        test("Be equal to itself") {
            selfEqualityCheck(thisData, thisFactory)
        }
        test("Not be equal to other characters even with the same parameters") {
            differentMageInequalityCheck(thisFactory)
        }
        test("Have valid stats") {
            mageValidStatCheck(thisFactory)
            assertThrows<InvalidStatValueException> {
                WhiteMage("", -1, -1, -1, queue)
            }
            assertDoesNotThrow {
                WhiteMage("", 1, 1, 1, queue)
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
        test("Be unable to equip knives") {
            invalidEquippableWeaponCheck(
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
                staffData,
                thisFactory,
                weaponFactories[3]
            )
        }
        test("Be able to attack other characters") {
            characterAttackTest(
                thisData,
                staffData,
                thisFactory,
                weaponFactories[3]
            )

            whiteMage1.equip(Staff("TestStaff", 11, 5, 10))

            val randomCharacters: List<GameCharacter> = listOf(
                whiteMage2,
                Engineer("TestCharacter", 20, 5, queue),
                Enemy("TestEnemy", 10, 10, 15, 4, queue),
                Knight("TestCharacter", 30, 10, queue),
                Thief("TestCharacter", 15, 2, queue),
                BlackMage("TestMage", 10, 10, 0, queue),
            )
            for (gameCharacter in randomCharacters) {
                gameCharacter.currentHp shouldBe gameCharacter.maxHp
                whiteMage1.attack(gameCharacter)
                gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
                gameCharacter.currentHp shouldBe Integer.max(
                    0,
                    gameCharacter.maxHp - (whiteMage1.equippedWeapon.damage - gameCharacter.defense)
                )
            }
        }
        test("Be able to show its equipped weapon's magic damage") {
            checkAll(thisData, staffData) {
                mage, staff ->
                val randomWhiteMage = mage.process(thisFactory)
                val randomStaff = staff.process(StaffTestingFactory())
                randomWhiteMage.equip(randomStaff)
                randomWhiteMage.getMagicDamage() shouldBe randomStaff.magicDamage
            }
        }
        test("Be able to show a list of its magic spells") {
            checkAll(
                thisData,
                staffData
            ) { mage, staff ->
                val randomWhiteMage = mage.process(thisFactory)
                val randomStaff = staff.process(StaffTestingFactory())
                randomWhiteMage.equip(randomStaff)
                val expected = listOf(
                    Cure(),
                    Paralysis(),
                    Poison(randomWhiteMage.getMagicDamage())
                )
                randomWhiteMage.getSpells() shouldBe expected
            }
        }
        test("Be able to cast white magic spells") {
            whiteMagicCastTest(queue)
        }
        test("Be unable to cast spells with insufficient mp") {
            insufficientMpSpellCastCheck({ _: Int -> Cure() }, thisData, thisFactory)
            insufficientMpSpellCastCheck({ value: Int -> Poison(value) }, thisData, thisFactory)
            insufficientMpSpellCastCheck({ _: Int -> Paralysis() }, thisData, thisFactory)
        }
        test("Be unable to use magic without setting an active spell first") {
            noActiveSpellCheck(thisFactory)
        }
        test("Be unable to cast black magic spells") {
            whiteMageUnusableSpellCastCheck(queue)
        }
    }
})
