/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.magic.blackmagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Burned
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.debuff.NoDebuff
import kotlin.math.ceil

/**
 * This represents a Fire spell castable by a BlackMage.
 * It has a chance of applying the [Burned] adverse status effect
 * @property cost the cost of this spell, which is always `15`
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Fire(private val mageMagicDamage: Int) : BlackMagic {
    override val cost: Int = 15
    private val finalDamage = ceil(mageMagicDamage.toDouble() / 3f).toInt()
    override val debuff: Debuff
        get() = Burned(finalDamage)
    override fun castBlackMagic(spellTarget: GameCharacter): Debuff {
        val k: Double = MagicRNGSeeder.seed.nextDouble()
        spellTarget.receiveMagicDamage(mageMagicDamage)
        return if (k <= 0.2) {
            spellTarget.addDebuff(debuff)
            debuff
        } else NoDebuff()
    }

    override fun toString(): String {
        return "Fire { cost: $cost }"
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (this.hashCode() != other.hashCode()) return false

        other as Fire

        if (cost != other.cost) return false

        return true
    }
}
