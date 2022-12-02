/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.classes

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Weapon

/**
 * A character controlled by the user.
 *
 * @property equippedWeapon the weapon that the character is currently using
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface PlayerCharacter : GameCharacter {
    override val name: String
    val equippedWeapon: Weapon
    /**
     * Equips a weapon to the character.
     * @param weapon the weapon this character equips
     */
    fun equip(weapon: Weapon)

    /**
     * This companion object is a container for static methods that
     * interact with the PlayerCharacters' inventory
     */
    companion object Inventory {
        /**
         * The weapon inventory shared by all instances of PlayerCharacter
         */
        private val weaponInventory: MutableList<Weapon> = mutableListOf(
            Axe("BasicAxe", 60, 40)
        )

        /**
         * Getter for the weapon inventory shared by PlayerCharacters
         * @return the characters' weapon inventory as an immutable list
         */
        @JvmStatic
        fun getInventory(): List<Weapon> {
            return weaponInventory.toList()
        }

        /**
         * Adds a weapon to the characters' inventory.
         * This weapon might be new or a weapon that was previously equipped and
         * was changed for another weapon
         * @param weapon the weapon to add to the inventory
         */
        @JvmStatic
        fun addWeaponToInventory(weapon: Weapon) {
            weaponInventory.add(weapon)
        }

        /**
         * Discards a weapon, if it is in the inventory
         * @param weapon the weapon to discard
         * @return whether the weapon in the inventory was discarded or not
         */
        @JvmStatic
        fun discardWeaponFromInventory(weapon: Weapon): Boolean {
            return weaponInventory.remove(weapon)
        }
    }
}
