/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player

import cl.uchile.dcc.finalreality.exceptions.Require
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.weapon.Knife
import cl.uchile.dcc.finalreality.model.character.player.weapon.Staff
import cl.uchile.dcc.finalreality.model.character.player.weapon.usability.BlackMageWeapon
import java.util.Objects
import java.util.concurrent.BlockingQueue

/**
 * A Black Mage is a type of player character that can cast black magic.
 * Black Mages can use a [Knife] or a [Staff]
 *
 * @param name the character's name.
 * @param maxHp the character's maximum health points.
 * @param maxMp the character's maximum magic points.
 * @param defense the character's defense.
 * @param turnsQueue the queue with the characters waiting for their turn.
 * @property currentMp the current MP of the character.
 * @property currentHp the current HP of the character.
 * @constructor Creates a new Black Mage.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class BlackMage(
    name: String,
    maxHp: Int,
    maxMp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractPlayerCharacter(name, maxHp, defense, turnsQueue) {
    val maxMp = Require.Stat(maxMp, "Max MP") atLeast 1
    var currentMp: Int = maxMp
        set(value) {
            field = Require.Stat(value, "Current MP") inRange 0..maxMp
        }
    fun equip(weapon: BlackMageWeapon) {
        weapon.equipWeapon(this)
    }
    fun equipKnife(knife: Knife) {
        this.setWeapon(knife)
    }
    fun equipStaff(staff: Staff) {
        this.setWeapon(staff)
    }
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is BlackMage -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        maxHp != other.maxHp -> false
        maxMp != other.maxMp -> false
        defense != other.defense -> false
        else -> true
    }

    override fun hashCode() =
        Objects.hash(BlackMage::class, name, maxHp, maxMp, defense)

    override fun toString() = "BlackMage { " +
        "name: '$name' " +
        "maxHp: $maxHp, " +
        "maxMp: $maxMp, " +
        "defense: $defense, " +
        "currentHp: $currentHp, " +
        "currentMp: $currentMp " +
        "}"
}
