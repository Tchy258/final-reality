package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.controller.characterCreationTransition
import cl.uchile.dcc.finalreality.controller.endCheckTransition
import cl.uchile.dcc.finalreality.controller.falseQuestionsCheck
import cl.uchile.dcc.finalreality.controller.invalidTransitionCheck
import cl.uchile.dcc.finalreality.controller.stateQuestions
import cl.uchile.dcc.finalreality.controller.stateTransitions
import cl.uchile.dcc.finalreality.controller.validTransitionCheck
import cl.uchile.dcc.finalreality.controller.weaponEquipTransition
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.function.Predicate

class PlayerDefeatedStateTest : FunSpec({
    val dummyController = GameController()
    val thisState = MagicalPlayerTurnState(dummyController)

    val validTransitions: List<(GameState) -> Unit> = listOf(
        characterCreationTransition
    )
    val thisQuestion = GameState::isPlayerDefeated
    val otherQuestions = stateQuestions.toMutableList()
    otherQuestions.remove(thisQuestion)

    val invalidTransitions: MutableList<(GameState) -> Unit> = stateTransitions.toMutableList()
    val predicate = Predicate { transition: (GameState) -> Unit -> validTransitions.contains(transition) }
    invalidTransitions.removeIf(predicate)
    context("A PlayerDefeatedState should") {
        test("Be able to do valid transitions") {
            validTransitionCheck(thisState, validTransitions)
        }
        test("Be unable to do invalid transitions") {
            invalidTransitionCheck(thisState, invalidTransitions)
        }
        test("Answer correctly when asked who they are") {
            falseQuestionsCheck(thisState, otherQuestions)
            thisQuestion(thisState) shouldBe true
        }
    }
})
