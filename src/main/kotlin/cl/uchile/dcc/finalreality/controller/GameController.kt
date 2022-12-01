package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.controller.state.CharacterCreationState
import cl.uchile.dcc.finalreality.controller.state.GameState
import cl.uchile.dcc.finalreality.exceptions.IllegalActionException
import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.Mage
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

/**
 * Class that controls and changes the state of the game
 *
 * @property turnsQueue the game's turns queue
 * @property playerCharacters the game's active player characters
 * @property enemyCharacters the game's active enemy characters
 * @property playerInventory the player characters' weapon inventory as an immutable list
 * @property gameIsOver indicator of the game's end
 * @property activeCharacterIndex the index of the current character's turn
 * @property enemyNameList a list of names to assign to the enemies
 * @property playerWin whether the player has won (if game is over)
 */
class GameController {
    /**
     * This block initializes the game generating a random number of enemies
     */
    init {
        val enemyAmount = Random.nextInt(1, 6)
        generateEnemy(enemyAmount)
    }
    private val turnsQueue = LinkedBlockingQueue<GameCharacter>()
    private val playerCharacters = mutableListOf<PlayerCharacter>()
    private val enemyCharacters = mutableListOf<Enemy>()
    private var gameIsOver = false
    private var realState: GameState = CharacterCreationState(this)
    val state: GameState
        get() = realState
    private val playerInventory
        get() = PlayerCharacter.getInventory()
    private val enemyNameList: List<String> = listOf(
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
    private var playerWin = false

    /**
     * Function to check whether the player won,
     * this function only makes sense if gameIsOver is true
     */
    fun isPlayerWinner(): Boolean {
        if (state.isPlayerDefeated() || state.isEnemyDefeated()) return playerWin
        else throw IllegalActionException("ask if the player won", state::class.simpleName!!)
    }

    /**
     * A -1 value implies there's no active character that can take a turn
     * either due to not having checked the queue for the first time or a character
     * being paralyzed
     */
    private var activeCharacterIndex: Int = -1

    /**
     * Function to get the index of the character that has to take a turn
     * @return the character's index or -1 if no active character
     */
    fun getCurrentCharacter(): Int = activeCharacterIndex

    fun setState(state: GameState) {
        this.realState = state
    }

    /**
     * Function to get the weapons in the inventory
     * @return A list of triples with the weapon's name, damage and weight
     */
    fun getInventory(): List<Triple<String, Int, Int>> {
        if (state.isNonMagicalPlayerTurn() || state.isMagicalPlayerTurn()) {
            val viewData = mutableListOf<Triple<String, Int, Int>>()
            for (weapon in playerInventory) {
                val data = Triple(weapon.name, weapon.damage, weapon.weight)
                viewData.add(data)
            }
            state.toWeaponEquip()
            return viewData.toList()
        } else {
            throw IllegalActionException("check the inventory", state::class.simpleName!!)
        }
    }

    /**
     * Attempts to equip a weapon from the inventory with the specified [weaponId] to the
     * character with the specified [characterId]
     * @return whether the weapon was successfully equipped or not
     */
    fun equipWeapon(characterId: Int, weaponId: Int): Boolean {
        return state.equipWeapon(playerCharacters[characterId], playerInventory[weaponId])
    }

    /**
     * Function to get enemy names
     * @return the names of the enemies participating in this battle
     */
    fun getEnemyNames(): List<String> {
        val list = mutableListOf<String>()
        for (enemy in enemyCharacters) {
            list.add(enemy.name)
        }
        return list.toList()
    }
    /**
     * Creates a new engineer and adds it to the player characters list
     */
    fun createEngineer(name: String) {
        playerCharacters.add(
            state.createEngineer(name, turnsQueue)
        )
    }
    /**
     * Creates a new knight
     */
    fun createKnight(name: String) {
        playerCharacters.add(
            state.createKnight(name, turnsQueue)
        )
    }
    /**
     * Creates a new thief
     */
    fun createThief(name: String) {
        playerCharacters.add(
            state.createThief(name, turnsQueue)
        )
    }
    /**
     * Creates a new white mage
     */
    fun createWhiteMage(name: String) {
        playerCharacters.add(
            state.createWhiteMage(name, turnsQueue)
        )
    }
    /**
     * Creates a new black mage
     */
    fun createBlackMage(name: String) {
        playerCharacters.add(
            state.createBlackMage(name, turnsQueue)
        )
    }
    /**
     * Function to get player Hp values
     * @return a list of pairs containing the current and maxHp of each character in the
     * same order they are present on the [playerCharacters] list
     */
    fun getCharacterHp(): List<Pair<Int, Int>> {
        val hpList: MutableList<Pair<Int, Int>> = mutableListOf()
        for (character in playerCharacters) {
            val pair = Pair(character.currentHp, character.maxHp)
            hpList.add(pair)
        }
        return hpList.toList()
    }

    /**
     * Function to get enemy Hp values
     * @return a list of pairs containing the current and maxHp of each character in the
     * same order they are present on the [enemyCharacters] list
     */
    fun getEnemyHp(): List<Pair<Int, Int>> {
        val hpList: MutableList<Pair<Int, Int>> = mutableListOf()
        for (character in enemyCharacters) {
            val pair = Pair(character.currentHp, character.maxHp)
            hpList.add(pair)
        }
        return hpList.toList()
    }

    /**
     * Function to get the active character's spells
     * @return a list of triples with spell's name, it's cost and possible effect name
     */
    fun getAvailableSpells(): List<Triple<String, Int, String>> {
        if (state.isMagicalPlayerTurn()) {
            val spells = playerCharacters[activeCharacterIndex].getSpells()
            val viewData = mutableListOf<Triple<String, Int, String>>()
            for (spell in spells) {
                val info = Triple(spell::class.simpleName!!, spell.cost, spell.debuff::class.simpleName!!)
                viewData.add(info)
            }
            return viewData.toList()
        } else {
            throw IllegalActionException("check the available spells", state::class.simpleName!!)
        }
    }
    /**
     * Function called to take turns
     */
    fun nextTurn() {
        val character: GameCharacter = state.nextTurn(turnsQueue)
        val canAct = character.rollEffects()
        if (canAct) {
            activeCharacterIndex = playerCharacters.indexOf(character)
            if (character.isMage()) {
                state.toMagicalPlayerTurn()
            } else if (character.isPlayerCharacter()) {
                state.toNonMagicalPlayerTurn()
            } else {
                state.toEnemyTurn()
            }
        }
        state.toEndCheck()
        activeCharacterIndex = -1
    }

    /**
     * To generate a new enemy and add it to the enemies' side
     */
    fun generateEnemy(amount: Int) {
        if (state.isEnemyGeneration() || state.isCharacterCreation()) {
            for (i in 1..amount) {
                val newEnemy = Enemy(
                    enemyNameList[Random.nextInt(0, enemyNameList.size)],
                    Random.nextInt(60, 100),
                    Random.nextInt(15, 45),
                    Random.nextInt(300, 600),
                    Random.nextInt(5, 20),
                    turnsQueue
                )
                enemyCharacters.add(newEnemy)
                newEnemy.waitTurn()
            }
        } else throw IllegalActionException("generate enemies", state::class.simpleName!!)
    }
    /**
     * Public function to issue an attack
     * @param id1 the index of the player character on the player character list
     * @param id2 the index of the enemy character on the enemy character list
     */
    fun attack(id1: Int, id2: Int): Int {
        val damage = state.attack(playerCharacters[id1], enemyCharacters[id2])
        advanceTurn(playerCharacters[id1])
        return damage
    }
    /**
     * Issues a cast command to the caster given by [casterId] unto a target character
     * @param casterId index of the caster
     * @param spellId index of the spell on the caster's spell list
     * @param enemyTarget whether this spell targets enemies or allies
     * @return a pair with the damage dealt and debuff if any
     */
    fun useMagic(casterId: Int, spellId: Int, targetId: Int, enemyTarget: Boolean): Pair<Int, String> {
        val attacker = playerCharacters[casterId] as Mage
        val spellList = attacker.getSpells()
        val target = if (enemyTarget) enemyCharacters[targetId] else playerCharacters[targetId]
        val (damage, debuff) = state.useMagic(attacker, spellList[spellId], target)
        advanceTurn(attacker)
        return Pair(damage, debuff::class.simpleName!!)
    }

    /**
     * Function to end any turn
     * @param character the character whose turn is ending
     */
    private fun advanceTurn(character: GameCharacter) {
        turnsQueue.poll()
        waitTurn(character)
        state.toEndCheck()
    }

    /**
     * Function to end a character's turn
     * @param characterId the index of the character in the [playerCharacters] list
     */
    fun endTurn(characterId: Int) {
        val character = playerCharacters[characterId]
        advanceTurn(character)
    }

    /**
     * Function to make a character wait their turn only if they're
     * still alive
     * @param character the character that wishes to rejoin the turns queue
     */
    private fun waitTurn(character: GameCharacter) {
        if (character.currentHp != 0) character.waitTurn()
    }
    /**
     * Function to make enemies attack
     */
    fun enemyTurn(character: GameCharacter) {
        if (state.isEnemyTurn()) {
            var attackDone = false
            while (!attackDone) {
                val k = Random.nextInt(0, 5)
                if (playerCharacters[k].currentHp != 0) {
                    state.enemyAttack(character, playerCharacters[k])
                    attackDone = true
                }
            }
            advanceTurn(character)
        } else {
            throw IllegalActionException("make an enemy attack", state::class.simpleName!!)
        }
    }

    /**
     * Function to handle the user's victory, it gives the user a new weapon and
     * asks them if they want to continue playing
     * @param nextBattle whether the user wants to create another battle
     */
    fun onPlayerWin(nextBattle: Boolean) {
        state.onPlayerWin(nextBattle)
    }

    /**
     * Function to handle enemy victory
     * @param nextGame whether the user wants to play again
     */
    fun onEnemyWin(nextGame: Boolean) {
        state.onEnemyWin(nextGame)
    }

    /**
     * Function to check whether the game has finished
     */
    fun isGameOver(): Boolean {
        if (state.isEndCheck()) {
            val playerDead = oneSideDead(playerCharacters)
            if (playerDead) {
                gameIsOver = true
                state.toPlayerDefeated()
                playerWin = false
            }
            val enemyDead = oneSideDead(enemyCharacters)
            if (enemyDead) {
                gameIsOver = true
                state.toEnemyDefeated()
                playerWin = true
            }
            if (!gameIsOver) state.toTurnWait()
            return gameIsOver
        } else {
            throw IllegalActionException("check the game's end", state::class.simpleName!!)
        }
    }

    /**
     * Checks if all the characters from any side (enemy or player)
     * have their hp set to 0 (i.e. are dead)
     * @return whether the enemy/player's side died
     */
    private fun oneSideDead(side: List<GameCharacter>): Boolean {
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
