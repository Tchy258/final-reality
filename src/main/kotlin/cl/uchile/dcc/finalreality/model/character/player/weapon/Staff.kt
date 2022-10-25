/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.exceptions.Require
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import java.util.Objects
/**
 * A class that identifies a [Weapon] as a Staff, and tells whoever tries to equip it
 * to equip a staff.
 *
 * @param name the name of the weapon.
 * @param damage the base damage done by the weapon.
 * @param weight the weight of the weapon.
 * @property magicDamage the magicDamage of the staff.
 *
 * @constructor Creates a new staff.
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
class Staff(
    name: String,
    damage: Int,
    weight: Int,
    magicDamage: Int
) : AbstractWeapon(name, damage, weight) {
    val magicDamage: Int = Require.Stat(magicDamage, "Damage ") atLeast 0
    override fun equipToBlackMage(character: BlackMage) {
        character.equipStaff(this)
    }
    override fun equipToWhiteMage(character: WhiteMage) {
        character.equipStaff(this)
    }
    override fun hashCode() = Objects.hash(Staff::class, name, damage, weight, magicDamage)
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Staff -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        damage != other.damage -> false
        weight != other.weight -> false
        magicDamage != other.magicDamage -> false
        else -> true
    }
    override fun toString() = "Staff { name: '$name', damage: $damage, weight: $weight, magicDamage: $magicDamage }"
}
