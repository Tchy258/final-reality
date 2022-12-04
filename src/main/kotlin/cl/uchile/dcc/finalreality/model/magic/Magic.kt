/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.magic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff

/**
 * Generic interface to represent any type of magic
 *
 * @property cost the spell's mana points cost
 * @property debuff the spell's possible adverse effect
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

interface Magic {
    val cost: Int
    val debuff: Debuff
    /**
     * Cast this magic spell onto a [spellTarget],
     * applying a [Debuff] if applicable
     * @return the spell's adverse effect
     */
    fun cast(spellTarget: GameCharacter): Debuff
    /**
     * Cast this black magic spell onto a [spellTarget],
     * applying a [Debuff] if applicable
     * @return the spell's adverse effect
     */
    fun castBlackMagic(spellTarget: GameCharacter): Debuff
    /**
     * Cast this white magic spell onto a [spellTarget],
     * applying a [Debuff] if applicable
     * @return the spell's adverse effect or null if it didn't activate it
     */
    fun castWhiteMagic(spellTarget: GameCharacter): Debuff
}
