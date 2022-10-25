package cl.uchile.dcc.finalreality.model.character.player.classes

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData.Companion.validCharacterGenerator
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.character.player.weapon.Axe
import cl.uchile.dcc.finalreality.model.character.player.weapon.Bow
import cl.uchile.dcc.finalreality.model.character.player.weapon.Knife
import cl.uchile.dcc.finalreality.model.character.player.weapon.Staff
import cl.uchile.dcc.finalreality.model.character.player.weapon.Sword
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import java.util.concurrent.LinkedBlockingQueue

class PlayerCharacterTurnTest : FunSpec({
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
            genF = validCharacterGenerator,
            genG = validCharacterGenerator,
            genH = validCharacterGenerator,
            genI = validCharacterGenerator,
            genJ = validCharacterGenerator,
            iterations = 10 // Or else this test takes 15 minutes
        ) { weight1, weight2, weight3, weight4, weight5,
            character1, character2, character3, character4, character5 ->
            val weapon1 = Axe("an Axe", 5, weight1)
            val weapon2 = Bow("a Bow", 5, weight2)
            val weapon3 = Knife("a Knife", 5, weight3)
            val weapon4 = Staff("a Knife", 5, weight4, 5)
            val weapon5 = Sword("a Sword", 5, weight5)
            val knight = Knight(character1.name, character1.maxHp, character1.defense, queue)
            val engineer = Engineer(character2.name, character2.maxHp, character2.defense, queue)
            val thief = Thief(character3.name, character3.maxHp, character3.defense, queue)
            val whiteMage = WhiteMage(
                character4.name,
                character4.maxHp,
                character4.maxMp,
                character4.defense,
                queue
            )
            val blackMage = BlackMage(
                character5.name,
                character5.maxHp,
                character5.maxMp,
                character5.defense,
                queue
            )

            knight.equip(weapon5)
            engineer.equip(weapon1)
            thief.equip(weapon2)
            whiteMage.equip(weapon4)
            blackMage.equip(weapon3)

            val weightMap = hashMapOf<PlayerCharacter, Int>()
            weightMap[knight] = weight1
            weightMap[engineer] = weight2
            weightMap[thief] = weight3
            weightMap[whiteMage] = weight4
            weightMap[blackMage] = weight5
            // Characters are sorted according to their weapons' weight
            val sortedMap = weightMap.toList().sortedBy { (_, value) -> value }.toMap()

            knight.waitTurn()
            engineer.waitTurn()
            thief.waitTurn()
            whiteMage.waitTurn()
            blackMage.waitTurn()

            eventually {
                assert(!queue.isEmpty())
                queue.size shouldBe 5
            }
            for (character in sortedMap) {
                // The character at the head of the queue is either: the next character on the map
                // or a character whose weapon has the same weight
                val current = queue.poll() as PlayerCharacter
                assert(current == character || current.equippedWeapon.weight == character.value)
            }
        }
    }
})
