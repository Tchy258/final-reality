package cl.uchile.dcc.finalreality.model.character.player.weapon.wielder

import cl.uchile.dcc.finalreality.model.character.player.weapon.Axe

/**
 * This represents a player character that can wield an [Axe].
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface AxeWielder {
    /**
     * Equips an [Axe] to the character
     */
    fun equipAxe(axe: Axe)
}
