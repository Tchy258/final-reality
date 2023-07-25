package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.exceptions.InconsistentReturnStateException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.weapon.Weapon

/**
 * This state represents the phase where a character chooses a weapon
 * to equip from the player character's inventory
 * @property previousState the state the game should return to depending on
 * which type of character is swapping weapons
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class WeaponEquipState(
    override val controller: GameController,
    private val previousState: GameState
) : GameState {
    override fun isWeaponEquip(): Boolean = true
    override fun toNonMagicalPlayerTurn() {
        if (previousState.isNonMagicalPlayerTurn()) controller.setState(NonMagicalPlayerTurnState(controller))
        else throw InconsistentReturnStateException(previousState::class.simpleName!!, "NonMagicalPlayerTurnState")
    }
    override fun toMagicalPlayerTurn() {
        if (previousState.isMagicalPlayerTurn()) controller.setState(MagicalPlayerTurnState(controller))
        else throw InconsistentReturnStateException(previousState::class.simpleName!!, "MagicalPlayerTurnState")
    }
    /**
     * Function to look swap a character's weapon
     * @param character the character who wants to swap weapons
     * @return whether the weapon could be equipped
     */
    override fun equipWeapon(character: PlayerCharacter, weapon: Weapon): Boolean {
        return try {
            character.equip(weapon)
            controller.setState(previousState)
            true
        } catch (e: InvalidWeaponException) {
            false
        }
    }
}
