package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.BlackMage

/**
 * This represents a weapon that can be equipped by a [BlackMage]
 */
interface BlackMageWeapon {
    /**
     * Equips a weapon to a [BlackMage]
     */
    fun equipWeapon(aCharacter: BlackMage)
}