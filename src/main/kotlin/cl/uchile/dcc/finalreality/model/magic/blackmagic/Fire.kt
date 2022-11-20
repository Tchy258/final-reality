package cl.uchile.dcc.finalreality.model.magic.blackmagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Burned
import kotlin.math.ceil

class Fire : BlackMagic {
    override val cost: Int
        get() = 15
    override fun castBlackMagic(magicDamage: Int, spellTarget: GameCharacter): Boolean {
        var activated = false
        val k: Double = RNGSeeder.seed.nextDouble()
        val finalDamage = ceil(magicDamage.toDouble() / 3f).toInt()
        if (k <= 0.2) {
            activated = true
            spellTarget.addDebuff(Burned(finalDamage))
        }
        spellTarget.receiveMagicDamage(magicDamage)
        return activated
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fire

        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        return cost
    }

    override fun toString(): String {
        return "Fire { cost: $cost }"
    }
}
