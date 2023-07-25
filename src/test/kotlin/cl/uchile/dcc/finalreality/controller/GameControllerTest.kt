package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.exceptions.IllegalStateActionException
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.magic.blackmagic.MagicRNGSeeder
import cl.uchile.dcc.finalreality.model.weapon.Sword
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
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
    // To ensure 2 adverse effects land immediately
    MagicRNGSeeder.setSeed(24)
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
        "Dragon",
        "Red Panda"
    )
    context("A GameController should") {
        game = GameController()
        var character1Hp: Int
        var character2Hp: Int
        var character3Hp: Int
        var character4Hp: Int
        var character5Hp: Int
        var character4Mp: Int
        var character5Mp: Int
        val charactersHp = mutableListOf<Pair<Int, Int>>()
        val charactersMp = mutableListOf(
            Pair(0, 0),
            Pair(0, 0),
            Pair(0, 0),
        )
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

            game.createEngineer("TestEngineer") shouldBe false
            game.createKnight("TestKnight") shouldBe false
            game.createThief("TestThief") shouldBe false
            game.createWhiteMage("TestWhiteMage") shouldBe false
            game.createBlackMage("TestBlackMage") shouldBe false
            game.startBattle() shouldBe true
            // Advancing the seed....
            character1Hp = seedCopy.nextInt(300, 500)
            seedCopy.nextInt(20, 50)
            character2Hp = seedCopy.nextInt(400, 600)
            seedCopy.nextInt(30, 60)
            character3Hp = seedCopy.nextInt(300, 400)
            seedCopy.nextInt(15, 45)
            character4Hp = seedCopy.nextInt(200, 400)
            character4Mp = seedCopy.nextInt(100, 200)
            seedCopy.nextInt(10, 30)
            character5Hp = seedCopy.nextInt(200, 400)
            character5Mp = seedCopy.nextInt(100, 200)
            seedCopy.nextInt(10, 30)
            charactersHp.addAll(
                listOf(
                    Pair(character1Hp, character1Hp),
                    Pair(character2Hp, character2Hp),
                    Pair(character3Hp, character3Hp),
                    Pair(character4Hp, character4Hp),
                    Pair(character5Hp, character5Hp)
                )
            )
            charactersMp.addAll(
                listOf(
                    Pair(character4Mp, character4Mp),
                    Pair(character5Mp, character5Mp)
                )
            )
        }
        test("Be able to return the characters and enemies' Hp values") {
            game.getCharacterHp() shouldBe charactersHp
            game.getEnemyHp() shouldBe enemyHp
        }
        test("Be able to return the characters' Mp, 0 for non mages") {
            game.getCharacterMp() shouldBe charactersMp
        }
        test("Be able to check whether a character has debuffs") {
            val listOfDebuffs = game.getAdverseEffects(0, false)
            listOfDebuffs.isEmpty() shouldBe true
        }
        context("When not on a player turn disallow:") {
            test("Checking the inventory") {
                assertThrows<IllegalStateActionException> {
                    game.getInventory()
                }
            }
            test("Swapping weapons") {
                assertThrows<IllegalStateActionException> {
                    game.equipWeapon(0)
                }
            }
            test("Checking the equipped weapon's magic damage") {
                assertThrows<IllegalStateActionException> {
                    game.getMagicDamage()
                }
            }
            test("Checking spells") {
                assertThrows<IllegalStateActionException> {
                    game.getAvailableSpells()
                }
            }
            test("Casting spells") {
                assertThrows<IllegalStateActionException> {
                    game.useMagic(0, 0, true)
                }
            }
            test("Attacking") {
                assertThrows<IllegalStateActionException> {
                    game.attack(0)
                }
            }
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
            test("Give a turn to a character (White Mage)") {
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
                    Triple("BasicAxe", 80, 40),
                    Triple("BasicWand (Currently equipped)", 15, 21)
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
            test("Allow this character to cast a spell on its allies") {
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
            test("Disallow this character from doing any other action before an end check is done") {
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
                game.attack(0) shouldBeGreaterThan 0
                game.isGameOver() shouldBe false
            }
            context("Allow the next mage available to") {
                test("Cast a spell on an enemy") {
                    game.nextTurn()
                    var characterIndex = game.getCurrentCharacter()
                    while (characterIndex != 4) {
                        game.isGameOver()
                        game.nextTurn()
                        characterIndex = game.getCurrentCharacter()
                    }
                    val expected = listOf(
                        Triple("Fire", 15, "Burned"),
                        Triple("Thunder", 15, "Paralyzed")
                    )
                    game.getAvailableSpells() shouldBe expected
                }
                test("See the attacked enemy's applied debuffs") {
                    val mageDamage = game.getMagicDamage()
                    game.useMagic(0, 0, true) shouldBe Pair(30, "Burned")
                    val burnedDamage = ceil(mageDamage / 3f).toInt()
                    game.getAdverseEffects(
                        0,
                        true
                    ) shouldBe listOf("Burned { finalDamage: $burnedDamage, turnLimit: 5 }")
                }
                game.isGameOver()
            }
            test("Disallow attacking a dead enemy") {
                var characterIndex: Int

                fun oneEnemyDead(): Pair<Boolean, Int> {
                    val currentEnemyHp = game.getEnemyHp()
                    for (i in currentEnemyHp.indices) {
                        if (currentEnemyHp[i].first == 0) return Pair(true, i)
                    }
                    return Pair(false, -1)
                }
                var enemyDead = oneEnemyDead()
                while (!enemyDead.first) {
                    game.nextTurn()
                    characterIndex = game.getCurrentCharacter()
                    if (characterIndex != -1) game.attack(0)
                    game.isGameOver()
                    enemyDead = oneEnemyDead()
                }
                // Since it's the one attacked the most, the enemy with index 0 should be dead
                // But the result of the function will be taken to be sure
                val deadCharacterIndex = enemyDead.second
                // Current hp of the enemy on index position "deadCharacterIndex"
                game.getEnemyHp()[deadCharacterIndex].first shouldBe 0
                characterIndex = game.getCurrentCharacter()
                while (characterIndex == -1) {
                    game.nextTurn()
                    characterIndex = game.getCurrentCharacter()
                    if (characterIndex != -1) break
                    game.isGameOver()
                }
                game.attack(deadCharacterIndex) shouldBe -1
                game.gameState.isEndCheck() shouldBe false
                (game.gameState.isNonMagicalPlayerTurn() || game.gameState.isMagicalPlayerTurn()) shouldBe true
                var someOtherEnemyIndex = Random.nextInt(0, 5)
                while (someOtherEnemyIndex == deadCharacterIndex) someOtherEnemyIndex = Random.nextInt(0, 5)
                game.attack(someOtherEnemyIndex) shouldBeGreaterThanOrEqual 0
                game.gameState.isEndCheck() shouldBe true
                game.isGameOver() shouldBe false
            }
            test("Disallow a dead character from acting for the rest of the game") {
                fun characterDead(): Pair<Boolean, Int> {
                    val characters = game.getCharacterHp()
                    for (i in characters.indices) {
                        if (characters[i].first == 0) return Pair(true, i)
                    }
                    return Pair(false, -1)
                }

                var characterDead = characterDead()
                var characterIndex: Int
                while (!characterDead.first) {
                    game.nextTurn()
                    characterIndex = game.getCurrentCharacter()
                    if (characterIndex != -1) {
                        var dmg = game.attack(Random.nextInt(0, 5))
                        while (dmg == -1) dmg = game.attack(Random.nextInt(0, 5))
                    }
                    characterDead = characterDead()
                    if (characterDead.first) break
                    game.isGameOver()
                }
                val deadCharacterIndex = characterDead.second
                var currentCharacter: Int
                var gameOver = game.isGameOver()
                while (!gameOver) {
                    game.nextTurn()
                    currentCharacter = game.getCurrentCharacter()
                    currentCharacter shouldNotBe deadCharacterIndex
                    if (currentCharacter != -1) {
                        var dmg = game.attack(Random.nextInt(0, 5))
                        while (dmg == -1) dmg = game.attack(Random.nextInt(0, 5))
                    }
                    gameOver = game.isGameOver()
                }
                (game.gameState.isEnemyDefeated() || game.gameState.isPlayerDefeated()) shouldBe true
            }
            context("Be able to determine who won the game and") {
                test("Allow the player to restart the game when defeated") {
                    game.onEnemyWin(true)
                    game.gameState.isCharacterCreation() shouldBe true
                    game.createKnight("OpKnight1")
                    game.createKnight("OpKnight2")
                    game.createKnight("OpKnight3")
                    game.createKnight("OpKnight4")
                    game.createBlackMage("Lone Black Mage")
                    // These knights will be given really broken weapons
                    // to equip to end the game swiftly
                    val opSword = Sword("Ultima Sword", 9999, 30)
                    PlayerCharacter.addWeaponToInventory(opSword)
                    PlayerCharacter.addWeaponToInventory(opSword)
                    PlayerCharacter.addWeaponToInventory(opSword)
                    PlayerCharacter.addWeaponToInventory(opSword)
                    game.startBattle() shouldBe true
                }
                test("Allow the player to continue playing when victorious, give them a new weapon, and heal 10% of the characters' hp and mp") {
                    var gameOver = false
                    var currentCharacter: Int
                    val enemies = game.getEnemyHp().size
                    var inventorySize = PlayerCharacter.getInventory().size
                    val weaponSwitched = mutableListOf(false, false, false, false, false)
                    while (!gameOver) {
                        game.nextTurn()
                        currentCharacter = game.getCurrentCharacter()
                        if (currentCharacter != -1 && currentCharacter != 4) {
                            if (!weaponSwitched[currentCharacter]) {
                                weaponSwitched[currentCharacter] = true
                                val inventory = game.getInventory()
                                inventorySize = inventory.size - 1
                                for (index in inventory.indices) {
                                    if (inventory[index].first == "Ultima Sword") {
                                        game.equipWeapon(index)
                                        break
                                    }
                                }
                            }
                            var dmg = game.attack(Random.nextInt(0, enemies))
                            while (dmg == -1) dmg = game.attack(Random.nextInt(0, enemies))
                        } else if (currentCharacter == 4) {
                            var cast = game.useMagic(0, Random.nextInt(0, enemies), true)
                            while (cast.first == -1) cast = game.useMagic(0, Random.nextInt(0, enemies), true)
                        }
                        gameOver = game.isGameOver()
                    }
                    game.isPlayerWinner() shouldBe true
                    game.gameState.isEnemyDefeated() shouldBe true
                    val hpBefore = game.getCharacterHp()
                    val mpBefore = game.getCharacterMp()
                    game.onPlayerWin(true)
                    game.gameState.isTurnWait() shouldBe true
                    PlayerCharacter.getInventory().size shouldBe inventorySize + 1
                    val hpNow = game.getCharacterHp()
                    val mpNow = game.getCharacterMp()
                    for (i in hpNow.indices) {
                        hpNow[i].first shouldBeGreaterThanOrEqual hpBefore[i].first
                        if (mpNow[i].second != 0) {
                            mpNow[i].first shouldBeGreaterThanOrEqual mpBefore[i].first
                        }
                    }
                }
            }
        }
    }
    afterSpec {
        PlayerCharacter.resetInventory()
    }
})
