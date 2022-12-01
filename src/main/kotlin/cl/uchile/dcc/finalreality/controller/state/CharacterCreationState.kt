package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

/**
 * This represents the initial game state, where the user creates
 * their characters
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

class CharacterCreationState(override val controller: GameController) : GameState {
    override fun isCharacterCreation(): Boolean = true
    override fun toTurnWait() {
        controller.setState(TurnWaitState(controller))
    }
    /**
     * Creates a new engineer
     */
    override fun createEngineer(name: String, queue: LinkedBlockingQueue<GameCharacter>): Engineer {
        return Engineer(
            name,
            Random.nextInt(300, 500),
            Random.nextInt(20, 50),
            queue
        )
    }
    /**
     * Creates a new knight
     */
    override fun createKnight(name: String, queue: LinkedBlockingQueue<GameCharacter>): Knight {
        return Knight(
            name,
            Random.nextInt(400, 600),
            Random.nextInt(30, 60),
            queue
        )
    }
    /**
     * Creates a new thief
     */
    override fun createThief(name: String, queue: LinkedBlockingQueue<GameCharacter>): Thief {
        return Thief(
            name,
            Random.nextInt(300, 400),
            Random.nextInt(15, 45),
            queue
        )
    }
    /**
     * Creates a new white mage
     */
    override fun createWhiteMage(name: String, queue: LinkedBlockingQueue<GameCharacter>): WhiteMage {
        return WhiteMage(
            name,
            Random.nextInt(200, 400),
            Random.nextInt(100, 200),
            Random.nextInt(10, 30),
            queue
        )
    }
    /**
     * Creates a new black mage
     */
    override fun createBlackMage(name: String, queue: LinkedBlockingQueue<GameCharacter>): BlackMage {
        return BlackMage(
            name,
            Random.nextInt(200, 400),
            Random.nextInt(100, 200),
            Random.nextInt(10, 30),
            queue
        )
    }
}
