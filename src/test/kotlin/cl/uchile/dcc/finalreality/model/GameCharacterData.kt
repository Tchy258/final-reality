package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import io.kotest.property.Arb

/**
 * This interface encapsulates generators for testing data
 * (this data is supplied to testing factories)
 */
internal interface GameCharacterData {
    /**
     * An arb that creates valid model data
     */
    val validGenerator: Arb<GameCharacterData>

    /**
     * An arb that creates arbitrary model data
     */
    val arbitraryGenerator: Arb<GameCharacterData>

    fun process(factory: CharacterTestingFactory): GameCharacter
}
