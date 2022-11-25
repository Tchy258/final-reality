/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.magic.whitemagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.magic.Magic

/**
 * This represents white magic spells
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface WhiteMagic : Magic {
    override fun cast(magicDamage: Int, spellTarget: GameCharacter): Boolean = castWhiteMagic(magicDamage, spellTarget)
    /**
     * Casts this spell targeting a [spellTarget], activating its effect
     * and applying [Debuff]s (if any)
     * @return whether the spell activated its adverse effect
     */
    fun castWhiteMagic(magicDamage: Int, spellTarget: GameCharacter): Boolean
}
