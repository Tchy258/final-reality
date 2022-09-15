package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.exceptions.Require
import java.util.Objects
import java.util.concurrent.BlockingQueue

/**
 * A class that holds all the information of a single enemy of the game.
 *
 * @param name The name of this enemy.
 * @property damage The base damage this enemy deals.
 * @property weight The weight of this enemy.
 * @param turnsQueue The queue with the characters waiting for their turn.
 * @param maxHp The maximum health points of this enemy.
 * @param defense The defense of this enemy.
 *
 * @constructor Creates a new enemy with a name, a weight and the queue with the characters ready to
 *  play.
 *
 * @author <a href="https://github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Enemy(
    name: String,
    val damage: Int,
    weight: Int,
    maxHp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractCharacter(name, maxHp, defense, turnsQueue) {
    val weight = Require.Stat(weight, "Weight") atLeast 1

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

    override fun hashCode() = Objects.hash(Enemy::class, name, weight, maxHp, defense)

    override fun toString(): String = "Enemy { name:'$name', weight: $weight, maxHp: $maxHp, defense: $defense }"
}
