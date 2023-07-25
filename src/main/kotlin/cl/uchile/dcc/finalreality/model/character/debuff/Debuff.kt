/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.GameCharacter

/**
 * This represents an adverse effect that can be applied to a [GameCharacter] due
 * to a spell cast on it
 */
interface Debuff {
    /**
     * Activates this adverse effect on the [character]
     * @return whether this character can attack or not due to [Paralyzed] or death
     */
    fun rollEffect(character: GameCharacter): Boolean
}
