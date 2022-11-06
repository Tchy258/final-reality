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
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Sword
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.util.Objects
import java.util.concurrent.BlockingQueue

/**
 * A [Thief] is a concrete type of [AbstractPlayerCharacter] that can equip
 * a [Sword], a [Knife] or a [Bow].
 *
 * @param name the character's name.
 * @param maxHp the character's maximum health points.
 * @param defense the character's defense.
 * @param turnsQueue the queue with the characters waiting for their turn.
 * @property currentHp the current HP of the character.
 * @constructor Creates a new Thief.
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Thief(
    name: String,
    maxHp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractPlayerCharacter(name, maxHp, defense, turnsQueue) {
    override fun equip(weapon: Weapon) {
        weapon.equipToThief(this)
    }
    fun equipBow(bow: Bow) {
        this.setWeapon(bow)
    }
    fun equipKnife(knife: Knife) {
        this.setWeapon(knife)
    }
    fun equipSword(sword: Sword) {
        this.setWeapon(sword)
    }
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Thief -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        maxHp != other.maxHp -> false
        defense != other.defense -> false
        else -> true
    }
    override fun hashCode() = Objects.hash(Thief::class, name, maxHp, defense)
    override fun toString() = "Thief { " +
        "name: '$name', " +
        "maxHp: $maxHp, " +
        "defense: $defense, " +
        "currentHp: $currentHp " +
        "}"
}
