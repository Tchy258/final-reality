package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.model.character.player.Engineer
import cl.uchile.dcc.finalreality.model.character.player.Thief
import cl.uchile.dcc.finalreality.model.character.player.weapon.usability.EngineerWeapon
import cl.uchile.dcc.finalreality.model.character.player.weapon.usability.ThiefWeapon
import java.util.Objects
/**
 * A class that identifies a [Weapon] as a Bow, and tells whoever tries to equip it
 * to equip a bow.
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
) : AbstractWeapon(name, damage, weight),
    ThiefWeapon,
    EngineerWeapon {
    override fun equipWeapon(aCharacter: Thief) {
        aCharacter.equipBow(this)
    }
    override fun equipWeapon(aCharacter: Engineer) {
        aCharacter.equipBow(this)
    }
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
