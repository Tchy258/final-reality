package cl.uchile.dcc.finalreality.model.character.player.weapon

/**
 * A class that holds all the information of a weapon.
 *
 * @property name the name of the weapon.
 * @property damage the base damage done by the weapon.
 * @property weight the weight of the weapon.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
abstract class AbstractWeapon(
    override val name: String,
    override val damage: Int,
    override val weight: Int
) : Weapon
