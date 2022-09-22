package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.weapon.wielder.StaffWielder
import java.util.Objects
/**
 * A class that identifies a [Weapon] as a Staff, and tells whoever tries to equip it
 * to equip a staff.
 *
 * @param name the name of the weapon.
 * @param damage the base damage done by the weapon.
 * @param weight the weight of the weapon.
 * @property magicDamage the magicDamage of the staff.
 *
 * @constructor Creates a new staff.
 * @author <a href="https://www.github.com/Tchy258">Tchy258</a>
 */
class Staff(
    name: String,
    damage: Int,
    weight: Int,
    val magicDamage: Int
) : AbstractWeapon(name, damage, weight) {
    override fun equipWeapon(aCharacter: PlayerCharacter) {
        (aCharacter as StaffWielder).equipStaff(this)
    }
    override fun hashCode() = Objects.hash(Staff::class, name, damage, weight, magicDamage)
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Staff -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        damage != other.damage -> false
        weight != other.weight -> false
        magicDamage != other.magicDamage -> false
        else -> true
    }
    override fun toString() = "Staff { name: '$name', damage: $damage, weight: $weight, magicDamage: $magicDamage }"
}
