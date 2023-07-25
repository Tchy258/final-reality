package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import java.util.concurrent.LinkedBlockingQueue

/**
 * This is an intermediate state while a character hasn't been taken
 * from the queue yet, it transitions to adverse effect application as
 * soon as a character is taken from the queue
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class TurnWaitState(override val controller: GameController) : GameState {
    override fun isTurnWait(): Boolean = true
    override fun toMagicalPlayerTurn() {
        controller.setState(MagicalPlayerTurnState(controller))
    }
    override fun toNonMagicalPlayerTurn() {
        controller.setState(NonMagicalPlayerTurnState(controller))
    }
    override fun toEnemyTurn() {
        controller.setState(EnemyTurnState(controller))
    }

    override fun toEndCheck() {
        controller.setState(EndCheckState(controller))
    }
    override tailrec fun nextTurn(queue: LinkedBlockingQueue<GameCharacter>): GameCharacter {
        return if (!queue.isEmpty()) {
            queue.peek()
        } else {
            Thread.sleep(2000)
            nextTurn(queue)
        }
    }
}
