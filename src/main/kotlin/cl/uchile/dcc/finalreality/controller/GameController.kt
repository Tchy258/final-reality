package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.Mage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.magic.Magic
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Cure
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Paralysis
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Poison
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.InputMismatchException
import java.util.Scanner
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random
import kotlin.reflect.KFunction1

/**
 * Class that controls and changes the state of the game
 *
 * @property turnsQueue the game's turns queue
 * @property playerCharacters the game's active player characters
 * @property enemyCharacters the game's active enemy characters
 * @property input the input source to take commands and change state
 * @property gameIsOver indicator of the game's end
 */
class GameController(private var input: Scanner = Scanner(System.`in`)) {
    private val turnsQueue = LinkedBlockingQueue<GameCharacter>()
    private val playerCharacters = mutableListOf<PlayerCharacter>()
    private val enemyCharacters = mutableListOf<Enemy>()
    private var gameIsOver = false

    /**
     * Changes the source that this GameController uses to take commands
     * @param source a source of input
     */
    fun changeInputSource(source: Scanner) {
        input = source
    }

    /**
     * This block initializes the game
     */
    init {
        println("Please name your characters\n")
        println("Engineer: ")
        val engineerName = input.next()
        playerCharacters.add(
            Engineer(
                engineerName,
                Random.nextInt(300, 500),
                Random.nextInt(20, 40),
                turnsQueue
            )
        )
        playerCharacters[0].equip(
            Axe("BasicAxe", 60, 40)
        )
        println("Knight: ")
        val knightName = input.next()
        playerCharacters.add(
            Knight(
                knightName,
                Random.nextInt(400, 600),
                Random.nextInt(30, 45),
                turnsQueue
            )
        )
        playerCharacters[1].equip(
            Sword("BasicSword", 50, 30)
        )
        println("Thief: ")
        val thiefName = input.next()
        playerCharacters.add(
            Thief(
                thiefName,
                Random.nextInt(200, 400),
                Random.nextInt(15, 25),
                turnsQueue
            )
        )
        playerCharacters[2].equip(
            Bow("BasicBow", 40, 20)
        )
        println("BlackMage: ")
        val blackMageName = input.next()
        playerCharacters.add(
            BlackMage(
                blackMageName,
                Random.nextInt(200, 400),
                Random.nextInt(300, 500),
                Random.nextInt(10, 20),
                turnsQueue
            )
        )
        playerCharacters[3].equip(
            Knife("BasicKnife", 40, 20)
        )
        println("WhiteMage: ")
        val whiteMageName = input.next()
        playerCharacters.add(
            WhiteMage(
                whiteMageName,
                Random.nextInt(200, 400),
                Random.nextInt(300, 500),
                Random.nextInt(10, 20),
                turnsQueue
            )
        )
        playerCharacters[4].equip(
            Staff("BasicStaff", 30, 20, 30)
        )
        for (i in 1..6) {
            enemyCharacters.add(
                Enemy(
                    "Enemy$i",
                    Random.nextInt(60, 100),
                    Random.nextInt(15, 45),
                    Random.nextInt(300, 600),
                    Random.nextInt(5, 20),
                    turnsQueue
                )
            )
        }
        for (character in playerCharacters) {
            character.waitTurn()
        }
        for (enemy in enemyCharacters) {
            enemy.waitTurn()
        }
        nextTurn()
    }

    /**
     * Function called immediately after the init block
     */
    fun nextTurn() {
        if (!gameIsOver) {
            if (turnsQueue.isEmpty()) {
                Thread.sleep(2000)
                nextTurn()
            }
            val character: GameCharacter = turnsQueue.poll()
            character.rollEffects()
            if (character.currentHp == 0) {
                println("${character.name} has died to an adverse effect!")
                nextTurn()
            } else {
                character.takeTurn(this)
            }
            var playerDead = true
            for (playerCharacter in playerCharacters) {
                playerDead = playerDead && (playerCharacter.currentHp == 0)
            }
            if (playerDead) {
                onEnemyWin()
                return
            }
            var enemyDead = true
            for (enemy in enemyCharacters) {
                enemyDead = enemyDead && (enemy.currentHp == 0)
            }
            if (enemyDead) {
                onPlayerWin()
            }
            nextTurn()
        } else {
            return
        }
    }
    /**
     * To generate new enemies when the user has defeated all enemies
     * and wishes to continue playing
     */
    fun generateMoreEnemies() {
        enemyCharacters.clear()
        for (i in 1..6) {
            enemyCharacters.add(
                Enemy(
                    "Enemy$i",
                    Random.nextInt(60, 100),
                    Random.nextInt(15, 45),
                    Random.nextInt(300, 600),
                    Random.nextInt(5, 20),
                    turnsQueue
                )
            )
        }
        for (enemy in enemyCharacters) {
            enemy.waitTurn()
        }
    }

