/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import java.util.Objects
/**
 * A class that identifies a [Weapon] as an Axe, and tells whoever tries to equip it
 * to equip an axe.
 *
 * @param name the name of the weapon.
 * @param damage the base damage done by the weapon.
 * @param weight the weight of the weapon.
 *
 * @constructor Creates a new axe.
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
class Axe(
    name: String,
    damage: Int,
    weight: Int
) : AbstractWeapon(name, damage, weight) {
    override fun equipToEngineer(character: Engineer) {
        character.equipAxe(this)
    }
    override fun equipToKnight(character: Knight) {
        character.equipAxe(this)
    }
    override fun hashCode() = Objects.hash(Axe::class, name, damage, weight)
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Axe -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        damage != other.damage -> false
        weight != other.weight -> false
        else -> true
    }
    override fun toString() = "Axe { name: '$name', damage: $damage, weight: $weight }"
}
