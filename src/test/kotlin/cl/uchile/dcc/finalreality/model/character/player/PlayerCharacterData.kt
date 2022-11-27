package cl.uchile.dcc.finalreality.model.character.player

import cl.uchile.dcc.finalreality.model.GameCharacterData
import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter

internal interface PlayerCharacterData : GameCharacterData {
    override fun process(factory: CharacterTestingFactory): PlayerCharacter
}
