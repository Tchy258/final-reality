/*
 * "Final Reality" (c) by R8V and ~Your name~
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player

import cl.uchile.dcc.finalreality.exceptions.Require
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import java.util.Objects
import java.util.concurrent.BlockingQueue

/**
 * A White Mage is a type of [PlayerCharacter] that can cast white magic.
 *
 * @param name the character's name.
 * @param maxHp the character's maximum health points.
 * @param maxMp the character's maximum magic points.
 * @param defense the character's defense.
 * @param turnsQueue the queue with the characters waiting for their turn.
 * @property currentMp The current MP of the character.
 * @property currentHp The current HP of the character.
 * @constructor Creates a new Black Mage.
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class WhiteMage(
    name: String,
    maxHp: Int,
    maxMp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractPlayerCharacter(name, maxHp, defense, turnsQueue) {
    val maxMp = Require.Stat(maxMp, "Max MP") atLeast 0
    var currentMp: Int = maxMp
        set(value) {
            field = Require.Stat(value, "Current MP") inRange 0..maxMp
        }
    override fun equipAxe(axe: Axe): Boolean {
        return false
    }
    override fun equipBow(bow: Bow): Boolean {
        return false
    }
    override fun equipKnife(knife: Knife): Boolean {
        return false
    }
    override fun equipStaff(staff: Staff): Boolean {
        this.setWeapon(staff)
        return true
    }
    override fun equipSword(sword: Sword): Boolean {
        return false
    }
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is WhiteMage -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        maxHp != other.maxHp -> false
        maxMp != other.maxMp -> false
        defense != other.defense -> false
        else -> true
    }

    override fun hashCode() =
        Objects.hash(WhiteMage::class, name, maxHp, maxMp, defense)

    override fun toString() = "WhiteMage { " +
        "name: '$name', " +
        "maxHp: $maxHp, " +
        "maxMp: $maxMp, " +
        "defense: $defense, " +
        "currentHp: $currentHp, " +
        "currentMp: $currentMp " +
        "}"
}
