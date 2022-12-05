package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.model.character.GameCharacter

/**
 * This represents an enemy's turn, no user input should be accepted
 * here and the state should immediately transition to checking the
 * game's end after the enemy attacks
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class EnemyTurnState(override val controller: GameController) : GameState {
    override fun isEnemyTurn(): Boolean = true
    override fun toEndCheck() {
        controller.setState(EndCheckState(controller))
    }
    /**
     * Issue an attack command from [attacker] to [target]
     * @param attacker the character that attacks
     * @param target the character that receives the attack
     * @return the amount of damage dealt
     */
    override fun enemyAttack(attacker: GameCharacter, target: GameCharacter): Int {
        toEndCheck()
        return attacker.attack(target)
    }
}
