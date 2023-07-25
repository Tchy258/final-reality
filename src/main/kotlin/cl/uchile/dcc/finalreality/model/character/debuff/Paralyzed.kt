/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder

/**
 * An adverse effect applied to a [GameCharacter] as side effect of a [Thunder] spell,
 * upon activation, keeps the character from attacking, and then it wears off
 *
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Paralyzed : Debuff {
    override fun rollEffect(character: GameCharacter): Boolean {
        character.removeDebuff(this)
        return false
    }
    // Just like Burned, paralyzed is only a status
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
    override fun toString(): String {
        return "Paralyzed"
    }
}
