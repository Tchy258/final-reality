package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.model.character.player.WeaponUser

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
     * Tells a [WeaponUser] which [Weapon] they have to equip
     * @return true if [aCharacter] can equip this [Weapon].
     */
    fun equipWeapon(aCharacter: WeaponUser): Boolean
}
