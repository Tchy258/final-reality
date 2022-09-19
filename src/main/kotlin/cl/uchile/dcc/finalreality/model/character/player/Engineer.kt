/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import java.util.Objects
import java.util.concurrent.BlockingQueue

/**
 * An `Engineer` is a type of [PlayerCharacter] that can equip an [Axe] or
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
    override fun equipAxe(axe: Axe): Boolean {
        this.setWeapon(axe)
        return true
    }
    override fun equipBow(bow: Bow): Boolean {
        this.setWeapon(bow)
        return true
    }
    override fun equipKnife(knife: Knife): Boolean {
        return false
    }
    override fun equipStaff(staff: Staff): Boolean {
        return false
    }
    override fun equipSword(sword: Sword): Boolean {
        return false
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
        "defense: $defense " +
        "}"
}
