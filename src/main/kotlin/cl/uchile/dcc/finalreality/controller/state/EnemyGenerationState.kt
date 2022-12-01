package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import kotlin.random.Random

/**
 * This represents an optional game state where more enemies are generated
 * if the player wishes to continue to a next battle
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

class EnemyGenerationState(override val controller: GameController) : GameState {
    init {
        val enemyAmount = Random.nextInt(1, 6)
        controller.generateEnemy(enemyAmount)
        toTurnWait()
    }
    override fun isEnemyGeneration(): Boolean = true
    override fun toTurnWait() {
        controller.setState(TurnWaitState(controller))
    }
}
