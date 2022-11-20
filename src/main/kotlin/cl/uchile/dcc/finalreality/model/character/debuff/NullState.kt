package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter

/**
 * A dummy adverse effect which implies no status effect at all
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class NullState : Debuff {
    override fun rollEffect(character: GameCharacter): Boolean {
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "NullState"
    }
}
