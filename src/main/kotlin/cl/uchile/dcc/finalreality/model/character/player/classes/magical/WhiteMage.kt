/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.debuff.NoDebuff
import cl.uchile.dcc.finalreality.model.magic.Magic
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Cure
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Paralysis
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Poison
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.util.Objects
import java.util.concurrent.BlockingQueue

/**
 * A [WhiteMage] is a concrete type of [AbstractMage] that can cast white magic.
 * White mages can only use a [Staff] as their weapon.
 *
 * @param name the mage's name.
 * @param maxHp the mage's maximum health points.
 * @param maxMp the mage's maximum magic points.
 * @param defense the mage's defense.
 * @param turnsQueue the queue with the characters waiting for their turn.
 * @property currentMp The current MP of the mage.
 * @property currentHp The current HP of the mage.
 * @constructor Creates a new Black Mage.
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class WhiteMage(
    name: String,
    maxHp: Int,
    maxMp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractMage(name, maxHp, maxMp, defense, turnsQueue) {

    override fun equip(weapon: Weapon) {
        weapon.equipToWhiteMage(this)
    }

    override fun cast(spell: Magic, target: GameCharacter): Pair<Int, Debuff> {
        if (hasWeaponEquipped()) {
            val wasCast: Boolean = canUseMp(spell.cost)
            var debuff: Debuff = NoDebuff()
            return if (wasCast) {
                val hpBefore = target.currentHp
                debuff = spell.castWhiteMagic(target)
                Pair(target.currentHp - hpBefore, debuff)
            } else {
                Pair(-1, debuff)
            }
        } else {
            throw NoWeaponEquippedException(this.name)
        }
    }

    fun equipStaff(staff: Staff) {
        this.setWeapon(staff)
        this.currentMagicDamage = staff.magicDamage
    }

    override fun getSpells(): List<Magic> {
        return listOf(
            Cure(),
            Paralysis(),
            Poison(currentMagicDamage)
        )
    }
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is WhiteMage -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        maxHp != other.maxHp -> false
        maxMp != other.maxMp -> false
        defense != other.defense -> false
        else -> true
    }

    override fun hashCode() =
        Objects.hash(WhiteMage::class, name, maxHp, maxMp, defense)

    override fun toString() = "WhiteMage { " +
        "name: '$name', " +
        "maxHp: $maxHp, " +
        "maxMp: $maxMp, " +
        "defense: $defense, " +
        "currentHp: $currentHp, " +
        "currentMp: $currentMp " +
        "}"
}
