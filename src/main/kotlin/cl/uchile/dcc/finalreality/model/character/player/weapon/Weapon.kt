/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.player.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.Engineer
import cl.uchile.dcc.finalreality.model.character.player.Knight
import cl.uchile.dcc.finalreality.model.character.player.Thief
import cl.uchile.dcc.finalreality.model.character.player.WhiteMage

/**
 * This represents any generic weapon.
 *
 * @property name the name of the weapon.
 * @property damage the base damage done by the weapon.
 * @property weight the weight of the weapon.
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface Weapon {
    val name: String
    val damage: Int
    val weight: Int

    /**
     * Equips this [Weapon] to a [BlackMage].
     * By default, it throws an [InvalidWeaponException].
     * Only weapons usable by black mages can override this method.
     * @param character the [BlackMage] this [Weapon] will be equipped to
     */
    fun equipTo(character: BlackMage) { throw InvalidWeaponException(character::class.simpleName!!, this::class.simpleName!!) }
    /**
     * Equips this [Weapon] to an [Engineer].
     * By default, it throws an [InvalidWeaponException].
     * Only weapons usable by engineers can override this method.
     * @param character the [Engineer] this [Weapon] will be equipped to
     */
    fun equipTo(character: Engineer) { throw InvalidWeaponException(character::class.simpleName!!, this::class.simpleName!!) }
    /**
     * Equips this [Weapon] to a [Knight].
     * By default, it throws an [InvalidWeaponException].
     * Only weapons usable by knights can override this method.
     * @param character the [Knight] this [Weapon] will be equipped to
     */
    fun equipTo(character: Knight) { throw InvalidWeaponException(character::class.simpleName!!, this::class.simpleName!!) }
    /**
     * Equips this [Weapon] to a [Thief].
     * By default, it throws an [InvalidWeaponException].
     * Only weapons usable by thieves can override this method
     * @param character the [Thief] this [Weapon] will be equipped to
     */
    fun equipTo(character: Thief) { throw InvalidWeaponException(character::class.simpleName!!, this::class.simpleName!!) }
    /**
     * Equips this [Weapon] to a [WhiteMage].
     * By default, it throws an [InvalidWeaponException].
     * Only weapons usable by white mages can override this method
     * @param character the [WhiteMage] this [Weapon] will be equipped to
     */
    fun equipTo(character: WhiteMage) { throw InvalidWeaponException(character::class.simpleName!!, this::class.simpleName!!) }
}
