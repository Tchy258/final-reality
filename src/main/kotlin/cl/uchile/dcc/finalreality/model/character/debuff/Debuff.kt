package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter

/**
 * This represents an adverse effect that can be applied to a [GameCharacter] due
 * to a spell cast on it
 */
interface Debuff {
    /**
     * Activates this adverse effect on the [character]
     * @return whether this character can attack or not due to [Paralyzed]
     */
    fun rollEffect(character: GameCharacter): Boolean
}
