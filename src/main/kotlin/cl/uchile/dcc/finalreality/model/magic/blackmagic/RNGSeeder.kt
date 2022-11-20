package cl.uchile.dcc.finalreality.model.magic.blackmagic

import kotlin.random.Random

/**
 * Singleton object to have a unique instance of a seed for adverse effect landing calculations
 *
 * @property seed the adverse effect landing chance RNG seed
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
object RNGSeeder {
    /**
     * Changes the adverse effect RNGSeed
     */
    fun setSeed(seed: Int) {
        realSeed = Random(seed)
    }
    private var realSeed: Random = Random.Default
    val seed: Random
        get() = realSeed
}
