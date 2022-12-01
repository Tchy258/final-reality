package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.controller.state.CharacterCreationState
import cl.uchile.dcc.finalreality.controller.state.GameState
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll
import io.kotest.property.forAll
import org.junit.jupiter.api.assertDoesNotThrow


enum class States {
    CHARACTER_CREATION,
    END_CHECK,
    ENEMY_DEFEATED,
    ENEMY_GENERATION,
    MAGICAL_PLAYER_TURN,
    TURN_WAIT,
    WEAPON_EQUIP,
    NON_MAGICAL_PLAYER_TURN,
    PLAYER_DEFEATED,
    ENEMY_TURN,
}

internal suspend fun characterCreationTransitionsCheck(controller: GameController) {
    forAll<States>(100) {
        if(state == States.TURN_WAIT) {
            assertDoesNotThrow {
                controller.createBlackMage("TestCharacter")
            }
        }
    }
}