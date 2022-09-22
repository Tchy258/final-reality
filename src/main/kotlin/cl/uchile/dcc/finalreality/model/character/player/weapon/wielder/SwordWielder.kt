package cl.uchile.dcc.finalreality.model.character.player.weapon.wielder

import cl.uchile.dcc.finalreality.model.character.player.weapon.Sword

/**
 * This represents a player character that can equip a [Sword]
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface SwordWielder {
    /**
     * Equips a [Sword] to the character
     */
    fun equipSword(sword: Sword)
}
