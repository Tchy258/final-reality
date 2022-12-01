package cl.uchile.dcc.finalreality.exceptions

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.controller.state.CharacterCreationState
import cl.uchile.dcc.finalreality.controller.state.EndCheckState
import cl.uchile.dcc.finalreality.controller.state.EnemyDefeatedState
import cl.uchile.dcc.finalreality.controller.state.EnemyGenerationState
import cl.uchile.dcc.finalreality.controller.state.EnemyTurnState
import cl.uchile.dcc.finalreality.controller.state.GameState
import cl.uchile.dcc.finalreality.controller.state.MagicalPlayerTurnState
import cl.uchile.dcc.finalreality.controller.state.NonMagicalPlayerTurnState
import cl.uchile.dcc.finalreality.controller.state.PlayerDefeatedState
import cl.uchile.dcc.finalreality.controller.state.TurnWaitState
import cl.uchile.dcc.finalreality.controller.state.WeaponEquipState
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows

class InconsistentReturnStateExceptionTest : FunSpec({
    val prefix = "Tried to transition from WeaponEquipState to "
    val mid = " but "
    val suffix = " was expected"

    test("An InconsistentReturnStateException can be thrown with a message") {
        checkAll<String, String> { someStateName, anotherStateName ->
            assertThrows<InconsistentReturnStateException> {
                throw InconsistentReturnStateException(
                    someStateName,
                    anotherStateName
                )
            }.message shouldBe prefix + someStateName + mid + anotherStateName + suffix
        }
    }
})
