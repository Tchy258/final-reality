package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire

/**
 * An adverse effect which deals damage overtime as a side effect of a [Fire] spell
 * @property finalDamage the damage this effect deals per turn
 * @property turnLimit the amount of turns until this adverse effect wears off
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class Burned(private val finalDamage: Int) : Debuff {
    private var turnLimit: Int = 5
    override fun rollEffect(character: GameCharacter): Boolean {
        character.receiveMagicDamage(finalDamage)
        if (turnLimit > 0) {
            turnLimit--
        }
        if (turnLimit == 0) {
            character.removeDebuff(this)
        }
        return true
    }

    override fun toString(): String {
        return "Burned { finalDamage: $finalDamage, turnLimit: $turnLimit }"
    }
    // The amount of damage is not important, a burned status is just a burned status
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
