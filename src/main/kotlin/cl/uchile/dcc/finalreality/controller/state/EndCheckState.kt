package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController

/**
 * This represents the state that happens at the end of every turn.
 * If the game isn't over, it proceeds to the TurnWait state, else
 * transitions to the correct defeat state
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class EndCheckState(override val controller: GameController) : GameState {
    override fun isEndCheck(): Boolean = true
    override fun toEnemyDefeated() {
        controller.setState(EnemyDefeatedState(controller))
    }
    override fun toPlayerDefeated() {
        controller.setState(PlayerDefeatedState(controller))
    }
    override fun toTurnWait() {
        controller.setState(TurnWaitState(controller))
    }
}
