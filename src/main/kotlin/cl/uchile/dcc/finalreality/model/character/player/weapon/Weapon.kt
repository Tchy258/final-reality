package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter
/**
 * This represents any generic weapon.
 *
 * @property name the name of the weapon.
 * @property damage the base damage done by the weapon.
 * @property weight the weight of the weapon.
 *
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
interface Weapon {
    val name: String
    val damage: Int
    val weight: Int
    /**
     * Tells a [PlayerCharacter] which [Weapon] they have to equip
     */
    fun equipWeapon(aCharacter: PlayerCharacter)
}
