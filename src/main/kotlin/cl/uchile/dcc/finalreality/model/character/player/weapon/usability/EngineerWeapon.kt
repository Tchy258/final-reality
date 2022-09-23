package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.Engineer

/**
 * This represents a weapon that can be equipped by an [Engineer]
 */
interface EngineerWeapon {
    /**
     * Equips a weapon to an [Engineer]
     */
    fun equipWeapon(aCharacter: Engineer)
}