package cl.uchile.dcc.finalreality.model.magic.blackmagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff

/**
 * This represents black magic spells
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */

interface BlackMagic {
    /**
     * This spell's mp cost
     */
    val cost: Int
    /**
     * Casts this spell targeting a [spellTarget], activating its effect
     * and applying [Debuff]s (if any)
     */
    fun castBlackMagic(magicDamage: Int, spellTarget: GameCharacter)
}
