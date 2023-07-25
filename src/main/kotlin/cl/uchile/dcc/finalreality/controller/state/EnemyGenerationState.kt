package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController

/**
 * This represents an optional game state where more enemies are generated
 * if the player wishes to continue to a next battle
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

class EnemyGenerationState(override val controller: GameController) : GameState {
    override fun isEnemyGeneration(): Boolean = true
    override fun toTurnWait() {
        controller.setState(TurnWaitState(controller))
    }
}
