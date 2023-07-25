package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.controller.curriedActions
import cl.uchile.dcc.finalreality.controller.endCheckTransition
import cl.uchile.dcc.finalreality.controller.enemyAttackAction
import cl.uchile.dcc.finalreality.controller.falseQuestionsCheck
import cl.uchile.dcc.finalreality.controller.invalidActionsCheck
import cl.uchile.dcc.finalreality.controller.invalidTransitionCheck
import cl.uchile.dcc.finalreality.controller.stateQuestions
import cl.uchile.dcc.finalreality.controller.stateTransitions
import cl.uchile.dcc.finalreality.controller.validActionsCheck
import cl.uchile.dcc.finalreality.controller.validTransitionCheck
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.function.Predicate

class EnemyTurnStateTest : FunSpec({
    val dummyController = GameController()
    val thisState = EnemyTurnState(dummyController)

    val validTransitions: List<(GameState) -> Unit> = listOf(
        endCheckTransition
    )
    val validActions: List<(GameState) -> Any> = listOf(
        enemyAttackAction
    )
    val thisQuestion = GameState::isEnemyTurn
    val otherQuestions = stateQuestions.toMutableList()
    otherQuestions.remove(thisQuestion)

    val invalidTransitions: MutableList<(GameState) -> Unit> = stateTransitions.toMutableList()
    val predicate = Predicate { transition: (GameState) -> Unit -> validTransitions.contains(transition) }
    invalidTransitions.removeIf(predicate)

    val invalidActions: MutableList<(GameState) -> Any> = curriedActions.toMutableList()
    val actionPredicate = Predicate { action: (GameState) -> Any -> validActions.contains(action) }
    invalidActions.removeIf(actionPredicate)

    context("An EnemyTurnState should") {
        test("Do valid transitions") {
            validTransitionCheck(thisState, validTransitions)
        }
        test("Not do invalid transitions") {
            invalidTransitionCheck(thisState, invalidTransitions)
        }
        test("Answer correctly when asked who they are") {
            falseQuestionsCheck(thisState, otherQuestions)
            thisQuestion(thisState) shouldBe true
        }
        test("Do valid actions") {
            validActionsCheck(thisState, validActions)
        }
        test("Not do invalid actions") {
            invalidActionsCheck(thisState, invalidActions)
        }
    }
})
