package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController

/**
 * This represents a non-magical playable character's turn, this state
 * only allows attacking and swapping weapons
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class NonMagicalPlayerTurnState(override val controller: GameController) : AbstractPlayerTurnState() {
    override fun isNonMagicalPlayerTurn(): Boolean = true
    override fun toWeaponEquip() {
        controller.setState(WeaponEquipState(controller, this))
    }
    override fun toEndCheck() {
        controller.setState(EndCheckState(controller))
    }
}
