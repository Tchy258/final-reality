package cl.uchile.dcc.finalreality.model.character.player.classes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string

/**
 * Class that contains a [PlayerCharacter]'s data
 * @property name the character's name
 * @property maxHp the character's maxHp
 * @property maxMp the character's maxMp
 * @property defense the character's defense
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
data class CharacterData(val name: String, val maxHp: Int, val maxMp: Int, val defense: Int) {
    companion object {
        /**
         * [Arb] generator for arbitrary valid [PlayerCharacter] data.
         */
        val validCharacterGenerator = arbitrary {
            val name = Arb.string().bind()
            val maxHp = Arb.positiveInt().bind()
            val maxMp = Arb.positiveInt().bind()
            val defense = Arb.nonNegativeInt().bind()
            CharacterData(name, maxHp, maxMp, defense)
        }

        /**
         * [Arb] generator for arbitrary [PlayerCharacter] data, the generated characters might not be valid.
         */
        val arbitraryCharacterGenerator = arbitrary {
            val name = Arb.string().bind()
            val maxHp = Arb.int().bind()
            val maxMp = Arb.int().bind()
            val defense = Arb.int().bind()
            CharacterData(name, maxHp, maxMp, defense)
        }
    }
}
