package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.exceptions.Require
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * An abstract class that holds the common behaviour of all the characters in the game.
 *
 * @property name
 *    The name of the character.
 * @property maxHp
 *    The maximum health points of the character.
 * @property defense
 *    The defense of the character.
 * @property turnsQueue
 *    The queue with the characters waiting for their turn.
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

    private lateinit var scheduledExecutor: ScheduledExecutorService
    override val maxHp = Require.Stat(maxHp, "Max Hp") atLeast 1
    override var currentHp = maxHp
        set(value) {
            field = Require.Stat(value, "Current Hp") inRange 0..maxHp
        }
    override val defense = Require.Stat(defense, "Defense") atLeast 0
    override fun waitTurn() {
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        // Since the delay has to be a Long type and weights can be less than 10,
        // seconds should not be used (decimals are discarded after Long division),
        // so milliseconds are used instead.
        // (weight/10) seconds is the same as (weight/10) * 1000 = weight * 100 milliseconds.
        when (this) {
            is PlayerCharacter -> {
                scheduledExecutor.schedule(
                    /* command = */ ::addToQueue,
                    /* delay = */ (this.equippedWeapon.weight * 100).toLong(),
                    /* unit = */ TimeUnit.MILLISECONDS
                )
            }

            is Enemy -> {
                scheduledExecutor.schedule(
                    /* command = */ ::addToQueue,
                    /* delay = */ (this.weight * 100).toLong(),
                    /* unit = */ TimeUnit.MILLISECONDS
                )
            }
        }
    }

    /**
     * Adds this character to the turns queue.
     */
    private fun addToQueue() {
        turnsQueue.put(this)
        scheduledExecutor.shutdown()
    }
}
