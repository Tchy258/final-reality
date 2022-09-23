package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.WhiteMage

/**
 * This represents a weapon that can be equipped to a [WhiteMage]
 */
interface WhiteMageWeapon {
    /**
     * Equips a weapon to a [WhiteMage]
     */
    fun equipWeapon(aCharacter: WhiteMage)
}