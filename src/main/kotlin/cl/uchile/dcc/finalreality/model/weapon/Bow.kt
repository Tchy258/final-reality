package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.character.player.BowUser
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter
import java.util.Objects
/**
 * A class that identifies a [Weapon] as a Bow, and tells whoever tries to equip it
 * whether they can or not, they should be a [BowUser].
 *
 * @param name the name of the weapon.
 * @param damage the base damage done by the weapon.
 * @param weight the weight of the weapon.
 *
 * @constructor Creates a new bow.
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
class Bow(
    name: String,
    damage: Int,
    weight: Int
) : AbstractWeapon(name, damage, weight) {
    override fun canEquip(aCharacter: PlayerCharacter): Boolean = aCharacter is BowUser
    override fun hashCode() = Objects.hash(Bow::class, name, damage, weight)
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Bow -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        damage != other.damage -> false
        weight != other.weight -> false
        else -> true
    }
    override fun toString() = "Bow { name: '$name', damage: $damage, weight: $weight }"
}
