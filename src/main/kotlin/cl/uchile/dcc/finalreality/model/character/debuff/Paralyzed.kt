package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter

class Paralyzed : Debuff {
    private var shouldActivate: Boolean = true
    override fun rollEffect(character: GameCharacter): Boolean {
        return if (shouldActivate) {
            shouldActivate = false
            false
        } else {
            character.removeDebuff(this)
            true
        }
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Paralyzed

        if (shouldActivate != other.shouldActivate) return false

        return true
    }
    override fun hashCode(): Int {
        return shouldActivate.hashCode()
    }
    override fun toString(): String {
        return "Paralyzed"
    }
}
