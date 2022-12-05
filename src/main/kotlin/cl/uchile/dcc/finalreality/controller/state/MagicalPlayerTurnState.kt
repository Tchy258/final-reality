package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.Mage

/**
 * This represents a player mage's turn, this state is necessary to
 * ensure spells are cast only by mages
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class MagicalPlayerTurnState(override val controller: GameController) : AbstractPlayerTurnState() {
    override fun isMagicalPlayerTurn(): Boolean = true
    override fun toWeaponEquip() {
        controller.setState(WeaponEquipState(controller, this))
    }
    override fun toEndCheck() {
        controller.setState(EndCheckState(controller))
    }

    override fun getMagicDamage(character: PlayerCharacter): Int {
        character as Mage
        return character.getMagicDamage()
    }
    override fun useMagic(attacker: Mage, target: GameCharacter): Pair<Int, Debuff> {
        return attacker.cast(target)
    }
}
