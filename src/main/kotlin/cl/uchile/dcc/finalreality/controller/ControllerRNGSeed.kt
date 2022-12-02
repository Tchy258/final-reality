package cl.uchile.dcc.finalreality.controller

import kotlin.random.Random

/**
 * Singleton object to have a unique instance of a seed for character creation stat rolls
 *
 * @property seed the RNG seed for stat rolls
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */

object ControllerRNGSeed {
    /**
     * Changes the stat roll seed
     */
    fun setSeed(seed: Int) {
        realSeed = Random(seed)
    }
    private var realSeed: Random = Random.Default
    val seed: Random
        get() = realSeed
}
