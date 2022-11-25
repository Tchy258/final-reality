/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.magic.whitemagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Poisoned
import kotlin.math.ceil

/**
 * This represents the Poison spell, which inflicts the Poisoned adverse effect onto a character
 * @property cost the cost of this spell, which is always `40`
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Poison : WhiteMagic {
    override val cost: Int
        get() = 40

    override fun castWhiteMagic(magicDamage: Int, spellTarget: GameCharacter): Boolean {
        val finalDamage = ceil(magicDamage.toDouble() / 3).toInt()
        spellTarget.addDebuff(Poisoned(finalDamage))
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Poison
        if (hashCode() != other.hashCode()) return false
        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Poison { cost: $cost }"
    }
}
