package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.EngineerTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.KnightTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.ThiefTestingFactory
import cl.uchile.dcc.finalreality.model.differentWeaponInequalityCheck
import cl.uchile.dcc.finalreality.model.invalidEquippableWeaponCheck
import cl.uchile.dcc.finalreality.model.staffEqualityCheck
import cl.uchile.dcc.finalreality.model.staffInequalityCheck
import cl.uchile.dcc.finalreality.model.staffNotNullCheck
import cl.uchile.dcc.finalreality.model.staffSelfEqualityCheck
import cl.uchile.dcc.finalreality.model.staffValidStatsCheck
import cl.uchile.dcc.finalreality.model.validEquippableWeaponCheck
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

class StaffTest : FunSpec({
    lateinit var testWeapon1: Staff
    lateinit var testWeapon2: Staff
    lateinit var testWeapon3: Staff
    lateinit var thisFactory: StaffTestingFactory
    lateinit var thisData: Arb<StaffData>
    lateinit var characterData: Arb<CharacterData>
    lateinit var mageData: Arb<MageData>
    lateinit var characterFactories: List<CharacterTestingFactory>

    beforeEach {
        testWeapon1 = Staff("TestStaff", 10, 20, 5)
        testWeapon2 = Staff("TestStaff", 10, 20, 5)
        testWeapon3 = Staff("TestStaff2", 20, 10, 8)
        val queue: LinkedBlockingQueue<GameCharacter> = LinkedBlockingQueue()
        thisFactory = StaffTestingFactory()
        thisData = StaffData.validGenerator
        characterData = CharacterData.validGenerator
        mageData = MageData.validGenerator
        characterFactories = listOf(
            EngineerTestingFactory(queue),
            KnightTestingFactory(queue),
            ThiefTestingFactory(queue),
            BlackMageTestingFactory(queue),
            WhiteMageTestingFactory(queue)
        )
    }
    context("Two staffs with the same parameters should:") {
        test("Be equal") {
            staffEqualityCheck()
            testWeapon1 shouldBe testWeapon2
        }
        test("Have the same hashcode") {
            testWeapon1.hashCode() shouldBe testWeapon2.hashCode()
        }
    }
    context("Two staffs with different parameters should:") {
        test("Not be equal") {
            staffInequalityCheck()
            testWeapon1 shouldNotBe testWeapon3
        }
    }
    context("Any Staff should:") {
        test("Not be null") {
            staffNotNullCheck()
            testWeapon1 shouldNotBe null
            testWeapon2 shouldNotBe null
            testWeapon3 shouldNotBe null
        }
        test("Be equal to itself") {
            staffSelfEqualityCheck()
            testWeapon1 shouldBe testWeapon1
            testWeapon2 shouldBe testWeapon2
        }
        test("Not be equal to other weapons even with same parameters") {
            differentWeaponInequalityCheck(thisFactory)
        }
        test("Have valid stats") {
            staffValidStatsCheck()
            assertThrows<InvalidStatValueException> {
                Staff("", -1, -1, -1)
            }
            assertDoesNotThrow {
                Staff("", 1, 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            testWeapon1.toString() shouldBe "Staff { name: 'TestStaff', damage: 10, weight: 20, magicDamage: 5 }"
            testWeapon3.toString() shouldBe "Staff { name: 'TestStaff2', damage: 20, weight: 10, magicDamage: 8 }"
        }
        // Tests for equipTo... methods
        test("Be unequippable to an Engineer") {
            invalidEquippableWeaponCheck(
                characterData,
                thisData,
                characterFactories[0],
                thisFactory
            )
        }
        test("Be unequippable to a Knight") {
            invalidEquippableWeaponCheck(
                characterData,
                thisData,
                characterFactories[1],
                thisFactory
            )
        }
        test("Be unequippable to a Thief") {
            invalidEquippableWeaponCheck(
                characterData,
                thisData,
                characterFactories[2],
                thisFactory
            )
        }
        test("Be equippable to a BlackMage") {
            validEquippableWeaponCheck(
                mageData,
                thisData,
                characterFactories[3],
                thisFactory
            )
        }
        test("Be equippable to a WhiteMage") {
            validEquippableWeaponCheck(
                mageData,
                thisData,
                characterFactories[4],
                thisFactory
            )
        }
    }
})
