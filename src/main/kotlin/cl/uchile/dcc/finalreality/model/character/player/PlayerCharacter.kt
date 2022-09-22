/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player

import cl.uchile.dcc.finalreality.model.character.player.weapon.Weapon

/**
 * This represents a character from the game that is controlled by the player
 *
 * @property equippedWeapon the weapon that the character is currently using
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface PlayerCharacter {
    val equippedWeapon: Weapon
    /**
     * Makes a request to the [weapon] to know how to equip said [Weapon]
     * @param weapon to equip
     */
    fun equip(weapon: Weapon)
}
