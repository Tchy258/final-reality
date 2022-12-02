/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.magic.blackmagic

import kotlin.random.Random

/**
 * Singleton object to have a unique instance of a seed for adverse effect landing calculations
 *
 * @property seed the adverse effect landing chance RNG seed
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
object MagicRNGSeeder {
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
