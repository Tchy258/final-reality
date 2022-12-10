package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.controller.state.CharacterCreationState
import cl.uchile.dcc.finalreality.controller.state.GameState
import cl.uchile.dcc.finalreality.exceptions.IllegalStateActionException
import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.Mage
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.ceil
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
 *
 * @constructor Creates a new game controller
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class GameController {
    private var realState: GameState
    private val turnsQueue: LinkedBlockingQueue<GameCharacter>
    private val playerCharacters: MutableList<PlayerCharacter>
    private val enemyCharacters: MutableList<Enemy>
    private var gameIsOver: Boolean
    val gameState: GameState
        get() = realState
    private val playerInventory
        get() = PlayerCharacter.getInventory()
    private val enemyNameList: List<String>
    private var playerWin: Boolean
    /**
     * This block initializes the game generating a random number of enemies
     */
    init {
        val enemyAmount = ControllerRNGSeed.seed.nextInt(1, 6)
        realState = CharacterCreationState(this)
        turnsQueue = LinkedBlockingQueue()
        playerCharacters = mutableListOf()
        enemyCharacters = mutableListOf()
        gameIsOver = false
        enemyNameList = listOf(
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
        playerWin = false
        generateEnemy(enemyAmount)
    }

    /**
     * Function to check whether the player won,
     * this function only makes sense if gameIsOver is true
     */
    fun isPlayerWinner(): Boolean {
        if (gameState.isPlayerDefeated() || gameState.isEnemyDefeated()) return playerWin
        else throw IllegalStateActionException("ask if the player won", gameState::class.simpleName!!)
    }

    /**
     * A -1 value implies there's no active player character that can take a turn
     * either due to not having checked the queue for the first time, the character
     * being paralyzed, the player turn has already finished, or it's an enemy's turn
     */
    private var activeCharacterIndex: Int = -1

    /**
     * Function to get the index of the character that has to take a turn
     * @return the character's index or -1 if no active character
     */
    fun getCurrentCharacter(): Int = activeCharacterIndex

    /**
     * Function to get any character's adverse effects applied (if any)
     * @param index the index of the character whose effects are requested
     * @param enemyEffects whether the adverse effects of an enemy are being requested or not
     * @return a list of strings containing the character's adverse effects' string representation
     */
    fun getAdverseEffects(index: Int, enemyEffects: Boolean): List<String> {
        val character = if (enemyEffects) {
            enemyCharacters[index]
        } else {
            playerCharacters[index]
        }
        return character.getDebuffs()
    }

    /**
     * Function to set the game's state
     * @param state the new game state
     */
    fun setState(state: GameState) {
        this.realState = state
    }

    /**
     * Function to get the weapons in the inventory
     * @return A list of triples with the weapons' name, damage and weight
     */
    fun getInventory(): List<Triple<String, Int, Int>> {
        if (gameState.isNonMagicalPlayerTurn() || gameState.isMagicalPlayerTurn()) {
            val viewData = mutableListOf<Triple<String, Int, Int>>()
            for (weapon in playerInventory) {
                val data = Triple(weapon.name, weapon.damage, weapon.weight)
                viewData.add(data)
            }
            val equippedWeapon = playerCharacters[activeCharacterIndex].equippedWeapon
            viewData.add(Triple("${equippedWeapon.name} (Currently equipped)", equippedWeapon.damage, equippedWeapon.weight))
            gameState.toWeaponEquip()
            return viewData.toList()
        } else {
            throw IllegalStateActionException("check the inventory", gameState::class.simpleName!!)
        }
    }
    /**
     * Returns the equipped weapon's magic damage
     * This function is only usable on magic character turns
     */
    fun getMagicDamage(): Int {
        if (gameState.isMagicalPlayerTurn()) {
            return gameState.getMagicDamage(playerCharacters[activeCharacterIndex])
        } else {
            throw IllegalStateActionException("check a weapon's magic damage", gameState::class.simpleName!!)
        }
    }

    /**
     * Attempts to equip a weapon from the inventory with the specified [weaponId] to the
     * character with the [activeCharacterIndex]
     * @return whether the weapon was successfully equipped or not
     */
    fun equipWeapon(weaponId: Int): Boolean {
        if (gameState.isWeaponEquip()) {
            return if (weaponId < playerInventory.size) {
                gameState.equipWeapon(
                    playerCharacters[activeCharacterIndex],
                    playerInventory[weaponId]
                )
            } else {
                gameState.equipWeapon(
                    playerCharacters[activeCharacterIndex],
                    playerCharacters[activeCharacterIndex].equippedWeapon
                )
            }
        } else {
            throw IllegalStateActionException("equip a weapon", gameState::class.simpleName!!)
        }
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
     *
     * @return whether the engineer was created or not
     */
    fun createEngineer(name: String): Boolean {
        return if (playerCharacters.size < 5) {
            val character = gameState.createEngineer(name, turnsQueue)
            val weapon = Bow("BasicBow", 50, 25)
            character.equip(weapon)
            playerCharacters.add(
                character
            )
            true
        } else {
            false
        }
    }

    /**
     * Creates a new knight
     */
    fun createKnight(name: String): Boolean {
        return if (playerCharacters.size < 5) {
            val character = gameState.createKnight(name, turnsQueue)
            val weapon = Sword("BasicSword", 60, 30)
            character.equip(weapon)
            playerCharacters.add(
                character
            )
            true
        } else {
            false
        }
    }
    /**
     * Creates a new thief
     */
    fun createThief(name: String): Boolean {
        return if (playerCharacters.size < 5) {
            val character = gameState.createThief(name, turnsQueue)
            val weapon = Knife("BasicKnife", 50, 22)
            character.equip(weapon)
            playerCharacters.add(
                character
            )
            true
        } else {
            false
        }
    }
    /**
     * Creates a new white mage
     */
    fun createWhiteMage(name: String): Boolean {
        return if (playerCharacters.size < 5) {
            val character = gameState.createWhiteMage(name, turnsQueue)
            val weapon = Staff("BasicWand", 15, 21, 25)
            character.equip(weapon)
            playerCharacters.add(
                character
            )
            true
        } else {
            false
        }
    }
    /**
     * Creates a new black mage
     */
    fun createBlackMage(name: String): Boolean {
        return if (playerCharacters.size < 5) {
            val character = gameState.createBlackMage(name, turnsQueue)
            val weapon = Staff("BasicStaff", 20, 24, 30)
            character.equip(weapon)
            playerCharacters.add(
                character
            )
            true
        } else {
            false
        }
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
        if (gameState.isMagicalPlayerTurn()) {
            val spells = (playerCharacters[activeCharacterIndex] as Mage).getSpells()
            val viewData = mutableListOf<Triple<String, Int, String>>()
            for (spell in spells) {
                val info = Triple(spell::class.simpleName!!, spell.cost, spell.debuff::class.simpleName!!)
                viewData.add(info)
            }
            return viewData.toList()
        } else {
            throw IllegalStateActionException("check the available spells", gameState::class.simpleName!!)
        }
    }

    /**
     * Function to get the player characters' active mp
     * @return a pair containing the current and max Mp of the characters
     */
    fun getCharacterMp(): List<Pair<Int, Int>> {
        val mpValues = mutableListOf<Pair<Int, Int>>()
        for (character in playerCharacters) {
            if (!character.isMage()) {
                mpValues.add(Pair(0, 0))
            } else {
                character as Mage
                mpValues.add(Pair(character.currentMp, character.maxMp))
            }
        }
        return mpValues.toList()
    }
    fun startBattle(): Boolean {
        return if (gameState.isCharacterCreation() || gameState.isEnemyGeneration()) {
            if (playerCharacters.size == 5) {
                gameState.toTurnWait()
                for (character in playerCharacters) {
                    character.waitTurn()
                }
                for (enemy in enemyCharacters) {
                    enemy.waitTurn()
                }
                true
            } else {
                false
            }
        } else {
            false
        }
    }
    /**
     * Function called to take turns
     */
    fun nextTurn() {
        val character: GameCharacter = gameState.nextTurn(turnsQueue)
        val canAct = character.rollEffects()
        if (canAct) {
            activeCharacterIndex = playerCharacters.indexOf(character)
            if (character.isMage()) {
                gameState.toMagicalPlayerTurn()
            } else if (character.isPlayerCharacter()) {
                gameState.toNonMagicalPlayerTurn()
            } else {
                gameState.toEnemyTurn()
                enemyTurn(character)
            }
        } else {
            advanceTurn(character)
            gameState.toEndCheck()
            activeCharacterIndex = -1
        }
    }

    /**
     * To generate a new enemy and add it to the enemies' side
     */
    fun generateEnemy(amount: Int) {
        if (gameState.isEnemyGeneration() || gameState.isCharacterCreation()) {
            enemyCharacters.clear()
            for (i in 1..amount) {
                val newEnemy = Enemy(
                    enemyNameList[ControllerRNGSeed.seed.nextInt(0, enemyNameList.size)],
                    ControllerRNGSeed.seed.nextInt(60, 100),
                    ControllerRNGSeed.seed.nextInt(15, 45),
                    ControllerRNGSeed.seed.nextInt(300, 600),
                    ControllerRNGSeed.seed.nextInt(5, 20),
                    turnsQueue
                )
                enemyCharacters.add(newEnemy)
            }
        } else throw IllegalStateActionException("generate enemies", gameState::class.simpleName!!)
    }
    /**
     * Public function to issue an attack
     * @param enemyId the index of the enemy character on the enemy character list
     */
    fun attack(enemyId: Int): Int {
        if (activeCharacterIndex != -1) {
            val damage =
                gameState.attack(playerCharacters[activeCharacterIndex], enemyCharacters[enemyId])
            // Turn does not advance if the attack was not executed
            if (damage != -1) advanceTurn(playerCharacters[activeCharacterIndex])
            return damage
        } else {
            throw IllegalStateActionException("issue an attack command", this.gameState::class.simpleName!!)
        }
    }
    /**
     * Issues a cast command to the caster given by [activeCharacterIndex] unto a target character
     * @param spellId index of the spell on the caster's spell list
     * @param enemyTarget whether this spell targets enemies or allies
     * @return a pair with the damage dealt and debuff if any
     */
    fun useMagic(spellId: Int, targetId: Int, enemyTarget: Boolean): Pair<Int, String> {
        if (activeCharacterIndex != -1) {
            val attacker = playerCharacters[activeCharacterIndex] as Mage
            val spellList = attacker.getSpells()
            val target = if (enemyTarget) enemyCharacters[targetId] else playerCharacters[targetId]
            attacker.setSpell(spellList[spellId])
            val (damage, debuff) = gameState.useMagic(attacker, target)
            if (gameState.isEndCheck()) advanceTurn(attacker)
            return Pair(damage, debuff::class.simpleName!!)
        } else {
            throw IllegalStateActionException("use magic", this.gameState::class.simpleName!!)
        }
    }

    /**
     * Function to end any turn
     * @param character the character whose turn is ending
     */
    private fun advanceTurn(character: GameCharacter) {
        turnsQueue.poll()
        waitTurn(character)
        // The character is not active anymore
        activeCharacterIndex = -1
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
    private fun enemyTurn(character: GameCharacter) {
        if (gameState.isEnemyTurn()) {
            var attackDone = false
            while (!attackDone) {
                val k = Random.nextInt(0, 5)
                if (playerCharacters[k].currentHp != 0) {
                    gameState.enemyAttack(character, playerCharacters[k])
                    attackDone = true
                }
            }
            advanceTurn(character)
        } else {
            throw IllegalStateActionException("make an enemy attack", gameState::class.simpleName!!)
        }
    }

    /**
     * Function to handle the user's victory, it gives the user a new weapon and heals characters
     * if they want to continue playing
     * @param nextBattle whether the user wants to create another battle
     */
    fun onPlayerWin(nextBattle: Boolean) {
        gameState.onPlayerWin(nextBattle)
        if (nextBattle) {
            gameIsOver = false
            playerWin = false
            turnsQueue.clear()
            for (index in playerCharacters.indices) {
                // We don't want to revive dead characters, or else
                // the game could stretch for a bit too much
                if (playerCharacters[index].currentHp != 0) {
                    playerCharacters[index].receiveHealing(ceil(playerCharacters[index].maxHp / 10f).toInt())
                    if (playerCharacters[index].isMage()) {
                        val asMage = playerCharacters[index] as Mage
                        (playerCharacters[index] as Mage).restoreMp(ceil(asMage.maxMp / 10f).toInt())
                    }
                }
            }
            val enemyAmount = ControllerRNGSeed.seed.nextInt(1, 6)
            generateEnemy(enemyAmount)
            startBattle()
        }
    }

    /**
     * Function to handle enemy victory
     * @param nextGame whether the user wants to play again
     */
    fun onEnemyWin(nextGame: Boolean) {
        gameState.onEnemyWin(nextGame)
        if (nextGame) {
            gameIsOver = false
            playerWin = false
            enemyCharacters.clear()
            turnsQueue.clear()
            playerCharacters.clear()
            val inventory = PlayerCharacter.getInventory()
            for (weapon in inventory) {
                PlayerCharacter.discardWeaponFromInventory(weapon)
            }
            PlayerCharacter.addWeaponToInventory(Axe("BasicAxe", 80, 40))
            val enemyAmount = ControllerRNGSeed.seed.nextInt(0, 6)
            generateEnemy(enemyAmount)
        }
    }

    /**
     * Function to check whether the game has finished
     */
    fun isGameOver(): Boolean {
        if (gameState.isEndCheck()) {
            val playerDead = oneSideDead(playerCharacters)
            if (playerDead) {
                gameIsOver = true
                gameState.toPlayerDefeated()
                playerWin = false
            }
            val enemyDead = oneSideDead(enemyCharacters)
            if (enemyDead) {
                gameIsOver = true
                gameState.toEnemyDefeated()
                playerWin = true
            }
            if (!gameIsOver) gameState.toTurnWait()
            return gameIsOver
        } else {
            throw IllegalStateActionException("check the game's end", gameState::class.simpleName!!)
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
}
