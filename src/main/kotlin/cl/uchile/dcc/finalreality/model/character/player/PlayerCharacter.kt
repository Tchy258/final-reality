/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player

import cl.uchile.dcc.finalreality.model.character.AbstractCharacter
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * A character controlled by the user.
 *
 * @property equippedWeapon the weapon that the character is currently using
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface PlayerCharacter {
    val equippedWeapon: Weapon
    /**
     * Equips a [Weapon] to a [PlayerCharacter] if said character can do so
     * @return true if [weapon] was equipped
     */
    fun equip(weapon: Weapon): Boolean
    /**
     * Equips an [Axe] to a [PlayerCharacter]
     * @return true if [axe] was equipped
     */
    fun equipAxe(axe: Axe): Boolean
    /**
     * Equips a [Bow] to a [PlayerCharacter]
     * @return true if [bow] was equipped
     */
    fun equipBow(bow: Bow): Boolean
    /**
     * Equips a [Knife] to a [PlayerCharacter]
     * @return true if [knife] was equipped
     */
    fun equipKnife(knife: Knife): Boolean
    /**
     * Equips a [Staff] to a [PlayerCharacter]
     * @return true if [staff] was equipped
     */
    fun equipStaff(staff: Staff): Boolean
    /**
     * Equips a [Sword] to a [PlayerCharacter]
     * @return true if [sword] was equipped
     */
    fun equipSword(sword: Sword): Boolean
}
/**
 * A class that holds all the information of a player-controlled character in the game.
 *
 * @param name the character's name
 * @param maxHp the character's maximum health points
 * @param defense the character's defense
 * @param turnsQueue the queue with the characters waiting for their turn
 * @constructor Creates a new playable character.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
abstract class AbstractPlayerCharacter(
    name: String,
    maxHp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractCharacter(name, maxHp, defense, turnsQueue),
    PlayerCharacter {
    private lateinit var _equippedWeapon: Weapon
    override val equippedWeapon: Weapon
        get() = _equippedWeapon

    override fun equip(weapon: Weapon): Boolean {
        return weapon.equipWeapon(this)
    }
    /**
     * Setter for the [equippedWeapon] once the character knows if it can equip it
     */
    protected fun setWeapon(weapon: Weapon) {
        _equippedWeapon = weapon
    }
    abstract override fun equipAxe(axe: Axe): Boolean
    abstract override fun equipBow(bow: Bow): Boolean
    abstract override fun equipKnife(knife: Knife): Boolean
    abstract override fun equipStaff(staff: Staff): Boolean
    abstract override fun equipSword(sword: Sword): Boolean
    override fun waitTurn() {
        if (::_equippedWeapon.isInitialized) {
            scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
            scheduledExecutor.schedule(
                /* command = */ ::addToQueue,
                /* delay = */ (this.equippedWeapon.weight * 100).toLong(),
                /* unit = */ TimeUnit.MILLISECONDS
            )
        } else {
            println("$name has no weapon equipped, $name can't fight")
        }
    }
}
