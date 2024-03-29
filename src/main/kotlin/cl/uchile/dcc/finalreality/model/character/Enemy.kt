/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.exceptions.Require
import java.util.Objects
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * A class that holds all the information of a single enemy of the game.
 *
 * @param name the name of this enemy.
 * @param turnsQueue the queue with the characters waiting for their turn.
 * @param maxHp the maximum health points of this enemy.
 * @param defense the defense of this enemy.
 * @property damage the base damage this enemy deals.
 * @property weight the weight of this enemy.
 * @constructor Creates a new enemy with a name, a weight and the queue with the characters ready to
 *  play.
 *
 * @author <a href="https://github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Enemy(
    name: String,
    damage: Int,
    weight: Int,
    maxHp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractCharacter(name, maxHp, defense, turnsQueue) {
    val weight = Require.Stat(weight, "Weight") atLeast 1
    val damage = Require.Stat(damage, "Damage") atLeast 0

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Enemy -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        damage != other.damage -> false
        weight != other.weight -> false
        maxHp != other.maxHp -> false
        defense != other.defense -> false
        else -> true
    }
    override fun waitTurn() {
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        scheduledExecutor.schedule(
            /* command = */ ::addToQueue,
            /* delay = */ (this.weight * 100).toLong(),
            /* unit = */ TimeUnit.MILLISECONDS
        )
    }

    override fun attack(anotherCharacter: GameCharacter): Int {
        val hpBefore = anotherCharacter.currentHp
        anotherCharacter.receiveAttack(this.damage)
        return hpBefore - anotherCharacter.currentHp
    }
    override fun hashCode() = Objects.hash(Enemy::class, name, damage, weight, maxHp, defense)
    override fun toString(): String = "Enemy { name:'$name', damage: $damage, weight: $weight, maxHp: $maxHp, defense: $defense, currentHp: $currentHp }"
}
