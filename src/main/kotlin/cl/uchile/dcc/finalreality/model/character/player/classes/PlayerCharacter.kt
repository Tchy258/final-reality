/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.classes

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.weapon.Weapon

/**
 * A character controlled by the user.
 *
 * @property equippedWeapon the weapon that the character is currently using
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface PlayerCharacter: GameCharacter {
    override val name: String
    val equippedWeapon: Weapon
    /**
     * Equips a weapon to the character.
     * @param weapon the weapon this character equips
     */
    fun equip(weapon: Weapon)
}
