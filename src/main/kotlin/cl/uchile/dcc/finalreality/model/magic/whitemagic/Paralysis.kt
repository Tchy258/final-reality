package cl.uchile.dcc.finalreality.model.magic.whitemagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Paralyzed

/**
 * This represents the Paralysis spell, which always paralyzes a target
 * @property cost the cost of this spell, which is always `25`
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Paralysis : WhiteMagic {
    override val cost: Int
        get() = 25

    override fun castWhiteMagic(magicDamage: Int, spellTarget: GameCharacter): Boolean {
        spellTarget.addDebuff(Paralyzed())
        return true
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
