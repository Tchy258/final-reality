package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacterData
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string

/**
 * Class that contains an [AbstractMage]'s data.
 * This class should only be used for testing
 *
 * @property name the mage's name
 * @property maxHp the mage's maxHp
 * @property maxMp the mage's maxMp
 * @property defense the mage's defense
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
internal data class MageData(val name: String, val maxHp: Int, val maxMp: Int, val defense: Int) : PlayerCharacterData {
    override val validGenerator = MageData.validGenerator
    override val arbitraryGenerator = MageData.arbitraryGenerator
    override fun process(factory: CharacterTestingFactory): Mage {
        return factory.createMage(this)
    }

    companion object {
        val validGenerator = arbitrary {
            val name = Arb.string().bind()
            val maxHp = Arb.positiveInt().bind()
            val maxMp = Arb.positiveInt().bind()
            val defense = Arb.nonNegativeInt().bind()
            MageData(name, maxHp, maxMp, defense)
        }
        val arbitraryGenerator = arbitrary {
            val name = Arb.string().bind()
            val maxHp = Arb.int().bind()
            val maxMp = Arb.int().bind()
            val defense = Arb.int().bind()
            MageData(name, maxHp, maxMp, defense)
        }
    }
}
