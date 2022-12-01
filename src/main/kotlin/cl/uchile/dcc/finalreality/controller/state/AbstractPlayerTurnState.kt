package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.model.character.GameCharacter

/**
 * Abstract class to represent the state of any player turn.
 * Both non-magical and magic characters attack the same and have
 * a transition to the end check
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
abstract class AbstractPlayerTurnState : GameState {
    override fun toEndCheck() {
        controller.setState(EndCheckState(controller))
    }
    override fun attack(attacker: GameCharacter, target: GameCharacter): Int {
        controller.setState(EndCheckState(controller))
        return attacker.attack(target)
    }
}
