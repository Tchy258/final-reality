/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.exceptions.Require
import java.lang.Integer.max
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ScheduledExecutorService

/**
 * An abstract class that holds the common behaviour of all the characters in the game.
 *
 * @property name the name of the character.
 * @property maxHp the maximum health points of the character.
 * @property defense the defense of the character.
 * @property turnsQueue the queue with the characters waiting for their turn.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
abstract class AbstractCharacter(
    override val name: String,
    maxHp: Int,
    defense: Int,
    private val turnsQueue: BlockingQueue<GameCharacter>,
) : GameCharacter {

    protected lateinit var scheduledExecutor: ScheduledExecutorService
    override val maxHp = Require.Stat(maxHp, "Max Hp") atLeast 1
    override val defense = Require.Stat(defense, "Defense") atLeast 0
    private var _currentHp = maxHp
        set(value) {
            field = Require.Stat(value, "Current Hp") inRange 0..maxHp
        }
    override val currentHp: Int
        get() = _currentHp

    override fun receiveAttack(damage: Int) {
        _currentHp -= max(0,(damage - defense))
    }

    override fun receiveHealing(healing: Int) {
        _currentHp += healing
    }
    abstract override fun attack(anotherCharacter: GameCharacter)

    /**
     * Adds this character to the [turnsQueue].
     */
    protected fun addToQueue() {
        turnsQueue.put(this)
        scheduledExecutor.shutdown()
    }
}
