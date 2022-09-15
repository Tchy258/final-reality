package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter

/**
 * A class that holds all the information of a weapon.
 *
 * @property name String
 *     The name of the weapon.
 * @property damage Int
 *     The base damage done by the weapon.
 * @property weight Int
 *     The weight of the weapon.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
abstract class Weapon(
    val name: String,
    val damage: Int,
    val weight: Int
) {
    /**
     * Checks whether this weapon can be equipped to a [PlayerCharacter].
     * This will depend on the weapon's subclass and the interfaces the [PlayerCharacter] implements.
     */
    abstract fun canEquip(aCharacter: PlayerCharacter): Boolean
}
