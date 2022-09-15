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

/**
 * A character controlled by the user.
 *
 * @property equippedWeapon
 *    the weapon that the character is currently using
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface PlayerCharacter {
    val equippedWeapon: Weapon
    /**
     * Equips a weapon to the character if it can be equipped by said character.
     */
    fun equip(weapon: Weapon)
}

/**
 *  A [PlayerCharacter] that can use a [Sword]
 *  @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface SwordUser

/**
 * A [PlayerCharacter] that can use an [Axe]
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface AxeUser
/**
 * A [PlayerCharacter] that can use a [Knife]
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface KnifeUser
/**
 * A [PlayerCharacter] that can use a [Staff]
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface StaffUser
/**
 * A [PlayerCharacter] that can use a [Bow]
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface BowUser
/**
 * A class that holds all the information of a player-controlled character in the game.
 *
 * @param name        the character's name
 * @param maxHp       the character's maximum health points
 * @param defense     the character's defense
 * @param turnsQueue  the queue with the characters waiting for their turn
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
) : AbstractCharacter(name, maxHp, defense, turnsQueue), PlayerCharacter {
    private lateinit var _equippedWeapon: Weapon
    override val equippedWeapon: Weapon
        get() = _equippedWeapon

    override fun equip(weapon: Weapon) {
        if (weapon.canEquip(this)) {
            _equippedWeapon = weapon
        } else {
            println("I can't equip that")
        }
    }
}
