package cl.uchile.dcc.finalreality.controller

import cl.uchile.dcc.finalreality.controller.state.GameState
import cl.uchile.dcc.finalreality.exceptions.IllegalStateActionException
import cl.uchile.dcc.finalreality.exceptions.InvalidStateTransitionException
import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

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
internal val curryQueue = LinkedBlockingQueue<GameCharacter>()
internal val testCharacter = Knight("CurryKnight", 100, 10, curryQueue)
internal val testEnemy = Enemy("CurryEnemy", 10, 10, 100, 10, curryQueue)
internal val testMage = BlackMage("CurryMage", 100, 100, 10, curryQueue)
internal val testWeapon = Sword("CurrySword", 100, 10)
internal val testStaff = Staff("CurryStaff", 10, 10, 100)

internal val equipWeaponAction = { state: GameState -> (GameState::equipWeapon)(state, testCharacter, testWeapon) }
internal val attackAction = { state: GameState -> (GameState::attack)(state, testCharacter, testEnemy) }
internal val useMagicAction = { state: GameState -> (GameState::useMagic)(state, testMage, testEnemy) }
internal val enemyAttackAction = { state: GameState -> (GameState::enemyAttack)(state, testEnemy, testCharacter) }
internal val createEngineerAction = { state: GameState -> (GameState::createEngineer)(state, "name", curryQueue) }
internal val createKnightAction = { state: GameState -> (GameState::createKnight)(state, "name", curryQueue) }
internal val createThiefAction = { state: GameState -> (GameState::createThief)(state, "name", curryQueue) }
internal val createBlackMageAction = { state: GameState -> (GameState::createBlackMage)(state, "name", curryQueue) }
internal val createWhiteMageAction = { state: GameState -> (GameState::createWhiteMage)(state, "name", curryQueue) }
internal val getMagicDamageAction = { state: GameState -> (GameState::getMagicDamage)(state, testMage) }
internal val nextTurnAction = { state: GameState -> (GameState::nextTurn)(state, curryQueue) }
internal val onPlayerWinAction = { state: GameState -> (GameState::onPlayerWin)(state, true) }
internal val onEnemyWinAction = { state: GameState -> (GameState::onEnemyWin)(state, true) }

internal val curriedActions = listOf(
    equipWeaponAction,
    attackAction,
    useMagicAction,
    enemyAttackAction,
    createEngineerAction,
    createKnightAction,
    createThiefAction,
    createThiefAction,
    createBlackMageAction,
    createWhiteMageAction,
    getMagicDamageAction,
    nextTurnAction,
    onPlayerWinAction,
    onEnemyWinAction
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

internal fun validActionsCheck(state: GameState, actions: List<(GameState) -> Any>) {
    testMage.equip(testStaff)
    testMage.setSpell(Fire(testMage.getMagicDamage()))
    testCharacter.equip(testWeapon)
    for (action in actions) {
        assertDoesNotThrow {
            action(state)
        }
    }
}

internal fun invalidActionsCheck(state: GameState, actions: List<(GameState) -> Any>) {
    for (action in actions) {
        assertThrows<IllegalStateActionException> {
            action(state)
        }
    }
}
