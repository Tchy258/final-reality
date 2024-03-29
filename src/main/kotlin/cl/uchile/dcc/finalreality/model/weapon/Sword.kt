/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import java.util.Objects

/**
 * A class that identifies a [Weapon] as a Sword, and tells whoever tries to equip it
 * to equip a sword.
 *
 * @param name the name of the weapon.
 * @param damage the base damage done by the weapon.
 * @param weight the weight of the weapon.
 *
 * @constructor Creates a new sword.
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
class Sword(
    name: String,
    damage: Int,
    weight: Int
) : AbstractWeapon(name, damage, weight) {
    override fun equipToKnight(character: Knight) {
        character.equipSword(this)
    }
    override fun equipToThief(character: Thief) {
        character.equipSword(this)
    }
    override fun hashCode() = Objects.hash(Sword::class, name, damage, weight)
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Sword -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        damage != other.damage -> false
        weight != other.weight -> false
        else -> true
    }
    override fun toString() = "Sword { name: '$name', damage: $damage, weight: $weight }"
}
