package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.controller.endCheckTransition
import cl.uchile.dcc.finalreality.controller.falseQuestionsCheck
import cl.uchile.dcc.finalreality.controller.invalidTransitionCheck
import cl.uchile.dcc.finalreality.controller.magicalPlayerTurnTransition
import cl.uchile.dcc.finalreality.controller.nonMagicalPlayerTurnTransition
import cl.uchile.dcc.finalreality.controller.stateQuestions
import cl.uchile.dcc.finalreality.controller.stateTransitions
import cl.uchile.dcc.finalreality.controller.validTransitionCheck
import cl.uchile.dcc.finalreality.controller.weaponEquipTransition
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.function.Predicate

class WeaponEquipStateTest : FunSpec({
     val dummyController = GameController()
     val thisState1 = WeaponEquipState(dummyController,NonMagicalPlayerTurnState(dummyController))
     val thisState2 = WeaponEquipState(dummyController,MagicalPlayerTurnState(dummyController))
     val validTransitions1: List<(GameState) -> Unit> = listOf(
         nonMagicalPlayerTurnTransition
     )
     val validTransitions2: List<(GameState) -> Unit> = listOf(
         magicalPlayerTurnTransition
     )
     val thisQuestion = GameState::isWeaponEquip
     val otherQuestions = stateQuestions.toMutableList()
     otherQuestions.remove(thisQuestion)

     val invalidTransitions1: MutableList<(GameState) -> Unit> = stateTransitions.toMutableList()
     val predicate1 = Predicate { transition: (GameState) -> Unit -> validTransitions1.contains(transition) }
     invalidTransitions1.removeIf(predicate1)

     val invalidTransitions2: MutableList<(GameState) -> Unit> = stateTransitions.toMutableList()
     val predicate2 = Predicate { transition: (GameState) -> Unit -> validTransitions2.contains(transition) }
     invalidTransitions2.removeIf(predicate2)
     context("A WeaponEquipState should") {
         test("Be able to do valid transitions") {
             validTransitionCheck(thisState1, validTransitions1)
             validTransitionCheck(thisState2, validTransitions2)
         }
         test("Be unable to do invalid transitions") {
             invalidTransitionCheck(thisState1, invalidTransitions1)
             invalidTransitionCheck(thisState2, invalidTransitions2)
         }
         test("Answer correctly when asked who they are") {
             falseQuestionsCheck(thisState1, otherQuestions)
             thisQuestion(thisState1) shouldBe true
         }
     }
 })