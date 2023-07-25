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

class AxeTest : FunSpec({
    lateinit var testWeapon1: Axe
    lateinit var testWeapon2: Axe
    lateinit var testWeapon3: Axe
    lateinit var thisFactory: AxeTestingFactory
    lateinit var thisData: Arb<NonMagicalWeaponData>
    lateinit var characterData: Arb<CharacterData>
    lateinit var mageData: Arb<MageData>
    lateinit var characterFactories: List<CharacterTestingFactory>

    beforeEach {
        testWeapon1 = Axe("TestAxe", 10, 20)
        testWeapon2 = Axe("TestAxe", 10, 20)
        testWeapon3 = Axe("TestAxe2", 20, 10)
        thisFactory = AxeTestingFactory()
        val queue: LinkedBlockingQueue<GameCharacter> = LinkedBlockingQueue()
        thisData = NonMagicalWeaponData.validGenerator
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
    context("Two axes with the same parameters should:") {
        test("Be equal") {
            weaponEqualityCheck(thisData, thisFactory)
            testWeapon1 shouldBe testWeapon2
        }
        test("Have the same hashcode") {
            testWeapon1.hashCode() shouldBe testWeapon2.hashCode()
        }
    }
    context("Two axes with different parameters should:") {
        test("Not be equal") {
            weaponInequalityCheck(thisFactory)
            testWeapon1 shouldNotBe testWeapon3
        }
    }
    context("Any Axe should:") {
        test("Not be null") {
            weaponNotNullCheck(thisData, thisFactory)
        }
        test("Be equal to itself") {
            weaponSelfEqualityCheck(thisData, thisFactory)
            testWeapon1 shouldBe testWeapon1
            testWeapon2 shouldBe testWeapon2
        }
        test("Not be equal to other weapons even with same parameters") {
            differentWeaponInequalityCheck(thisFactory)
        }
        test("Have valid stats") {
            weaponValidStatsCheck(thisFactory)
            assertThrows<InvalidStatValueException> {
                Axe("", -1, -1)
            }
            assertDoesNotThrow {
                Axe("", 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            testWeapon1.toString() shouldBe "Axe { name: 'TestAxe', damage: 10, weight: 20 }"
            testWeapon3.toString() shouldBe "Axe { name: 'TestAxe2', damage: 20, weight: 10 }"
        }
        // Tests for equipTo... methods
        test("Be equippable to an Engineer") {
            validEquippableWeaponCheck(
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
        test("Be unequippable to a Thief") {
            invalidEquippableWeaponCheck(
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
