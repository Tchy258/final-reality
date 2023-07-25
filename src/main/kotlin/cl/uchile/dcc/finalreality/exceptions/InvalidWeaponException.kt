/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.exceptions

import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.weapon.Weapon

/**
 * This error is used to represent an illegal weapon equip
 *
 * @constructor Creates a new [InvalidWeaponException] with the [PlayerCharacter]
 * that tried to equip a [Weapon] it shouldn't
 */
class InvalidWeaponException(character: String, weapon: String) :
    Exception("Attempted to equip a $weapon to a $character")
