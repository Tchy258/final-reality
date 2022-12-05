/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.exceptions.NoActiveSpellException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.debuff.NoDebuff
import cl.uchile.dcc.finalreality.model.magic.Magic
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.util.Objects
import java.util.concurrent.BlockingQueue

/**
 * A [BlackMage] is a concrete type of [AbstractMage] that can cast black magic.
 * Black Mages can use a [Knife] or a [Staff]
 *
 * @param name the mage's name.
 * @param maxHp the mage's maximum health points.
 * @param maxMp the mage's maximum magic points.
 * @param defense the mage's defense.
 * @param turnsQueue the queue with the characters waiting for their turn.
 * @property currentMp the current MP of the mage.
 * @property currentHp the current HP of the mage.
 * @constructor Creates a new Black Mage.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
class BlackMage(
    name: String,
    maxHp: Int,
    maxMp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractMage(name, maxHp, maxMp, defense, turnsQueue) {

    override fun cast(target: GameCharacter): Pair<Int, Debuff> {
        if (hasWeaponEquipped()) {
            if (!hasActiveSpell()) throw NoActiveSpellException(name)
            val wasCast: Boolean = canUseMp(activeSpell.cost)
            val hpBefore = target.currentHp
            var hpNow = hpBefore
            var debuff: Debuff = NoDebuff()
            if (wasCast) {
                debuff = activeSpell.castBlackMagic(target)
                hpNow = target.currentHp
            }
            return if (wasCast) {
                Pair(hpBefore - hpNow, debuff)
            } else {
                Pair(-1, debuff)
            }
        } else {
            throw(NoWeaponEquippedException(this.name))
        }
    }

    override fun getSpells(): List<Magic> {
        return listOf(
            Thunder(currentMagicDamage),
            Fire(currentMagicDamage)
        )
    }

    override fun equip(weapon: Weapon) {
        weapon.equipToBlackMage(this)
    }
    fun equipKnife(knife: Knife) {
        this.setWeapon(knife)
        this.currentMagicDamage = 1
    }
    fun equipStaff(staff: Staff) {
        this.setWeapon(staff)
        this.currentMagicDamage = staff.magicDamage
    }

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is BlackMage -> false
        hashCode() != other.hashCode() -> false
        name != other.name -> false
        maxHp != other.maxHp -> false
        maxMp != other.maxMp -> false
        defense != other.defense -> false
        else -> true
    }

    override fun hashCode() =
        Objects.hash(BlackMage::class, name, maxHp, maxMp, defense)

    override fun toString() = "BlackMage { " +
        "name: '$name', " +
        "maxHp: $maxHp, " +
        "maxMp: $maxMp, " +
        "defense: $defense, " +
        "currentHp: $currentHp, " +
        "currentMp: $currentMp " +
        "}"
}
