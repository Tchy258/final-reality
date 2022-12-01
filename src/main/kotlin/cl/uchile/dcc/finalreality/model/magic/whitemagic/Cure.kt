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
import kotlin.math.ceil

/**
 * This represents the Cure spell, which heals 30% of a character's maxHp when cast
 * @property cost the cost of this spell, which is always `15`
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Cure : WhiteMagic {
    override val cost: Int
        get() = 15

    override fun castWhiteMagic(magicDamage: Int, spellTarget: GameCharacter): Debuff {
        // maxHp * 3/10 == 30% of maxHp
        spellTarget.receiveHealing(ceil(spellTarget.maxHp.toDouble() * 3 / 10f).toInt())
        return debuff
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cure
        if (hashCode() != other.hashCode()) return false
        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Cure { cost: $cost }"
    }
}
