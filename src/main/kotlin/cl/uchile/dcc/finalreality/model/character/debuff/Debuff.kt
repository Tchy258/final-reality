package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter

interface Debuff {
    fun rollEffect(character: GameCharacter): Boolean
}
