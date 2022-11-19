package cl.uchile.dcc.finalreality.model.magic.whitemagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Poisoned
import kotlin.math.ceil

class Poison : WhiteMagic {
    override val cost: Int
        get() = 40

    override fun castWhiteMagic(magicDamage: Int, spellTarget: GameCharacter) {
        val finalDamage = ceil(magicDamage.toDouble() * 3 / 10f).toInt()
        spellTarget.addDebuff(Poisoned(finalDamage))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Poison

        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        return cost
    }

    override fun toString(): String {
        return "Poison { cost: $cost }"
    }
}
