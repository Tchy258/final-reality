package cl.uchile.dcc.finalreality.model.character.player.classes

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.character.player.weapon.Axe
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.exhaustive
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

fun knightGenerator(queue: LinkedBlockingQueue<GameCharacter>) {
    arbitrary {
        val name = Arb.string().bind()
        val maxHp = Arb.positiveInt().bind()
        val defense = Arb.nonNegativeInt().bind()
        Knight(name,maxHp,defense,queue)
    }
}

fun thiefGenerator(queue: LinkedBlockingQueue<GameCharacter>) {
    arbitrary {
        val name = Arb.string().bind()
        val maxHp = Arb.positiveInt().bind()
        val defense = Arb.nonNegativeInt().bind()
        Thief(name,maxHp,defense,queue)
    }
}
fun engineerGenerator(queue: LinkedBlockingQueue<GameCharacter>) {
    arbitrary {
        val name = Arb.string().bind()
        val maxHp = Arb.positiveInt().bind()
        val defense = Arb.nonNegativeInt().bind()
        Engineer(name,maxHp,defense,queue)
    }
}
fun whiteMageGenerator(queue: LinkedBlockingQueue<GameCharacter>) {
    arbitrary {
        val name = Arb.string().bind()
        val maxHp = Arb.positiveInt().bind()
        val maxMp = Arb.positiveInt().bind()
        val defense = Arb.nonNegativeInt().bind()
        WhiteMage(name,maxHp, maxMp, defense,queue)
    }
}
fun blackMageGenerator(queue: LinkedBlockingQueue<GameCharacter>) {
    arbitrary {
        val name = Arb.string().bind()
        val maxHp = Arb.positiveInt().bind()
        val maxMp = Arb.positiveInt().bind()
        val defense = Arb.nonNegativeInt().bind()
        BlackMage(name,maxHp, maxMp, defense,queue)
    }
}
class PlayerCharacterTurnTest: FunSpec ({
    lateinit var character1: PlayerCharacter
    lateinit var character2: PlayerCharacter
    lateinit var character3: PlayerCharacter
    lateinit var character4: PlayerCharacter
    lateinit var character5: PlayerCharacter
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
    }
    test("PlayerCharacters should have their turns according to their weapons' weights") {
        checkAll(
            genA = Arb.positiveInt(10),
            genB = Arb.positiveInt(10),
            genC = Arb.positiveInt(10),
            genD = Arb.positiveInt(10),
            genE = Arb.positiveInt(10),
            genF = listOfCharacters,
            genG = listOfCharacters,
            genH = listOfCharacters,
            genI = listOfCharacters,
            genJ = listOfCharacters
        ) {weight1, weight2, weight3, weight4, weight5,
           character1, character2, character3, character4, character5 ->
            val weapon1 = Axe("an Axe",5,weight1)
            //val weapon2 = Bow("a Bow")

        }
    }
})