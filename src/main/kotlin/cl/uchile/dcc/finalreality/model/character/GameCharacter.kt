/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.model.character.debuff.Burned
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.debuff.Paralyzed
import cl.uchile.dcc.finalreality.model.character.debuff.Poisoned
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter

/**
 * This represents a character from the game.
 * A character can be controlled by the player or by the CPU (an enemy).
 *
 * @property name the name of the character.
 * @property maxHp the maximum health points of the character.
 * @property defense the defense of the character.
 * @property currentHp the current health points of the character.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface GameCharacter {
    val name: String
    val maxHp: Int
    val currentHp: Int
    val defense: Int
    /**
     * Sets a scheduled executor to make this character (thread) wait for `speed / 10`
     * seconds (that is the same as `speed * 100` milliseconds) before adding the character to the queue.
     */
    fun waitTurn()

    /**
     * Decrements this character's currentHp by receiving damage (mitigated by defense)
     */
    fun receiveAttack(damage: Int)
    /**
     * Reduces this character's hp ignoring its defense due to a magic spell or its effect
     */
    fun receiveMagicDamage(damage: Int)
    /**
     * Heals this character's currentHp increasing its value
     */
    fun receiveHealing(healing: Int)
    /**
     * Attacks a [GameCharacter], decreasing [anotherCharacter]'s [currentHp],
     *
     * @return the amount of damage dealt
     */
    fun attack(anotherCharacter: GameCharacter): Int
    /**
     * Adds a [Debuff] to this character
     */
    fun addDebuff(debuff: Debuff)
    /**
     * Removes a [Debuff] to this character
     */
    fun removeDebuff(debuff: Debuff)

    /**
     * @return whether the character is [Burned]
     */
    fun isBurned(): Boolean
    /**
     * @return whether the character is [Paralyzed]
     */
    fun isParalyzed(): Boolean
    /**
     * @return whether the character is [Poisoned]
     */
    fun isPoisoned(): Boolean
    /**
     * Activates adverse effects applied to this character
     * @return whether this character can attack or not (depending on if it's [Paralyzed] or died)
     */
    fun rollEffects(): Boolean

    /**
     * Returns true if this character is a player character
     */
    fun isPlayerCharacter(): Boolean {
        return false
    }
}
