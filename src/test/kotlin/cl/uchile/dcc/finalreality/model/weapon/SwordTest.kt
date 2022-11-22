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
import cl.uchile.dcc.finalreality.model.validEquippableWeaponCheck
import cl.uchile.dcc.finalreality.model.weaponEqualityCheck
import cl.uchile.dcc.finalreality.model.weaponInequalityCheck
import cl.uchile.dcc.finalreality.model.weaponNotNullCheck
import cl.uchile.dcc.finalreality.model.weaponSelfEqualityCheck
import cl.uchile.dcc.finalreality.model.weaponValidStatsCheck
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

class SwordTest : FunSpec({
    lateinit var testWeapon1: Sword
    lateinit var testWeapon2: Sword
    lateinit var testWeapon3: Sword
    lateinit var thisFactory: SwordTestingFactory
    lateinit var thisData: Arb<WeaponData>
    lateinit var characterData: Arb<CharacterData>
    lateinit var mageData: Arb<MageData>
    lateinit var characterFactories: List<CharacterTestingFactory>

    beforeEach {
        testWeapon1 = Sword("TestSword", 10, 20)
        testWeapon2 = Sword("TestSword", 10, 20)
        testWeapon3 = Sword("TestSword2", 20, 10)
        thisFactory = SwordTestingFactory()
        val queue: LinkedBlockingQueue<GameCharacter> = LinkedBlockingQueue()
        thisData = WeaponData.validGenerator
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
    context("Two swords with the same parameters should:") {
        test("Be equal") {
            weaponEqualityCheck(thisFactory)
        }
        test("Have the same hashcode") {
            testWeapon1.hashCode() shouldBe testWeapon2.hashCode()
        }
    }
    context("Two swords with different parameters should:") {
        test("Not be equal") {
            weaponInequalityCheck(thisFactory)
            testWeapon1 shouldNotBe testWeapon3
        }
    }
    context("Any Sword should:") {
        test("Not be null") {
            weaponNotNullCheck(thisFactory)
            testWeapon1 shouldNotBe null
            testWeapon2 shouldNotBe null
            testWeapon3 shouldNotBe null
        }
        test("Be equal to itself") {
            weaponSelfEqualityCheck(thisFactory)
            testWeapon1 shouldBe testWeapon1
            testWeapon2 shouldBe testWeapon2
        }
        test("Not be equal to other weapons even with same parameters") {
            differentWeaponInequalityCheck(thisFactory)
        }
        test("Have valid stats") {
            weaponValidStatsCheck(thisFactory)
            assertThrows<InvalidStatValueException> {
                Sword("", -1, -1)
            }
            assertDoesNotThrow {
                Sword("", 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            testWeapon1.toString() shouldBe "Sword { name: 'TestSword', damage: 10, weight: 20 }"
            testWeapon3.toString() shouldBe "Sword { name: 'TestSword2', damage: 20, weight: 10 }"
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
        test("Be equippable to a Knight") {
            validEquippableWeaponCheck(
                characterData,
                thisData,
                characterFactories[1],
                thisFactory
            )
        }
        test("Be equippable to a Thief") {
            validEquippableWeaponCheck(
                characterData,
                thisData,
                characterFactories[2],
                thisFactory
            )
        }
        test("Be unequippable to a BlackMage") {
            invalidEquippableWeaponCheck(
                mageData,
                thisData,
                characterFactories[3],
                thisFactory
            )
        }
        test("Be unequippable to a WhiteMage") {
            invalidEquippableWeaponCheck(
                mageData,
                thisData,
                characterFactories[4],
                thisFactory
            )
        }
    }
})
