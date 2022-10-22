/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.exceptions.Require
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.AbstractPlayerCharacter
import java.util.concurrent.BlockingQueue

/**
 * A class that holds all the information of a player-controlled mage character in the game.
 *
 * @param name the mage's name.
 * @param maxHp the mage's maximum health points.
 * @param defense the mage's defense.
 * @param turnsQueue the queue with the characters waiting for their turn.
 * @property maxMp the mage's maximum magic points.
 * @property currentMp The current MP of the mage.
 * @property currentHp The current HP of the mage.
 * @constructor Creates a new playable mage.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
abstract class AbstractMage(
    name: String,
    maxHp: Int,
    maxMp: Int,
    defense: Int,
    turnsQueue: BlockingQueue<GameCharacter>
) : AbstractPlayerCharacter(name, maxHp, defense, turnsQueue) {
    val maxMp = Require.Stat(maxMp, "Max MP") atLeast 1
    var currentMp: Int = maxMp
        set(value) {
            field = Require.Stat(value, "Current MP") inRange 0..maxMp
        }
}
