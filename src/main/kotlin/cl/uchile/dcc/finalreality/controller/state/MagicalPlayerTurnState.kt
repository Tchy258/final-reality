package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.debuff.Debuff
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.Mage
import cl.uchile.dcc.finalreality.model.magic.Magic

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
    override fun useMagic(attacker: Mage, spell: Magic, target: GameCharacter): Pair<Int, Debuff> {
        return attacker.cast(spell, target)
    }
}
