package cl.uchile.dcc.finalreality.model.magic.whitemagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import kotlin.math.ceil

class Cure : WhiteMagic {
    override val cost: Int
        get() = 15

    override fun castWhiteMagic(magicDamage: Int, spellTarget: GameCharacter): Boolean {
        // maxHp * 3/10 == 30% of maxHp
        spellTarget.receiveHealing(ceil(spellTarget.maxHp.toDouble() * 3 / 10f).toInt())
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cure

        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        return cost
    }

    override fun toString(): String {
        return "Cure { cost: $cost }"
    }
}
