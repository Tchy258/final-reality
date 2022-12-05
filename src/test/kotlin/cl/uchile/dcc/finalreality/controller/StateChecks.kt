package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.controller.state.GameState
import cl.uchile.dcc.finalreality.exceptions.InvalidStateTransitionException
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal val characterCreationTransition = GameState::toCharacterCreation
internal val endCheckTransition = GameState::toEndCheck
internal val enemyDefeatedTransition = GameState::toEnemyDefeated
internal val enemyGenerationTransition = GameState::toEnemyGeneration
internal val enemyTurnTransition = GameState::toEnemyTurn
internal val magicalPlayerTurnTransition = GameState::toMagicalPlayerTurn
internal val nonMagicalPlayerTurnTransition = GameState::toNonMagicalPlayerTurn
internal val playerDefeatedTransition = GameState::toPlayerDefeated
internal val turnWaitTransition = GameState::toTurnWait
internal val weaponEquipTransition = GameState::toWeaponEquip
internal val stateTransitions: List<(GameState) -> Unit> = listOf(
    characterCreationTransition,
    endCheckTransition,
    enemyDefeatedTransition,
    enemyGenerationTransition,
    enemyTurnTransition,
    magicalPlayerTurnTransition,
    nonMagicalPlayerTurnTransition,
    playerDefeatedTransition,
    turnWaitTransition,
    weaponEquipTransition,
)

internal val stateQuestions: List<(GameState) -> Boolean> = listOf(
    GameState::isCharacterCreation,
    GameState::isEndCheck,
    GameState::isEnemyDefeated,
    GameState::isEnemyTurn,
    GameState::isEnemyGeneration,
    GameState::isMagicalPlayerTurn,
    GameState::isNonMagicalPlayerTurn,
    GameState::isTurnWait,
    GameState::isWeaponEquip,
    GameState::isPlayerDefeated
)
internal fun validTransitionCheck(state: GameState, transitions: List<(GameState) -> Unit>) {
    for (transition in transitions) {
        assertDoesNotThrow {
            transition(state)
        }
    }
}

internal fun invalidTransitionCheck(state: GameState, transitions: List<(GameState) -> Unit>) {
    for (transition in transitions) {
        assertThrows<InvalidStateTransitionException> {
            transition(state)
        }
    }
}

internal fun falseQuestionsCheck(state: GameState, questions: List<(GameState) -> Boolean>) {
    for (question in questions) {
        question(state) shouldNotBe true
    }
}
