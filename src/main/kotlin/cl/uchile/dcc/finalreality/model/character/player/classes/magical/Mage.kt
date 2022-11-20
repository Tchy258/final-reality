package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter

/**
 * This represents a mage controlled by the user
 *
 * @property maxMp the mage's maximum magic points.
 * @property currentMp The current MP of the mage.
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface Mage : PlayerCharacter {
    val maxMp: Int
    val currentMp: Int
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
}
