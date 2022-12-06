package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.exceptions.IllegalStateActionException
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.assertThrows
import java.lang.Integer.min
import kotlin.math.ceil
import kotlin.random.Random

class GameControllerTest : FunSpec({
    lateinit var game: GameController
    ControllerRNGSeed.setSeed(6)
    val seedCopy = Random(6)
    // Advancing seed...
    val enemyAmount = seedCopy.nextInt(1, 6)
    val expectedEnemyNameList = listOf(
        "Goblin",
        "Zu",
        "Cockatrice",
        "Hooded Stabber",
        "Green Smelly",
        "Bomb",
        "Running Cactus",
        "Slayer",
        "Basilisk",
        "Zombie",
        "Mutant",
        "Were-wolf",
        "Vampire",
        "Ogre",
        "Red Panda"
    )
    context("A GameController should") {
        game = GameController()
        var character1Hp: Int
        var character2Hp: Int
        var character3Hp: Int
        var character4Hp: Int
        var character5Hp: Int
        val charactersHp = mutableListOf<Pair<Int, Int>>()
        val enemyHp = mutableListOf<Pair<Int, Int>>()
        test("Generate enemies when instantiated") {
            // The given seed should generate exactly these enemies
            val enemyNames = mutableListOf<String>()
            for (i in 1..enemyAmount) {
                enemyNames.add(expectedEnemyNameList[(seedCopy.nextInt(0, expectedEnemyNameList.size))])
                seedCopy.nextInt(60, 100)
                seedCopy.nextInt(15, 45)
                val hpVal = seedCopy.nextInt(300, 600)
                enemyHp.add(Pair(hpVal, hpVal))
                seedCopy.nextInt(5, 20)
            }
            game.getEnemyNames() shouldBe enemyNames
            game.gameState.isCharacterCreation() shouldBe true
        }
        test("Allow only 5 characters to be created and start a fight when all 5 are present") {
            game.startBattle() shouldBe false
            game.createEngineer("TestEngineer") shouldBe true
            game.startBattle() shouldBe false
            game.createKnight("TestKnight") shouldBe true
            game.startBattle() shouldBe false
            game.createThief("TestThief") shouldBe true
            game.startBattle() shouldBe false
            game.createWhiteMage("TestWhiteMage") shouldBe true
            game.startBattle() shouldBe false
            game.createBlackMage("TestBlackMage") shouldBe true
            game.startBattle() shouldBe true
            // Advancing the seed....
            character1Hp = seedCopy.nextInt(300, 500)
            seedCopy.nextInt(20, 50)
            character2Hp = seedCopy.nextInt(400, 600)
            seedCopy.nextInt(30, 60)
            character3Hp = seedCopy.nextInt(300, 400)
            seedCopy.nextInt(15, 45)
            character4Hp = seedCopy.nextInt(200, 400)
            seedCopy.nextInt(100, 200)
            seedCopy.nextInt(100, 300)
            character5Hp = seedCopy.nextInt(200, 400)
            seedCopy.nextInt(100, 200)
            seedCopy.nextInt(100, 300)
            charactersHp.addAll(
                listOf(
                    Pair(character1Hp, character1Hp),
                    Pair(character2Hp, character2Hp),
                    Pair(character3Hp, character3Hp),
                    Pair(character4Hp, character4Hp),
                    Pair(character5Hp, character5Hp)
                )
            )
        }
        test("Be able to return the characters and enemies Hp values") {
            game.getCharacterHp() shouldBe charactersHp
            game.getEnemyHp() shouldBe enemyHp
        }
        context("Be able to advance turns and") {
            test("Give turns to enemies") {
                game.getCurrentCharacter() shouldBe -1
                game.nextTurn()
                game.getCurrentCharacter() shouldBe -1
            }
            test("Enforce a game end check after a turn") {
                game.isGameOver() shouldBe false
            }
            test("Disallow checking the inventory if not on a player turn") {
                assertThrows<IllegalStateActionException> {
                    game.getInventory()
                }
            }
            test("Give a turn to a character") {
                var characterIndex = game.getCurrentCharacter()
                while (characterIndex == -1) {
                    game.nextTurn()
                    characterIndex = game.getCurrentCharacter()
                    if (characterIndex != -1) break
                    game.isGameOver()
                }
                characterIndex shouldNotBe -1
                characterIndex shouldBeGreaterThanOrEqual 0
                characterIndex shouldBeLessThan 5
                // With this seed, this character is always the WhiteMage
            }
            test("Allow said character to check the inventory") {
                val expected = listOf(
                    Triple("BasicAxe", 60, 40),
                    Triple("BasicWand (Currently equipped)", 8, 21)
                )
                game.getInventory() shouldBe expected
            }
            test("Allow said character to equip a valid weapon") {
                game.equipWeapon(0) shouldBe false
                game.equipWeapon(1) shouldBe true
            }
            test("Allow this character to see its spells") {
                val expected = listOf(
                    Triple("Cure", 15, "NoDebuff"),
                    Triple("Paralysis", 25, "Paralyzed"),
                    Triple("Poison", 40, "Poisoned")
                )
                game.getAvailableSpells() shouldBe expected
            }
            test("Allow this character to cast a spell") {
                val currentHp = game.getCharacterHp()
                var target = 0
                for (i in currentHp.indices) {
                    if (currentHp[i].first < currentHp[i].second) {
                        target = i
                        break
                    }
                }
                val possibleHealing = min(ceil((currentHp[target].second.toDouble() * 3 / 10f)).toInt(), currentHp[target].second - currentHp[target].first)
                val expected = Pair(possibleHealing, "NoDebuff")
                game.useMagic(0, target, false) shouldBe expected
            }
            test("Disallow this character from doing anything else before an end check is done") {
                assertThrows<IllegalStateActionException> {
                    game.useMagic(0, 0, false)
                }
                assertThrows<IllegalStateActionException> {
                    game.attack(1)
                }
                game.isGameOver() shouldBe false
                game.nextTurn()
            }
            test("Allow the next character available to attack") {
                var characterIndex = game.getCurrentCharacter()
                while (characterIndex == -1) {
                    game.nextTurn()
                    characterIndex = game.getCurrentCharacter()
                    if (characterIndex != -1) break
                    game.isGameOver()
                }
                game.attack(0)
            }
        }
    }
})
