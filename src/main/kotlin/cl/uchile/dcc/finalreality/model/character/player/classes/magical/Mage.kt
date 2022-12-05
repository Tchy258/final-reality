package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.magic.Magic

/**
 * This represents a mage controlled by the user
 *
 * @property maxMp the mage's maximum magic points.
 * @property currentMp The current MP of the mage.
 * @property activeSpell the mage's current active spell
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface Mage : PlayerCharacter {
    val maxMp: Int
    val currentMp: Int
    val activeSpell: Magic
    /**
     * Attempts to cast a spell
     * @return the amount of damage dealt and adverse effect, -1 if cast is unsuccessful,
     * and a NoDebuff object if no debuff was dealt
     */
    fun cast(target: GameCharacter): Pair<Int, Debuff>
    /**
     * Sets [spell] as this mage's current active spell
     */
    fun setSpell(spell: Magic)
    /**
     * Checks whether a spell with a [spellCost] can be cast and deducts mp
     * if it has to
     * @param spellCost the spell's mp cost
     * @return whether the spell can be cast or not
     */
    fun canUseMp(spellCost: Int): Boolean
    /**
     * Restores [restoration] mp to the mage
     * @param restoration the amount of mp restored
     */
    fun restoreMp(restoration: Int)

    /**
     * Returns this mage's current magic damage
     */
    fun getMagicDamage(): Int
    override fun isMage(): Boolean = true
}
