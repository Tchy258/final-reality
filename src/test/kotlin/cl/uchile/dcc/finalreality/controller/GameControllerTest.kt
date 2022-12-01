package cl.uchile.dcc.finalreality.controller

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.Scanner

class GameControllerTest : FunSpec({
    lateinit var game: GameController

    context("When a GameController is created, it should") {
        test("Request the names of 5 characters") {
            val str = "TestCharacter1\nTestCharacter2\nTestCharacter3\nTestCharacter4\nTestCharacter5\n"
            game = GameController(Scanner(str))
            game.isGameOver() shouldBe false
        }
    }
})
