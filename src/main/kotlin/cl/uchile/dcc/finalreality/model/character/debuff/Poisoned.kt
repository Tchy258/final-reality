/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Poison

/**
 * An adverse effect, it deals damage over time, applied upon casting [Poison] to a character
 *
 * @property turnLimit the amount of turns until this adverse effect wears off
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Poisoned(private var finalDamage: Int) : Debuff {

    private var turnLimit = 5
    override fun rollEffect(character: GameCharacter): Boolean {
        character.receiveMagicDamage(finalDamage)
        if (turnLimit > 0) {
            turnLimit--
        }
        if (turnLimit == 0) {
            character.removeDebuff(this)
        }
        return character.currentHp != 0
    }
    // Poisoned is just a status
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
    override fun toString(): String {
        return "Poisoned { finalDamage: $finalDamage, turnLimit: $turnLimit }"
    }
}
