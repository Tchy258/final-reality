package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.controller.curriedActions
import cl.uchile.dcc.finalreality.controller.falseQuestionsCheck
import cl.uchile.dcc.finalreality.controller.invalidActionsCheck
import cl.uchile.dcc.finalreality.controller.invalidTransitionCheck
import cl.uchile.dcc.finalreality.controller.stateQuestions
import cl.uchile.dcc.finalreality.controller.stateTransitions
import cl.uchile.dcc.finalreality.controller.turnWaitTransition
import cl.uchile.dcc.finalreality.controller.validTransitionCheck
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.function.Predicate

class EnemyGenerationStateTest : FunSpec({
    val dummyController = GameController()
    val thisState = EnemyGenerationState(dummyController)

    val validTransitions: List<(GameState) -> Unit> = listOf(
        turnWaitTransition
    )

    val thisQuestion = GameState::isEnemyGeneration
    val otherQuestions = stateQuestions.toMutableList()
    otherQuestions.remove(thisQuestion)

    val invalidTransitions: MutableList<(GameState) -> Unit> = stateTransitions.toMutableList()
    val predicate = Predicate { transition: (GameState) -> Unit -> validTransitions.contains(transition) }
    invalidTransitions.removeIf(predicate)
    val invalidActions = curriedActions.toList()

    context("An EnemyGenerationState should") {
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
        test("Not do invalid actions") {
            invalidActionsCheck(thisState, invalidActions)
        }
    }
})
