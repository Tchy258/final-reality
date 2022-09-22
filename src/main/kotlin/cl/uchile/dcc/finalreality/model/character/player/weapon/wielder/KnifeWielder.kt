package cl.uchile.dcc.finalreality.model.character.player.weapon.wielder

import cl.uchile.dcc.finalreality.model.character.player.weapon.Knife

/**
 * This represents a player character that can equip a [Knife]
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface KnifeWielder {
    /**
     * Equips a [Knife] to the character
     */
    fun equipKnife(knife: Knife)
}
