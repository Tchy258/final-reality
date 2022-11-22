package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Engineer
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.enemyAttackTest
import cl.uchile.dcc.finalreality.model.enemyInequalityCheck
import cl.uchile.dcc.finalreality.model.enemyValidStatCheck
import cl.uchile.dcc.finalreality.model.equalityCheck
import cl.uchile.dcc.finalreality.model.hpDecreaseCheck
import cl.uchile.dcc.finalreality.model.hpIncreaseCheck
import cl.uchile.dcc.finalreality.model.notNullCheck
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.Integer.max
import java.util.concurrent.LinkedBlockingQueue

class EnemyTest : FunSpec({
    lateinit var queue: LinkedBlockingQueue<GameCharacter>
    lateinit var enemy1: Enemy
    lateinit var enemy2: Enemy
    lateinit var enemy3: Enemy
    lateinit var enemy4: Enemy

    beforeEach {
        queue = LinkedBlockingQueue<GameCharacter>()
        enemy1 = Enemy("TestEnemy", 15, 10, 12, 5, queue)
        enemy2 = Enemy("TestEnemy", 15, 10, 12, 5, queue)
        enemy3 = Enemy("TestEnemy2", 18, 20, 7, 2, queue)
        enemy4 = Enemy("LightEnemy", 5, 5, 10, 1, queue)
    }
    context("Two enemies with the same parameters should:") {
        test("Be equal") {
            equalityCheck(EnemyData.validGenerator, EnemyTestingFactory(queue))
            enemy1 shouldBe enemy2
        }
        test("Have the same hashcode") {
            enemy1.hashCode() shouldBe enemy2.hashCode()
        }
    }
    context("Two enemies with different parameters should:") {
        test("Not be equal") {
            enemyInequalityCheck(EnemyTestingFactory(queue))
            enemy1 shouldNotBe enemy3
            enemy2 shouldNotBe enemy4
        }
    }
    context("Any Enemy should:") {
        test("Have a string representation") {
            enemy1.toString() shouldBe "Enemy { name:'TestEnemy', damage: 15, weight: 10, maxHp: 12, defense: 5, currentHp: 12 }"
            enemy2.toString() shouldBe "Enemy { name:'TestEnemy', damage: 15, weight: 10, maxHp: 12, defense: 5, currentHp: 12 }"
            enemy3.toString() shouldBe "Enemy { name:'TestEnemy2', damage: 18, weight: 20, maxHp: 7, defense: 2, currentHp: 7 }"
            enemy4.toString() shouldBe "Enemy { name:'LightEnemy', damage: 5, weight: 5, maxHp: 10, defense: 1, currentHp: 10 }"
        }
        test("Not be null") {
            notNullCheck(EnemyData.validGenerator, EnemyTestingFactory(queue))
            enemy1 shouldNotBe null
            enemy2 shouldNotBe null
            enemy3 shouldNotBe null
            enemy4 shouldNotBe null
        }
        test("Have valid stats") {
            enemyValidStatCheck()
            assertThrows<InvalidStatValueException> {
                Enemy("", -1, -1, -1, -1, queue)
            }
            assertDoesNotThrow {
                Enemy("", 1, 1, 1, 1, queue)
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            hpDecreaseCheck(EnemyData.validGenerator, EnemyTestingFactory(queue))
        }
        test("Not be able to have more current hp than its maxHp") {
            hpIncreaseCheck(EnemyData.validGenerator, EnemyTestingFactory(queue))
        }
        // This test only tries with fixed weight values, other characters try with random values
        test("Be able to join the turns queue and take its turn when it should") {
            enemy1.waitTurn()
            enemy2.waitTurn()
            enemy3.waitTurn()
            enemy4.waitTurn()
            eventually {
                assert(queue.isNotEmpty())
                queue.size shouldBe 4
            }
            queue.poll() shouldBe enemy4
            val either1or2: Enemy = queue.poll() as Enemy
            assert(either1or2 === enemy1 || either1or2 === enemy2)
            queue.poll() // Take out the other one with the same weight
            queue.poll() shouldBe enemy3
        }
        test("Be able to attack other characters") {
            enemyAttackTest(queue)
            val randomCharacters: List<GameCharacter> = listOf(
                enemy2,
                Engineer("TestCharacter", 20, 5, queue),
                BlackMage("TestMage", 10, 10, 1, queue),
                Knight("TestCharacter", 30, 10, queue),
                Thief("TestCharacter", 15, 2, queue),
                WhiteMage("TestMage", 10, 10, 0, queue),
            )
            for (gameCharacter in randomCharacters) {
                gameCharacter.currentHp shouldBe gameCharacter.maxHp
                enemy1.attack(gameCharacter)
                gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
                gameCharacter.currentHp shouldBe max(0, gameCharacter.maxHp - (enemy1.damage - gameCharacter.defense))
            }
        }
    }
})
