package cl.uchile.dcc.finalreality.model.character.player.weapon.usability

import cl.uchile.dcc.finalreality.model.character.player.Knight
/**
 * This represents a weapon that can be equipped by a [Knight]
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface KnightWeapon {
    /**
     * Equips a weapon to a [Knight]
     */
    fun equipWeapon(aCharacter: Knight)
}
