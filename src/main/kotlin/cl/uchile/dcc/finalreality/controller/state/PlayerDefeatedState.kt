package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController

/**
 * This represents the state when all the player characters are defeated
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class PlayerDefeatedState(override val controller: GameController) : GameState {
    override fun isPlayerDefeated(): Boolean = true
    override fun toCharacterCreation() {
        controller.setState(CharacterCreationState(controller))
    }
    override fun onEnemyWin(nextGame: Boolean) {
        if (nextGame) {
            toCharacterCreation()
        }
    }
}
