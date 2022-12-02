package cl.uchile.dcc.finalreality.controller

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GameControllerTest : FunSpec({
    lateinit var game: GameController
    ControllerRNGSeed.setSeed(6)
    context("A GameController should") {
        game = GameController()
        test("Generate enemies when instantiated") {
            // The given seed should generate exactly these enemies
            val expectedEnemyNames: List<String> = listOf(
                "Mutant",
                "Were-wolf",
                "Hooded stabber",
                "Red panda",
                "Basilisk"
            )
            game.getEnemyNames() shouldBe expectedEnemyNames
            game.gameState.isCharacterCreation() shouldBe true
        }
        test("Allow only 5 characters to be created and start a fight when all 5 are present") {
            game.startBattle() shouldBe false
            game.createEngineer("TestEngineer") shouldBe true
            game.startBattle() shouldBe false
            game.createThief("TestThief") shouldBe true
            game.startBattle() shouldBe false
            game.createKnight("TestKnight") shouldBe true
            game.startBattle() shouldBe false
            game.createWhiteMage("TestWhiteMage") shouldBe true
            game.startBattle() shouldBe false
            game.createBlackMage("TestBlackMage") shouldBe true
            game.startBattle() shouldBe true
        }
    }
})
