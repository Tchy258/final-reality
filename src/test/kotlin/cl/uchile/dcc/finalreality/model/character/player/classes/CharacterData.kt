package cl.uchile.dcc.finalreality.model.character.player.classes

import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacterData
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string

/**
 * Class that contains an [AbstractPlayerCharacter]'s data.
 * This class should only be used for testing
 *
 * @property name the character's name
 * @property maxHp the character's maxHp
 * @property defense the character's defense
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
internal data class CharacterData(val name: String, val maxHp: Int, val defense: Int) : PlayerCharacterData {
    override val validGenerator: Arb<CharacterData> = CharacterData.validGenerator
    override val arbitraryGenerator: Arb<CharacterData> = CharacterData.arbitraryGenerator
    override fun process(factory: CharacterTestingFactory): PlayerCharacter {
        return factory.createCharacter(this)
    }

    companion object {
        val validGenerator: Arb<CharacterData> = arbitrary {
            val name = Arb.string().bind()
            val maxHp = Arb.positiveInt().bind()
            val defense = Arb.nonNegativeInt().bind()
            CharacterData(name, maxHp, defense)
        }
        val arbitraryGenerator: Arb<CharacterData> = arbitrary {
            val name = Arb.string().bind()
            val maxHp = Arb.int().bind()
            val defense = Arb.int().bind()
            CharacterData(name, maxHp, defense)
        }
    }
}
