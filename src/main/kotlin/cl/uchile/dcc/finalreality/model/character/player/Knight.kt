/*
 * "Final Reality" (c) by R8V and ~Your name~
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
 * A `Knight` is a type of [PlayerCharacter] that can equip a [Sword], an [Axe] or
 * a [Knife].
 *
 * @param name the character's name
 * @param maxHp the character's maximum health points
 * @param defense the character's defense
 * @param turnsQueue the queue with the characters waiting for their turn
 * @property currentHp the current HP of the character.
 * @constructor Creates a new Knight.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Knight(
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
        println("$name can't equip bows")
        return false
    }
    override fun equipKnife(knife: Knife): Boolean {
        println("$name can't equip knives")
        return false
    }
    override fun equipStaff(staff: Staff): Boolean {
        println("$name can't equip staves")
        return false
    }
    override fun equipSword(sword: Sword): Boolean {
        this.setWeapon(sword)
        return true
    }
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Knight -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        maxHp != other.maxHp -> false
        defense != other.defense -> false
        else -> true
    }

    override fun hashCode() = Objects.hash(Knight::class, name, maxHp, defense)

    override fun toString() = "Knight { " +
        "name: '$name', " +
        "maxHp: $maxHp, " +
        "defense: $defense, " +
        "currentHp: $currentHp " +
        "}"
}
