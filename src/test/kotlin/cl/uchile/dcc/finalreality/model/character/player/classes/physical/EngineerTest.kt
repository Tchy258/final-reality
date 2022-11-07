package cl.uchile.dcc.finalreality.model.character.player.classes.physical

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.EnemyData.Companion.validEnemyGenerator
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.arbitraryCharacterGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.validCharacterGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData.Companion.validMageGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
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
import io.kotest.matchers.ints.shouldBeLessThan
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

class EngineerTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var engineer1: Engineer
    lateinit var engineer2: Engineer
    lateinit var engineer3: Engineer

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        engineer1 = Engineer("TestEngineer", 15, 10, queue)
        engineer2 = Engineer("TestEngineer", 15, 10, queue)
        engineer3 = Engineer("TestEngineer2", 18, 11, queue)
    }
    context("Two engineers with the same parameters should:") {
        test("Be equal") {
            checkAll(validCharacterGenerator) { engineer1 ->
                val randomEngineer1 = Engineer(engineer1.name, engineer1.maxHp, engineer1.defense, queue)
                val randomEngineer2 = Engineer(engineer1.name, engineer1.maxHp, engineer1.defense, queue)
                randomEngineer1 shouldBe randomEngineer2
            }
            engineer1 shouldBe engineer2
        }
        test("Have the same hashcode") {
            engineer1.hashCode() shouldBe engineer2.hashCode()
        }
    }
    context("Two engineers with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validCharacterGenerator
            ) { engineer1, engineer2 ->
                assume(
                    engineer1.name != engineer2.name ||
                        engineer1.maxHp != engineer2.maxHp ||
                        engineer1.defense != engineer2.defense
                )
                val randomEngineer1 = Engineer(engineer1.name, engineer1.maxHp, engineer1.defense, queue)
                val randomEngineer2 = Engineer(engineer2.name, engineer2.maxHp, engineer2.defense, queue)
                randomEngineer1 shouldNotBe randomEngineer2
            }
            engineer1 shouldNotBe engineer3
        }
    }
    context("Any Engineer should:") {
        test("Have a string representation") {
            engineer1.toString() shouldBe "Engineer { name: 'TestEngineer', maxHp: 15, defense: 10, currentHp: 15 }"
            engineer2.toString() shouldBe "Engineer { name: 'TestEngineer', maxHp: 15, defense: 10, currentHp: 15 }"
            engineer3.toString() shouldBe "Engineer { name: 'TestEngineer2', maxHp: 18, defense: 11, currentHp: 18 }"
        }
        test("Not be null") {
            checkAll(validCharacterGenerator) { engineer ->
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                randomEngineer shouldNotBe null
            }
            engineer1 shouldNotBe null
            engineer2 shouldNotBe null
            engineer3 shouldNotBe null
        }
        test("Not be equal to other characters even with the same parameters") {
            checkAll(validCharacterGenerator, Arb.positiveInt()) {
                characterData, maxMp ->

                val randomBlackMage = BlackMage(characterData.name, characterData.maxHp, maxMp, characterData.defense, queue)
                val randomEngineer = Engineer(characterData.name, characterData.maxHp, characterData.defense, queue)
                val randomThief = Thief(characterData.name, characterData.maxHp, characterData.defense, queue)
                val randomKnight = Knight(characterData.name, characterData.maxHp, characterData.defense, queue)
                val randomWhiteMage = WhiteMage(characterData.name, characterData.maxHp, maxMp, characterData.defense, queue)

                randomEngineer shouldNotBe randomWhiteMage
                randomEngineer shouldNotBe randomThief
                randomEngineer shouldNotBe randomKnight
                randomEngineer shouldNotBe randomBlackMage
            }
        }
        test("Have valid stats") {
            checkAll(arbitraryCharacterGenerator) {
                engineer ->
                if (engineer.maxHp <= 0 || engineer.defense < 0) {
                    assertThrows<InvalidStatValueException> {
                        Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                    }
                } else {
                    assertDoesNotThrow {
                        Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Engineer("", -1, -1, queue)
            }
            assertDoesNotThrow {
                Engineer("", 1, 1, queue)
            }
        }
        test("Not be able to wait its turn or attack unarmed") {
            checkAll(validCharacterGenerator) { engineer ->
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertThrows<NoWeaponEquippedException> {
                    randomEngineer.waitTurn()
                }
                assertThrows<NoWeaponEquippedException> {
                    randomEngineer.attack(engineer1)
                }
            }
        }
        // These tests use the 'equip<some weapon name>()' methods implicitly
        test("Be able to equip axes") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertDoesNotThrow {
                    randomEngineer.equip(randomAxe)
                }
                randomEngineer.equippedWeapon shouldBe randomAxe
            }
        }
        test("Be able to equip bows") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, bow ->
                val randomBow = Bow(bow.name, bow.damage, bow.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertDoesNotThrow {
                    randomEngineer.equip(randomBow)
                }
                randomEngineer.equippedWeapon shouldBe randomBow
            }
        }
        test("Be unable to equip knives") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, knife ->
                val randomKnife = Knife(knife.name, knife.damage, knife.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomKnife)
                }
            }
        }
        test("Be unable to equip staves") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validStaffGenerator
            ) { engineer, staff ->
                val randomStaff = Staff(staff.name, staff.damage, staff.weight, staff.magicDamage)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomStaff)
                }
            }
        }
        test("Be unable to equip swords") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, sword ->
                val randomSword = Sword(sword.name, sword.damage, sword.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                assertThrows<InvalidWeaponException> {
                    randomEngineer.equip(randomSword)
                }
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 55),
                genA = validCharacterGenerator,
                genB = Arb.positiveInt()
            ) { engineer, randomDamage ->
                assume {
                    randomDamage shouldBeGreaterThan engineer.defense
                }
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                randomEngineer.currentHp shouldBe engineer.maxHp
                assertDoesNotThrow {
                    randomEngineer.receiveAttack(randomDamage)
                }
                randomEngineer.currentHp shouldNotBe engineer.maxHp
                randomEngineer.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
        test("Not be able to have more current hp than its maxHp") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 55),
                genA = validCharacterGenerator,
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt()
            ) { engineer, randomHealing, randomDamage ->
                assume {
                    randomDamage shouldBeGreaterThan engineer.defense
                }
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                randomEngineer.currentHp shouldBe engineer.maxHp
                randomEngineer.receiveAttack(randomDamage)
                randomEngineer.currentHp shouldNotBe engineer.maxHp
                assertDoesNotThrow {
                    randomEngineer.receiveHealing(randomHealing)
                }
                randomEngineer.currentHp shouldBeLessThanOrEqual randomEngineer.maxHp
                randomEngineer.currentHp shouldBeGreaterThan 0
            }
        }
        test("Be able to join the turns queue with a weapon equipped") {
            checkAll(
                genA = validCharacterGenerator,
                genB = validWeaponGenerator
            ) { engineer, axe ->
                val randomAxe = Axe(axe.name, axe.damage, axe.weight)
                val randomEngineer = Engineer(engineer.name, engineer.maxHp, engineer.defense, queue)
                randomEngineer.equip(randomAxe)
                assertDoesNotThrow {
                    randomEngineer.waitTurn()
                }
            }
        }
        test("Be able to attack other characters") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 80),
                validEnemyGenerator,
                validCharacterGenerator,
                validCharacterGenerator,
                validMageGenerator,
                validWeaponGenerator
            ) { enemy, character1, character2, mage, weapon ->
                assume {
                    weapon.damage shouldBeGreaterThan mage.defense
                    weapon.damage shouldBeGreaterThan character2.defense
                    weapon.damage shouldBeGreaterThan enemy.defense
                }
                val randomEngineer = Engineer(character1.name, character1.maxHp, character1.defense, queue)
                val randomBow = Bow(weapon.name, weapon.damage, weapon.weight)

                randomEngineer.equip(randomBow)

                val randomCharacters: List<GameCharacter> = listOf(
                    Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue),
                    Engineer(character2.name, character2.maxHp, character2.defense, queue),
                    BlackMage(mage.name, mage.maxHp, mage.maxMp, mage.defense, queue),
                    Knight(character2.name, character2.maxHp, character2.defense, queue),
                    Thief(character2.name, character2.maxHp, character2.defense, queue),
                    WhiteMage(mage.name, mage.maxHp, mage.maxMp, mage.defense, queue)
                )
                for (gameCharacter in randomCharacters) {
                    gameCharacter.currentHp shouldBe gameCharacter.maxHp
                    randomEngineer.attack(gameCharacter)
                    gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
                }
            }

            engineer1.equip(Bow("TestBow", 11, 5))

            val randomCharacters: List<GameCharacter> = listOf(
                engineer2,
                BlackMage("TestMage", 10, 10, 0, queue),
                Enemy("TestEnemy", 10, 10, 15, 4, queue),
                Knight("TestCharacter", 30, 10, queue),
                Thief("TestCharacter", 15, 2, queue),
                WhiteMage("TestMage", 10, 10, 0, queue),
            )
            for (gameCharacter in randomCharacters) {
                gameCharacter.currentHp shouldBe gameCharacter.maxHp
                engineer1.attack(gameCharacter)
                gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
                gameCharacter.currentHp shouldBe Integer.max(
                    0,
                    gameCharacter.maxHp - (engineer1.equippedWeapon.damage - gameCharacter.defense)
                )
            }
        }
    }
})