    /**
     * Issue an attack command from [attacker] to [target]
     * @param attacker the character that attacks
     * @param target the character that receives the attack
     *
     */
    fun attack(attacker: GameCharacter, target: GameCharacter) {
        if (!attacker.isParalyzed()) {
            println("${attacker.name} attacks ${target.name}!")
            if (target.currentHp == 0) {
                println("${target.name} has been defeated")
            }
        } else {
            println("${attacker.name} is paralyzed, couldn't attack")
        }
        attacker.waitTurn()
    }

    /**
     * Function to look for a weapon in the inventory and choose to equip it
     * @param oldWeapon the character's currently equipped weapon
     * @return the weapon to be equipped
     */
    fun swapWeapon(oldWeapon: Weapon): Weapon {
        val inventory = PlayerCharacter.getInventory()
        for (i in inventory.indices) {
            println("$i " + inventory[i])
        }
        var answer: Int
        println("Which weapon should this character equip? (0 to ${inventory.size - 1}, -1 to keep current weapon) ")
        do {
            try {
                answer = input.nextInt()
                if (answer < -1 || answer >= inventory.size) {
                    println("Please choose a valid option")
                }
            } catch (e: InputMismatchException) {
                answer = -2
                println("Please choose a valid option")
            }
        } while (answer >= inventory.size || answer < -1)

        return if (answer == -1) {
            oldWeapon
        } else {
            inventory[answer]
        }
    }

    /**
     * Issues a cast command to the [attacker] unto a [target]
     * @return whether the attacker executed the spell (T) or didn't have enough mana (F)
     */
    fun useMagic(spell: Magic, attacker: Mage, target: GameCharacter): Boolean {
        val couldCast = attacker.cast(spell, target)
        val wasParalyzed = target.isParalyzed()
        val wasBurned = target.isBurned()
        val wasPoisoned = target.isPoisoned()
        if (couldCast) {
            println("${attacker.name} casts ${spell::class.simpleName!!} on ${target.name}!")
            if (!wasParalyzed && attacker.isParalyzed()) {
                println("${target.name} has been paralyzed!")
            }
            if (!wasBurned && attacker.isBurned()) {
                println("${target.name} has been burned!")
            }
            if (!wasPoisoned && attacker.isPoisoned()) {
                println("${target.name} has been poisoned!")
            }
        } else {
            println("${attacker.name} can't cast ${spell::class.simpleName!!}, insufficient Mp")
        }
        return couldCast
    }

    /**
     * Handler for non-magical player characters' turns
     * @param character the character taking a turn
     */
    fun playerCharacterTurn(character: PlayerCharacter) {
        println("${character.name}'s turn!")
        println("Choose an action: ")
        println("1 or 3 Attack")
        println("2 Swap Weapon")
        characterActionSelection(character)
        val target = targetSelection(character, true, ::playerCharacterTurn)
        attack(character, target)
    }

    /**
     * Handler to choose an action when it's the user's turn
     * @return a value that represents the action to execute
     */
    fun characterActionSelection(character: PlayerCharacter): Int {
        var answer: Int = input.nextInt()
        while (answer != 1 && answer != 3) {
            if (answer == 2) {
                val newWeapon = swapWeapon(character.equippedWeapon)
                if (newWeapon != character.equippedWeapon) {
                    println("${character.name} equipped ${newWeapon.name}")
                }
            } else {
                println("Please choose a valid option")
            }
            println("Choose an action (1, 2, 3)")
            answer = try {
                input.nextInt()
            } catch (e: InputMismatchException) {
                -2
            }
        }
        return answer
    }

    /**
     * Function to select a GameCharacter to attack or cast a spell unto
     * @param character the character that is choosing a target
     * @param callerFun the original turn handling function the [character] came from
     * @return the chosen target character
     */
    fun targetSelection(
        character: PlayerCharacter,
        selectEnemies: Boolean,
        callerFun: KFunction1<PlayerCharacter, Unit>
    ): GameCharacter {
        println("Choose a target: ")
        if (selectEnemies) {
            for (i in 1..enemyCharacters.size) {
                println("$i ${enemyCharacters[i - 1].name}")
            }
        } else {
            for (i in 1..playerCharacters.size) {
                println("$i ${playerCharacters[i - 1].name}")
            }
        }
        println("(Input -1 to go back)")
        var target: Int = input.nextInt()
        val upperLimit = if (selectEnemies) enemyCharacters.size else playerCharacters.size
        while (target < 1 || target > upperLimit) {
            if (target == -1) {
                callerFun(character)
            } else {
                println("Please choose a valid target")
            }
            target = try {
                input.nextInt()
            } catch (e: InputMismatchException) {
                -2
            }
        }
        return if (selectEnemies) {
            enemyCharacters[target]
        } else {
            playerCharacters[target]
        }
    }

