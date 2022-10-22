/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.exceptions

import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter

/**
 * This error is used to represent an illegal enqueue action
 *
 * @constructor Creates a new [NoWeaponEquippedException] with the [PlayerCharacter]
 * who tried to enqueue itself unarmed.
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

class NoWeaponEquippedException(aCharacter: String) :
    Exception("Attempted to join turn queue unarmed. $aCharacter has no weapon equipped")
