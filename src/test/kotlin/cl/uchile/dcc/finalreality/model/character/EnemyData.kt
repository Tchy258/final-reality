package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.model.ModelData
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
/**
 * Class that contains an [Enemy]'s data.
 * This class should only be used for testing
 *
 * @property name the enemy's name
 * @property damage the enemy's damage
 * @property weight the enemy's weight
 * @property maxHp the enemy's maxHp
 * @property defense the enemy's defense
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
internal data class EnemyData(val name: String, val damage: Int, val weight: Int, val maxHp: Int, val defense: Int) : ModelData {
    override val validGenerator: Arb<EnemyData> = EnemyData.validGenerator
    override val arbitraryGenerator: Arb<EnemyData> = EnemyData.arbitraryGenerator
    companion object {
        val validGenerator: Arb<EnemyData> = arbitrary {
            val name = Arb.string().bind()
            val damage = Arb.nonNegativeInt().bind()
            val weight = Arb.positiveInt().bind()
            val maxHp = Arb.positiveInt().bind()
            val defense = Arb.nonNegativeInt().bind()
            EnemyData(name, damage, weight, maxHp, defense)
        }
        val arbitraryGenerator: Arb<EnemyData> = arbitrary {
            val name = Arb.string().bind()
            val damage = Arb.int().bind()
            val weight = Arb.int().bind()
            val maxHp = Arb.int().bind()
            val defense = Arb.int().bind()
            EnemyData(name, damage, weight, maxHp, defense)
        }
    }
}
