package cl.uchile.dcc.finalreality.model.character.player.weapon.wielder

import cl.uchile.dcc.finalreality.model.character.player.weapon.Staff

/**
 * This represents a player character that can equip a [Staff]
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface StaffWielder {
    /**
     * Equips a [Staff] to the character
     */
    fun equipStaff(staff: Staff)
}
