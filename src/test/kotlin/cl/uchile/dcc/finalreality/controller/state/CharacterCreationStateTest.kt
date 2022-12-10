package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.controller.createBlackMageAction
import cl.uchile.dcc.finalreality.controller.createEngineerAction
import cl.uchile.dcc.finalreality.controller.createKnightAction
import cl.uchile.dcc.finalreality.controller.createThiefAction
import cl.uchile.dcc.finalreality.controller.createWhiteMageAction
import cl.uchile.dcc.finalreality.controller.curriedActions
import cl.uchile.dcc.finalreality.controller.falseQuestionsCheck
import cl.uchile.dcc.finalreality.controller.invalidActionsCheck
import cl.uchile.dcc.finalreality.controller.invalidTransitionCheck
import cl.uchile.dcc.finalreality.controller.stateQuestions
import cl.uchile.dcc.finalreality.controller.stateTransitions
import cl.uchile.dcc.finalreality.controller.turnWaitTransition
import cl.uchile.dcc.finalreality.controller.validActionsCheck
import cl.uchile.dcc.finalreality.controller.validTransitionCheck
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.function.Predicate

class CharacterCreationStateTest : FunSpec({
    val dummyController = GameController()
    val thisState = CharacterCreationState(dummyController)

    val validTransitions: List<(GameState) -> Unit> = listOf(
        turnWaitTransition
    )
    val validActions: List<(GameState) -> Any> = listOf(
        createEngineerAction,
        createKnightAction,
        createThiefAction,
        createBlackMageAction,
        createWhiteMageAction
    )
    val thisQuestion = GameState::isCharacterCreation
    val otherQuestions = stateQuestions.toMutableList()
    otherQuestions.remove(thisQuestion)
    val invalidTransitions: MutableList<(GameState) -> Unit> = stateTransitions.toMutableList()
    val transitionPredicate = Predicate { transition: (GameState) -> Unit -> validTransitions.contains(transition) }
    invalidTransitions.removeIf(transitionPredicate)
    val invalidActions: MutableList<(GameState) -> Any> = curriedActions.toMutableList()
    val actionPredicate = Predicate { action: (GameState) -> Any -> validActions.contains(action) }
    invalidActions.removeIf(actionPredicate)

    context("A CharacterCreationState should") {
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
        test("Execute legal actions") {
            validActionsCheck(thisState, validActions)
        }
        test("Not execute illegal actions") {
            invalidActionsCheck(thisState, invalidActions)
        }
    }
})
