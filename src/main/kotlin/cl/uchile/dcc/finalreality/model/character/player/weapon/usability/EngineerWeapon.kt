package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.Engineer

/**
 * This represents a weapon that can be equipped by an [Engineer]
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface EngineerWeapon {
    /**
     * Equips a weapon to an [Engineer]
     */
    fun equipWeapon(aCharacter: Engineer)
}