    /**
     * Handler function for mage characters' turns
     * @param character the mage taking this turn
     */
    fun playerBlackMageTurn(character: PlayerCharacter) {
        println("${character.name}'s turn!")
        println("Choose an action: ")
        println("1 Attack")
        println("2 Swap Weapon")
        println("3 Cast Magic")
        val action = characterActionSelection(character)
        var spell: Magic = Fire() // Dummy value
        if (action == 3) {
            spell = selectBlackMagic()
        }
        println("Target enemies or allies? [E/a]")
        val selectEnemies = input.next().lowercase() != "a"
        val target = targetSelection(character, selectEnemies, ::playerBlackMageTurn)
        if (action == 1) {
            attack(character, target)
        } else if (action == 3) {
            useMagic(spell, character as BlackMage, target)
        }
    }

    /**
     * Handler function for mage characters' turns
     * @param character the mage taking this turn
     */
    fun playerWhiteMageTurn(character: PlayerCharacter) {
        println("${character.name}'s turn!")
        println("Choose an action: ")
        println("1 Attack")
        println("2 Swap Weapon")
        println("3 Cast Magic")
        val action = characterActionSelection(character)
        var spell: Magic = Cure() // Dummy value
        if (action == 3) {
            spell = selectWhiteMagic()
        }
        println("Target enemies or allies? [E/a]")
        val selectEnemies = input.next().lowercase() != "a"
        val target = targetSelection(character, selectEnemies, ::playerWhiteMageTurn)
        if (action == 1) {
            attack(character, target)
        } else if (action == 3) {
            useMagic(spell, character as WhiteMage, target)
        }
    }
    fun enemyTurn(character: Enemy) {
        var attackDone = false
        while (!attackDone) {
            val k = Random.nextInt(0, 5)
            if (playerCharacters[k].currentHp != 0) {
                attack(character, playerCharacters[k])
                attackDone = true
            }
        }
    }
    /**
     * Handler function to choose a black magic spell
     */
    fun selectBlackMagic(): Magic {
        println("Choose a spell: ")
        println("1 Thunder")
        println("2 Fire")
        val answer: Int = try {
            input.nextInt()
        } catch (e: InputMismatchException) {
            println("Invalid input, thunder chosen by default")
            1
        }
        return if (answer == 1) {
            Thunder()
        } else {
            Fire()
        }
    }

    /**
     * Handler function to choose a white magic spell
     */
    fun selectWhiteMagic(): Magic {
        println("Choose a spell: ")
        println("1 Cure")
        println("2 Poison")
        println("3 Paralysis")
        val answer: Int = try {
            input.nextInt()
        } catch (e: InputMismatchException) {
            println("Invalid input, cure chosen by default")
            1
        }
        return when (answer) {
            1 -> Cure()
            2 -> Poison()
            else -> Paralysis()
        }
    }

    /**
     * Function to make enemies attack
     */

    /**
     * Function to handle the user's victory, it gives the user a new weapon and
     * asks them if they want to continue playing
     */
    fun onPlayerWin() {
        val k = Random.nextInt(0, 5)
        val j = Random.nextInt(0, 5)
        val prefix: String = when (j) {
            0 -> "Ancient"
            1 -> "Good Quality"
            2 -> "Mystic"
            3 -> "Enchanted"
            else -> "Blessed"
        }
        val damage = min(300, Random.nextInt(30, 60) * (j + 1))
        val weight = max(15, Random.nextInt(20, 40) / (j + 1))
        val magicDamage = min(200, Random.nextInt(30, 60) * (j + 1))
        val newWeapon: Weapon = when (k) {
            0 -> Axe("$prefix Axe", damage, weight)
            1 -> Bow("$prefix Bow", damage, weight)
            2 -> Knife("$prefix Knife", damage, weight)
            3 -> Sword("$prefix Sword", damage, weight)
            else -> Staff("$prefix Staff", damage, weight, magicDamage)
        }
        PlayerCharacter.addWeaponToInventory(newWeapon)
        println("Obtained $newWeapon!")
        println("Continue to the next fight? [y/N]")
        val answer: String? = input.next()
        if (answer != null) {
            if (answer.lowercase() == "y") {
                generateMoreEnemies()
                nextTurn()
            }
        } else {
            gameIsOver = true
            return
        }
    }

    /**
     * Function to handle enemy victory
     */
    fun onEnemyWin() {
        gameIsOver = true
    }

    /**
     * Function to check the game state
     */
    fun isGameOver(): Boolean {
        return gameIsOver
    }

    override fun toString(): String {
        TODO("Not implemented yet")
    }
}
