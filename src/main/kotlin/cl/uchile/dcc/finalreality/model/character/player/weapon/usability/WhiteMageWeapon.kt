package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.WhiteMage

/**
 * This represents a weapon that can be equipped to a [WhiteMage]
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface WhiteMageWeapon {
    /**
     * Equips a weapon to a [WhiteMage]
     */
    fun equipWeapon(aCharacter: WhiteMage)
}
