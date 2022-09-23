package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.model.character.player.Engineer
import cl.uchile.dcc.finalreality.model.character.player.Knight
import cl.uchile.dcc.finalreality.model.character.player.weapon.usability.EngineerWeapon
import cl.uchile.dcc.finalreality.model.character.player.weapon.usability.KnightWeapon
import java.util.Objects
/**
 * A class that identifies a [Weapon] as an Axe, and tells whoever tries to equip it
 * to equip an axe.
 *
 * @param name the name of the weapon.
 * @param damage the base damage done by the weapon.
 * @param weight the weight of the weapon.
 *
 * @constructor Creates a new axe.
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
class Axe(
    name: String,
    damage: Int,
    weight: Int
) : AbstractWeapon(name, damage, weight),
    KnightWeapon,
    EngineerWeapon {
    override fun equipWeapon(aCharacter: Knight) {
        aCharacter.equipAxe(this)
    }
    override fun equipWeapon(aCharacter: Engineer) {
        aCharacter.equipAxe(this)
    }
    override fun hashCode() = Objects.hash(Axe::class, name, damage, weight)
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Axe -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        damage != other.damage -> false
        weight != other.weight -> false
        else -> true
    }
    override fun toString() = "Axe { name: '$name', damage: $damage, weight: $weight }"
}
