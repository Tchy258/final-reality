/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.classes.physical

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.AbstractPlayerCharacter
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.util.Objects
import java.util.concurrent.BlockingQueue

/**
 * An [Engineer] is a concrete type of [AbstractPlayerCharacter] that can equip an [Axe] or
 * a [Bow].
 *
 * @param name the character's name.
 * @param maxHp the character's maximum health points.
 * @param defense the character's defense.
 * @param turnsQueue the queue with the characters waiting for their turn.
 * @property currentHp The current HP of the character.
 * @constructor Creates a new Engineer.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Engineer(
    name: String,
    maxHp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractPlayerCharacter(name, maxHp, defense, turnsQueue) {
    override fun equip(weapon: Weapon) {
        weapon.equipToEngineer(this)
    }
    fun equipAxe(axe: Axe) {
        this.setWeapon(axe)
    }
    fun equipBow(bow: Bow) {
        this.setWeapon(bow)
    }
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Engineer -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        maxHp != other.maxHp -> false
        defense != other.defense -> false
        else -> true
    }

    override fun hashCode() =
        Objects.hash(Engineer::class, name, maxHp, defense)

    override fun toString() = "Engineer { " +
        "name: '$name', " +
        "maxHp: $maxHp, " +
        "defense: $defense, " +
        "currentHp: $currentHp " +
        "}"
}
