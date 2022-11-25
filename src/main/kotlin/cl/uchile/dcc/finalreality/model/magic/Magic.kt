package cl.uchile.dcc.finalreality.model.magic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff

/**
 * Generic interface to represent any type of magic
 *
 * @property cost the spell's mana points cost
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

interface Magic {
    val cost: Int
    /**
     * Cast this magic spell onto a [spellTarget] with a [magicDamage] value,
     * applying a [Debuff] if applicable
     * @return whether the spell activated its adverse effect
     */
    fun cast(magicDamage: Int, spellTarget: GameCharacter): Boolean
}
