package cl.uchile.dcc.finalreality.model.magic.blackmagic

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.magic.Magic

/**
 * This represents black magic spells
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */

interface BlackMagic : Magic {
    override fun cast(magicDamage: Int, spellTarget: GameCharacter): Boolean = castBlackMagic(magicDamage, spellTarget)
    /**
     * Casts this spell targeting a [spellTarget], activating its effect
     * and applying [Debuff]s (if any)
     * @return whether the spell activated its adverse effect
     */
    fun castBlackMagic(magicDamage: Int, spellTarget: GameCharacter): Boolean
}
