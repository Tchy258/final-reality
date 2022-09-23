package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.BlackMage

/**
 * This represents a weapon that can be equipped by a [BlackMage]
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface BlackMageWeapon {
    /**
     * Equips a weapon to a [BlackMage]
     */
    fun equipWeapon(aCharacter: BlackMage)
}
