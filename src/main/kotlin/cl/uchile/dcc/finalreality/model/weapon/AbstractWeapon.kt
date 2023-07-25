/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.exceptions.Require

/**
 * A class that holds all the information of a weapon.
 *
 * @property name the name of the weapon.
 * @property damage the base damage done by the weapon.
 * @property weight the weight of the weapon.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
abstract class AbstractWeapon(
    override val name: String,
    damage: Int,
    weight: Int
) : Weapon {
    override val damage: Int = Require.Stat(damage, "Damage ") atLeast 0
    override val weight: Int = Require.Stat(weight, "Weight ") atLeast 1
}
