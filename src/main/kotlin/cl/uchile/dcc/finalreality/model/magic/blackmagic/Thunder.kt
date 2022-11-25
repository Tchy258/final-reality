package cl.uchile.dcc.finalreality.model.magic.blackmagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
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
    override fun castBlackMagic(magicDamage: Int, spellTarget: GameCharacter): Boolean {
        var activated = false
        val k: Double = RNGSeeder.seed.nextDouble()
        if (k <= 0.3) {
            activated = true
            spellTarget.addDebuff(Paralyzed())
        }
        spellTarget.receiveMagicDamage(magicDamage)
        return activated
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

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
