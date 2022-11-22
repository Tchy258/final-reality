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

class KnifeTest : FunSpec({
    lateinit var testWeapon1: Knife
    lateinit var testWeapon2: Knife
    lateinit var testWeapon3: Knife
    lateinit var thisFactory: KnifeTestingFactory
    lateinit var thisData: Arb<WeaponData>
    lateinit var characterData: Arb<CharacterData>
    lateinit var mageData: Arb<MageData>
    lateinit var characterFactories: List<CharacterTestingFactory>

    beforeEach {
        testWeapon1 = Knife("TestKnife", 10, 20)
        testWeapon2 = Knife("TestKnife", 10, 20)
        testWeapon3 = Knife("TestKnife2", 20, 10)
        thisFactory = KnifeTestingFactory()
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
    context("Two knifes with the same parameters should:") {
        test("Be equal") {
            weaponEqualityCheck(thisFactory)
            testWeapon1 shouldBe testWeapon2
        }
        test("Have the same hashcode") {
            testWeapon1.hashCode() shouldBe testWeapon2.hashCode()
        }
    }
    context("Two knifes with different parameters should:") {
        test("Not be equal") {
            weaponInequalityCheck(thisFactory)
            testWeapon1 shouldNotBe testWeapon3
        }
    }
    context("Any Knife should:") {
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
                Knife("", -1, -1)
            }
            assertDoesNotThrow {
                Knife("", 1, 1)
            }
        }
        // Tests toString() method
        test("Have a string representation") {
            testWeapon1.toString() shouldBe "Knife { name: 'TestKnife', damage: 10, weight: 20 }"
            testWeapon3.toString() shouldBe "Knife { name: 'TestKnife2', damage: 20, weight: 10 }"
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
        test("Be equippable to a BlackMage") {
            validEquippableWeaponCheck(
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
