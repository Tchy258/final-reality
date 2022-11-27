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
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

interface Magic {
    val cost: Int
    /**
     * Cast this magic spell onto a [spellTarget] with a [magicDamage] value,
     * applying a [Debuff] if applicable
     * @return the spell's adverse effect or null if it didn't activate it
     */
    fun cast(magicDamage: Int, spellTarget: GameCharacter): Debuff?
    /**
     * Cast this black magic spell onto a [spellTarget] with a [magicDamage] value,
     * applying a [Debuff] if applicable
     * @return the spell's adverse effect or null if it didn't activate it
     */
    fun castBlackMagic(magicDamage: Int, spellTarget: GameCharacter): Debuff?
    /**
     * Cast this white magic spell onto a [spellTarget] with a [magicDamage] value,
     * applying a [Debuff] if applicable
     * @return the spell's adverse effect or null if it didn't activate it
     */
    fun castWhiteMagic(magicDamage: Int, spellTarget: GameCharacter): Debuff?
}
