/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.exceptions.Require
import cl.uchile.dcc.finalreality.model.character.debuff.Burned
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.debuff.NullState
import cl.uchile.dcc.finalreality.model.character.debuff.Paralyzed
import cl.uchile.dcc.finalreality.model.character.debuff.Poisoned
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import java.lang.Integer.max
import java.lang.Integer.min
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

    private val statusEffects: MutableSet<Debuff> = mutableSetOf(NullState())
    protected lateinit var scheduledExecutor: ScheduledExecutorService
    override val maxHp = Require.Stat(maxHp, "Max Hp") atLeast 1
    override val defense = Require.Stat(defense, "Defense") atLeast 0
    private var _currentHp = maxHp
        set(value) {
            field = Require.Stat(value, "Current Hp") inRange 0..maxHp
        }
    override val currentHp: Int
        get() = _currentHp

    override fun addDebuff(debuff: Debuff) {
        statusEffects.add(debuff)
    }
    override fun removeDebuff(debuff: Debuff) {
        statusEffects.remove(debuff)
    }

    override fun isBurned(): Boolean {
        return statusEffects.contains(Burned(1))
    }

    override fun isParalyzed(): Boolean {
        return statusEffects.contains(Paralyzed())
    }

    override fun isPoisoned(): Boolean {
        return statusEffects.contains(Poisoned(1))
    }
    override fun receiveAttack(damage: Int) {
        val finalDamage: Int = if (damage > defense) {
            damage - defense
        } else {
            0
        }
        val finalHp: Int = if (finalDamage > _currentHp) {
            0
        } else {
            _currentHp - finalDamage
        }
        _currentHp = finalHp
    }

    override fun receiveMagicDamage(damage: Int) {
        _currentHp = max(0, _currentHp - damage)
    }
    override fun receiveHealing(healing: Int) {
        val finalHp: Int = try {
            Math.addExact(_currentHp, healing)
        } catch (integerOverflow: ArithmeticException) {
            this.maxHp
        }
        _currentHp = min(this.maxHp, finalHp)
    }

    /**
     * This method encapsulates the real attack logic which depends on
     * whether this [GameCharacter] is a [PlayerCharacter] or an [Enemy]
     */
    protected abstract fun executeAttack(anotherCharacter: GameCharacter)

    override fun attack(anotherCharacter: GameCharacter): Boolean {
        val canAttack = rollEffects()
        if (canAttack) {
            executeAttack(anotherCharacter)
        }
        return canAttack
    }
    /**
     * Activates adverse effects applied to this character
     * @return whether this character can attack or not (depending on if it's [Paralyzed])
     */
    private fun rollEffects(): Boolean {
        var canAttack = true
        for (debuff in statusEffects) {
            canAttack = canAttack && debuff.rollEffect(this)
        }
        return canAttack
    }
    /**
     * Adds this character to the [turnsQueue].
     */
    protected fun addToQueue() {
        turnsQueue.put(this)
        scheduledExecutor.shutdown()
    }
}
