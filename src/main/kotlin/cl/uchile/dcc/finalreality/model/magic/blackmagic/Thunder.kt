/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.magic.blackmagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.debuff.NoDebuff
import cl.uchile.dcc.finalreality.model.character.debuff.Paralyzed

/**
 * This represents a Thunder spell castable by a BlackMage.
 * It has a chance of applying the [Paralyzed] adverse status effect.
 * @property cost the cost of this spell, which is always `15`
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Thunder : BlackMagic {
    override val cost: Int
        get() = 15
    override val debuff: Debuff
        get() = Paralyzed()
    override fun castBlackMagic(magicDamage: Int, spellTarget: GameCharacter): Debuff {
        var activated: Debuff = NoDebuff()
        val k: Double = MagicRNGSeeder.seed.nextDouble()
        if (k <= 0.3) {
            activated = debuff
            spellTarget.addDebuff(activated)
        }
        spellTarget.receiveMagicDamage(magicDamage)
        return activated
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (this.hashCode() != other.hashCode()) return false
        other as Thunder

        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Thunder { cost: $cost }"
    }
}
