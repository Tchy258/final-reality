package cl.uchile.dcc.finalreality.model.character.player.classes

import cl.uchile.dcc.finalreality.model.character.player.weapon.Axe
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class PlayerCharacterTurnTest: FunSpec ({
    lateinit var character1: PlayerCharacter
    lateinit var character2: PlayerCharacter
    lateinit var character3: PlayerCharacter
    lateinit var character4: PlayerCharacter
    lateinit var character5: PlayerCharacter
    test("PlayerCharacters should have their turns according to their weapons' weights") {
        checkAll(
            genA = Arb.positiveInt(10),
            genB = Arb.positiveInt(10),
            genC = Arb.positiveInt(10),
            genD = Arb.positiveInt(10),
            genE = Arb.positiveInt(10)
        ) {weight1, weight2, weight3, weight4, weight5 ->
            val weapon1 = Axe("an Axe",5,weight1)
            //val weapon2 = Bow("a Bow")

        }
    }
})