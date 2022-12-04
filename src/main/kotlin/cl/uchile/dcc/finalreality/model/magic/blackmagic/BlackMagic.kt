/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.magic.blackmagic

import cl.uchile.dcc.finalreality.exceptions.InvalidSpellCastException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.magic.Magic

/**
 * This represents black magic spells
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */

interface BlackMagic : Magic {
    override fun cast(spellTarget: GameCharacter): Debuff = castBlackMagic(spellTarget)
    override fun castBlackMagic(spellTarget: GameCharacter): Debuff
    override fun castWhiteMagic(spellTarget: GameCharacter): Debuff = throw InvalidSpellCastException("BlackMage", this::class.simpleName!!)
}
