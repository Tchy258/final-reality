package cl.uchile.dcc.finalreality.model.character.player.classes.physical

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import java.util.concurrent.LinkedBlockingQueue

class EngineerTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var engineer1: Engineer
    lateinit var engineer2: Engineer
    lateinit var engineer3: Engineer

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        engineer1 = Engineer("TestEngineer", 15, 10, queue)
        engineer2 = Engineer("TestEngineer", 15, 10, queue)
        engineer3 = Engineer("TestEngineer2", 18, 11, queue)
    }
    context("Two engineers with the same parameters should") {
        test("Be equal") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.nonNegativeInt()
            ) { name, maxHp, defense ->
                val randomEngineer1 = Engineer(name, maxHp, defense, queue)
                val randomEngineer2 = Engineer(name, maxHp, defense, queue)
                randomEngineer1 shouldBe randomEngineer2
            }
            engineer1 shouldBe engineer2
        }
    }
})
