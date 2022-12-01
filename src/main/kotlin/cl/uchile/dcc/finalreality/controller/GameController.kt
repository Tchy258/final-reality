package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
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
    private var enemyVictory = false
    val playerInventory
        get() = PlayerCharacter.getInventory()

    private val enemyNames: List<String> = listOf(
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
        "Red Fox"
    )

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
        for (i in 1..Random.nextInt(1, 6)) {
            generateEnemy()
        }
        for (character in playerCharacters) {
            character.waitTurn()
        }
        for (enemy in enemyCharacters) {
            enemy.waitTurn()
        }
    }

    /**
     * Function called to take turns and update the game state
     * @return the next non-enemy character that can act and use a turn
     */
    tailrec fun update(): PlayerCharacter {
        if (turnsQueue.isEmpty()) {
            Thread.sleep(2000)
            return update()
        }
        print(this)
        val character: GameCharacter = turnsQueue.peek()
        val canAct = character.rollEffects()
        return if (canAct) {
            character.takeTurn(this)
        } else {
            advanceTurn(character)
            update()
        }
    }
    /**
     * To generate a new enemy and add it to the enemies' side
     */
    fun generateEnemy() {
        val newEnemy = Enemy(
            enemyNames[Random.nextInt(0, enemyNames.size)],
            Random.nextInt(60, 100),
            Random.nextInt(15, 45),
            Random.nextInt(300, 600),
            Random.nextInt(5, 20),
            turnsQueue
        )
        enemyCharacters.add(newEnemy)
        newEnemy.waitTurn()
    }

    /**
     * Issue an attack command from [attacker] to [target]
     * @param attacker the character that attacks
     * @param target the character that receives the attack
     * @return the amount of damage dealt
     */
    fun attack(attacker: GameCharacter, target: GameCharacter): Int {
        var damage = 0
        if (!attacker.isParalyzed()) {
            damage = attacker.attack(target)
        }
        advanceTurn(attacker)
        return damage
    }

    /**
     * Function to look swap a character's weapon
     * @param character the character who wants to swap weapons
     */
    fun equipWeapon(character: PlayerCharacter, weapon: Weapon) {
        character.equip(weapon)
    }

    /**
     * Issues a cast command to the [attacker] unto a [target]
     * @return a pair with the damage dealt and debuff if any
     */
    fun useMagic(spell: Magic, attacker: Mage, target: GameCharacter): Pair<Int, Debuff?> {
        return attacker.cast(spell, target)
    }

    fun advanceTurn(character: GameCharacter) {
        turnsQueue.poll()
        waitTurn(character)
    }

    fun waitTurn(character: GameCharacter) {
        if (character.currentHp != 0) character.waitTurn()
    }
    /**
     * Function to make enemies attack
     */
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
     * Function to handle the user's victory, it gives the user a new weapon and
     * asks them if they want to continue playing
     * @param nextBattle whether the user wants to create another battle
     */
    fun onPlayerWin(nextBattle: Boolean) {
        // println("Continue to the next fight? [y/N]")
        if (nextBattle) {
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
            // println("Obtained $newWeapon!")
        } else {
            gameIsOver = true
            return
        }
    }

    /**
     * Function to handle enemy victory
     */
    fun onEnemyWin() {
        // println("Your party has been defeated")
        enemyVictory = true
    }

    /**
     * Function to check whether the game has finished
     */
    fun isGameOver() {
        val playerDead = oneSideDead(playerCharacters)
        if (playerDead) {
            gameIsOver = true
            enemyVictory = true
        }
        val enemyDead = oneSideDead(enemyCharacters)
        if (enemyDead) {
            gameIsOver = true
        }
    }

    /**
     * Checks if all the characters from any side (enemy or player)
     * have their hp set to 0 (i.e. are dead)
     * @return whether the enemy/player's side died
     */
    private fun oneSideDead(side: MutableList<out GameCharacter>): Boolean {
        var sideDead = true
        for (character in side) {
            sideDead = sideDead && character.currentHp == 0
        }
        return sideDead
    }

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append("Enemies: ")
        for (i in 0..9) {
            builder.append(' ')
        }
        builder.append("Characters:\n")
        for (i in 0..4) {
            val enemy = enemyCharacters[i]
            builder.append("${enemy.name} HP:${enemy.currentHp}/${enemy.maxHp} ")
            for (j in 0..11) {
                builder.append(' ')
            }
            val character = playerCharacters[i]
            builder.append(character)
            builder.append("\n")
        }
        builder.append("${enemyCharacters[5].name} HP:${enemyCharacters[5].currentHp}/${enemyCharacters[5].maxHp}")
        builder.append("\n\n")
        return builder.toString()
    }
}
