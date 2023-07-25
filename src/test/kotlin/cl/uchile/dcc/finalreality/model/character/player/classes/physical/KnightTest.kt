package cl.uchile.dcc.finalreality.model.character.player.classes.physical

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.characterAttackTest
import cl.uchile.dcc.finalreality.model.characterInequalityCheck
import cl.uchile.dcc.finalreality.model.characterQueueJoinCheck
import cl.uchile.dcc.finalreality.model.characterUnarmedActionCheck
import cl.uchile.dcc.finalreality.model.characterValidStatCheck
import cl.uchile.dcc.finalreality.model.differentCharacterInequalityCheck
import cl.uchile.dcc.finalreality.model.equalityCheck
import cl.uchile.dcc.finalreality.model.hpDecreaseCheck
import cl.uchile.dcc.finalreality.model.hpIncreaseCheck
import cl.uchile.dcc.finalreality.model.invalidEquippableWeaponCheck
import cl.uchile.dcc.finalreality.model.notNullCheck
import cl.uchile.dcc.finalreality.model.selfEqualityCheck
import cl.uchile.dcc.finalreality.model.validEquippableWeaponCheck
import cl.uchile.dcc.finalreality.model.weapon.AxeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.BowTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.KnifeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.NonMagicalWeaponData
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.StaffTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.Sword
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

class KnightTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var testCharacter1: Knight
    lateinit var testCharacter2: Knight
    lateinit var testCharacter3: Knight
    lateinit var thisFactory: KnightTestingFactory
    lateinit var thisData: Arb<CharacterData>
    lateinit var weaponFactories: List<WeaponTestingFactory>
    lateinit var weaponData: Arb<NonMagicalWeaponData>
    lateinit var staffData: Arb<StaffData>

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        thisFactory = KnightTestingFactory(queue)
        thisData = CharacterData.validGenerator
        weaponData = NonMagicalWeaponData.validGenerator
        staffData = StaffData.validGenerator
        weaponFactories = listOf(
            AxeTestingFactory(),
            BowTestingFactory(),
            KnifeTestingFactory(),
            StaffTestingFactory(),
            SwordTestingFactory()
        )
        testCharacter1 = Knight("TestKnight", 15, 10, queue)
        testCharacter2 = Knight("TestKnight", 15, 10, queue)
        testCharacter3 = Knight("TestKnight2", 18, 11, queue)
    }
    context("Two knights with the same parameters should:") {
        test("Be equal") {
            equalityCheck(
                thisData,
                thisFactory
            )
            testCharacter1 shouldBe testCharacter2
        }
        test("Have the same hashcode") {
            testCharacter1.hashCode() shouldBe testCharacter2.hashCode()
        }
    }
    context("Two knights with different parameters should:") {
        test("Not be equal") {
            characterInequalityCheck(thisFactory)
            testCharacter1 shouldNotBe testCharacter3
        }
    }
    context("Any Knight should:") {
        test("Have a string representation") {
            testCharacter1.toString() shouldBe "Knight { name: 'TestKnight', maxHp: 15, defense: 10, currentHp: 15 }"
            testCharacter2.toString() shouldBe "Knight { name: 'TestKnight', maxHp: 15, defense: 10, currentHp: 15 }"
            testCharacter3.toString() shouldBe "Knight { name: 'TestKnight2', maxHp: 18, defense: 11, currentHp: 18 }"
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
            differentCharacterInequalityCheck(
                thisFactory
            )
        }
        test("Have valid stats") {
            characterValidStatCheck(thisFactory)

            assertThrows<InvalidStatValueException> {
                Knight("", -1, -1, queue)
            }
            assertDoesNotThrow {
                Knight("", 1, 1, queue)
            }
        }
        test("Not be able to wait its turn or attack unarmed") {
            characterUnarmedActionCheck(thisFactory)
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be able to equip axes") {
            validEquippableWeaponCheck(
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
        test("Be unable to equip staves") {
            invalidEquippableWeaponCheck(
                thisData,
                staffData,
                thisFactory,
                weaponFactories[3]
            )
        }
        test("Be able to equip swords") {
            validEquippableWeaponCheck(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[4]
            )
        }
        test("Be able to have its currentHp changed to non-negative values") {
            hpDecreaseCheck(thisData, thisFactory)
        }
        test("Not be able to have more current hp than its maxHp") {
            hpIncreaseCheck(thisData, thisFactory)
        }
        test("Be able to join the turns queue with a weapon equipped") {
            characterQueueJoinCheck(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[0]
            )
        }
        test("Be able to attack other characters") {
            characterAttackTest(
                thisData,
                weaponData,
                thisFactory,
                weaponFactories[4]
            )

            testCharacter1.equip(Sword("TestSword", 14, 5))

            val randomCharacters: List<GameCharacter> = listOf(
                testCharacter2,
                Knight("TestCharacter", 20, 5, queue),
                Enemy("TestEnemy", 10, 10, 15, 4, queue),
                BlackMage("TestMage", 10, 10, 0, queue),
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
    }
})
