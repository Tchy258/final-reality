package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import io.kotest.core.spec.style.FunSpec

class GameStateTest : FunSpec({
    lateinit var controller: GameController
    beforeEach {
        controller = GameController()
    }
    context("All game states should") {
        test("Be able to do valid transitions") {

        }
        test("Execute the delegating methods") {

        }
    }

})
