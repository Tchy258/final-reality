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
import cl.uchile.dcc.finalreality.model.character.debuff.Paralyzed

/**
 * This represents the Paralysis spell, which always paralyzes a target
 * @property cost the cost of this spell, which is always `25`
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Paralysis : WhiteMagic {
    override val debuff: Debuff
        get() = Paralyzed()
    override val cost: Int
        get() = 25

    override fun castWhiteMagic(magicDamage: Int, spellTarget: GameCharacter): Debuff {
        spellTarget.addDebuff(Paralyzed())
        return debuff
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Paralysis
        if (hashCode() != other.hashCode()) return false
        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Paralysis { cost: $cost }"
    }
}
