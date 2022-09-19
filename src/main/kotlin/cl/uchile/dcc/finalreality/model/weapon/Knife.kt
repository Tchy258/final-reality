package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter
import java.util.Objects
/**
 * A class that identifies a [Weapon] as a Knife, and tells whoever tries to equip it
 * to equip a knife.
 *
 * @param name the name of the weapon.
 * @param damage the base damage done by the weapon.
 * @param weight the weight of the weapon.
 *
 * @constructor Creates a new knife.
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
class Knife(
    name: String,
    damage: Int,
    weight: Int
) : AbstractWeapon(name, damage, weight) {
    override fun equipWeapon(aCharacter: PlayerCharacter): Boolean = aCharacter.equipKnife(this)
    override fun hashCode() = Objects.hash(Knife::class, name, damage, weight)
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Knife -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        damage != other.damage -> false
        weight != other.weight -> false
        else -> true
    }
    override fun toString() = "Knife { name: '$name', damage: $damage, weight: $weight }"
}
