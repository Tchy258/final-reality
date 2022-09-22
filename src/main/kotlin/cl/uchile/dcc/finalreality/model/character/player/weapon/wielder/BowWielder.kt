package cl.uchile.dcc.finalreality.model.character.player.weapon.wielder

import cl.uchile.dcc.finalreality.model.character.player.weapon.Bow

/**
 * This represents a player character that can wield an [Bow].
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface BowWielder {
    /**
     * Equips a [Bow] to the character
     */
    fun equipBow(bow: Bow)
}
