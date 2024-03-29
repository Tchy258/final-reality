package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.exceptions.IllegalStateActionException
import cl.uchile.dcc.finalreality.exceptions.InvalidStateTransitionException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.Mage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.util.concurrent.LinkedBlockingQueue

/**
 * Interface that determines the current game state and game flow
 * @property controller the game controller that has this state
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

interface GameState {
    val controller: GameController

    /**
     * Helper methods to determine the state
     */
    fun isCharacterCreation(): Boolean = false
    fun isTurnWait(): Boolean = false
    fun isEnemyTurn(): Boolean = false
    fun isNonMagicalPlayerTurn(): Boolean = false
    fun isWeaponEquip(): Boolean = false
    fun isEndCheck(): Boolean = false
    fun isEnemyDefeated(): Boolean = false
    fun isEnemyGeneration(): Boolean = false
    fun isMagicalPlayerTurn(): Boolean = false
    fun isPlayerDefeated(): Boolean = false

    /**
     * Methods to transition between states, if a state cannot transition to another
     * due to how the game's logic works, an exception is thrown
     */
    fun toCharacterCreation() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "CharacterCreationState")
    }
    fun toTurnWait() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "TurnWaitState")
    }
    fun toEnemyTurn() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "EnemyTurnState")
    }
    fun toNonMagicalPlayerTurn() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "PlayerTurnState")
    }
    fun toWeaponEquip() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "WeaponEquipState")
    }
    fun toEnemyGeneration() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "EnemyGenerationState")
    }
    fun toEnemyDefeated() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "EnemyDefeatedState")
    }
    fun toPlayerDefeated() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "PlayerDefeatedState")
    }
    fun toMagicalPlayerTurn() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "MagicalPlayerTurnState")
    }
    fun toEndCheck() {
        throw InvalidStateTransitionException(this::class.simpleName!!, "EndCheckState")
    }
    /**
     * Function to swap a character's weapon
     * @param character the character who wants to swap weapons
     * @return whether the weapon could be equipped
     */
    fun equipWeapon(character: PlayerCharacter, weapon: Weapon): Boolean {
        throw IllegalStateActionException("equip a weapon", this::class.simpleName!!)
    }
    /**
     * Issue an attack command from [attacker] to [target]
     * @param attacker the character that attacks
     * @param target the character that receives the attack
     * @return the amount of damage dealt
     */
    fun attack(attacker: GameCharacter, target: GameCharacter): Int {
        throw IllegalStateActionException("issue an attack command", this::class.simpleName!!)
    }
    /**
     * Issue a spell cast from [attacker] to [target]
     * @param attacker the spell caster
     * @param target the character that receives the spell's effects
     */
    fun useMagic(attacker: Mage, target: GameCharacter): Pair<Int, Debuff> {
        throw IllegalStateActionException("issue a magic cast command", this::class.simpleName!!)
    }
    /**
     * Issue an attack command from [attacker] to [target] only meant to be used
     * on enemy turns
     * @param attacker the character that attacks
     * @param target the character that receives the attack
     * @return the amount of damage dealt
     */
    fun enemyAttack(attacker: GameCharacter, target: GameCharacter): Int {
        throw IllegalStateActionException("make an enemy attack", this::class.simpleName!!)
    }
    /**
     * Creates a new engineer
     * @param name the engineer's name
     * @param queue the game's turns queue
     * @return A randomly generated engineer
     */
    fun createEngineer(name: String, queue: LinkedBlockingQueue<GameCharacter>): Engineer {
        throw IllegalStateActionException("create an engineer", this::class.simpleName!!)
    }
    /**
     * Creates a new knight
     * @param name the knight's name
     * @param queue the game's turns queue
     * @return A randomly generated knight
     */
    fun createKnight(name: String, queue: LinkedBlockingQueue<GameCharacter>): Knight {
        throw IllegalStateActionException("create a knight", this::class.simpleName!!)
    }
    /**
     * Creates a new thief
     * @param name the thief's name
     * @param queue the game's turns queue
     * @return A randomly generated thief
     */
    fun createThief(name: String, queue: LinkedBlockingQueue<GameCharacter>): Thief {
        throw IllegalStateActionException("create a thief", this::class.simpleName!!)
    }
    /**
     * Creates a new black mage
     * @param name the black mage's name
     * @param queue the game's turns queue
     * @return A randomly generated black mage
     */
    fun createBlackMage(name: String, queue: LinkedBlockingQueue<GameCharacter>): BlackMage {
        throw IllegalStateActionException("create a black mage", this::class.simpleName!!)
    }
    /**
     * Creates a new white mage
     * @param name the white mage's name
     * @param queue the game's turns queue
     * @return A randomly generated white mage
     */
    fun createWhiteMage(name: String, queue: LinkedBlockingQueue<GameCharacter>): WhiteMage {
        throw IllegalStateActionException("create a white mage", this::class.simpleName!!)
    }
    /**
     * Function that returns [character]'s equipped weapon's
     * magic damage, or an exception if not on a mage turn
     *
     */
    fun getMagicDamage(character: PlayerCharacter): Int {
        throw IllegalStateActionException("get a magic weapon damage", this::class.simpleName!!)
    }
    /**
     * Function to wait for the next turn
     *
     * @param queue the [GameCharacter]s' turns queue
     */
    fun nextTurn(queue: LinkedBlockingQueue<GameCharacter>): GameCharacter {
        throw IllegalStateActionException("get a character from the queue", this::class.simpleName!!)
    }

    /**
     * Function meant to be executed once the player wins, if they choose to continue playing,
     * a new weapon will be added on their inventory and the player characters
     * will restore 10% of their Hp and Mp
     * @param nextBattle whether the user wishes to proceed to the next battle
     */
    fun onPlayerWin(nextBattle: Boolean) {
        throw IllegalStateActionException("grant the player a victory", this::class.simpleName!!)
    }
    /**
     * Function meant to be executed once the player loses
     */
    fun onEnemyWin(nextGame: Boolean) {
        throw IllegalStateActionException("grant the enemy a victory", this::class.simpleName!!)
    }
}
