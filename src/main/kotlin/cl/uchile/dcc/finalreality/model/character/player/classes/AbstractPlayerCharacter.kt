/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.classes

import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.AbstractCharacter
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * A class that holds all the information of a player-controlled character in the game.
 *
 * @param name the character's name.
 * @param maxHp the character's maximum health points.
 * @param defense the character's defense.
 * @param turnsQueue the queue with the characters waiting for their turn.
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
    /**
     * Setter for the [equippedWeapon] once the character knows if it can equip it
     */
    protected fun setWeapon(weapon: Weapon) {
        _equippedWeapon = weapon
    }
    override fun waitTurn() {
        if (::_equippedWeapon.isInitialized) {
            scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
            scheduledExecutor.schedule(
                /* command = */ ::addToQueue,
                /* delay = */ (this.equippedWeapon.weight * 100).toLong(),
                /* unit = */ TimeUnit.MILLISECONDS
            )
        } else {
            throw NoWeaponEquippedException(this.name)
        }
    }

    override fun attack(anotherCharacter: GameCharacter) {
        if (::_equippedWeapon.isInitialized) {
            anotherCharacter.receiveAttack(_equippedWeapon.damage)
        }
        else {
            throw NoWeaponEquippedException(this.name)
        }
    }
}
