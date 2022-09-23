package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.Thief

/**
 * This represents a weapon that can be equipped by a [Thief]
 */
interface ThiefWeapon {
    /**
     * Equips a weapon to a [Thief]
     */
    fun equipWeapon(aCharacter: Thief)
}