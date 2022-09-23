package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.Thief

/**
 * This represents a weapon that can be equipped by a [Thief]
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface ThiefWeapon {
    /**
     * Equips a weapon to a [Thief]
     */
    fun equipWeapon(aCharacter: Thief)
}
