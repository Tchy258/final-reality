package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.character.player.AxeUser
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter
import java.util.Objects
/**
 * A class that identifies a [Weapon] as an Axe, and tells whoever tries to equip it
 * whether they can or not, they should be a [AxeUser]
 *
 * @param name      the name of the weapon.
 * @param damage    the base damage done by the weapon.
 * @param weight    the weight of the weapon.
 *
 * @constructor Creates a new Axe.
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
class Axe(
    name: String,
    damage: Int,
    weight: Int
) : Weapon(name, damage, weight) {
    override fun canEquip(aCharacter: PlayerCharacter): Boolean = aCharacter is AxeUser
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
