package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter

class Poisoned(private val finalDamage: Int) : Debuff {
    private var turnLimit = 5
    override fun rollEffect(character: GameCharacter): Boolean {
        character.receiveMagicDamage(finalDamage)
        if (turnLimit != 0) {
            turnLimit--
        } else {
            character.removeDebuff(this)
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Poisoned

        if (finalDamage != other.finalDamage) return false
        if (turnLimit != other.turnLimit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = finalDamage
        result = 31 * result + turnLimit
        return result
    }

    override fun toString(): String {
        return "Poisoned { finalDamage: $finalDamage, turnLimit: $turnLimit }"
    }
}
