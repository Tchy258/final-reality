package cl.uchile.dcc.finalreality.model.character.player.weapon

import cl.uchile.dcc.finalreality.model.character.player.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.Knight
import cl.uchile.dcc.finalreality.model.character.player.Thief
import cl.uchile.dcc.finalreality.model.character.player.weapon.usability.BlackMageWeapon
import cl.uchile.dcc.finalreality.model.character.player.weapon.usability.KnightWeapon
import cl.uchile.dcc.finalreality.model.character.player.weapon.usability.ThiefWeapon
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
) : AbstractWeapon(name, damage, weight),
    ThiefWeapon,
    KnightWeapon,
    BlackMageWeapon {
    override fun equipWeapon(aCharacter: Knight) {
        aCharacter.equipKnife(this)
    }
    override fun equipWeapon(aCharacter: Thief) {
        aCharacter.equipKnife(this)
    }
    override fun equipWeapon(aCharacter: BlackMage) {
        aCharacter.equipKnife(this)
    }
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
