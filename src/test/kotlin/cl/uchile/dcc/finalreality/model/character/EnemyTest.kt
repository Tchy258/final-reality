package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.character.EnemyData.Companion.arbitraryEnemyGenerator
import cl.uchile.dcc.finalreality.model.character.EnemyData.Companion.validEnemyGenerator
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
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
            checkAll(validEnemyGenerator) { enemy ->
                val randomEnemy1 = Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
                val randomEnemy2 = Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
                randomEnemy1 shouldBe randomEnemy2
            }
            enemy1 shouldBe enemy2
        }
        test("Have the same hashcode") {
            enemy1.hashCode() shouldBe enemy2.hashCode()
        }
    }
    context("Two enemies with different parameters should:") {
        test("Not be equal") {
            checkAll(
                genA = validEnemyGenerator,
                genB = validEnemyGenerator
            ) { enemy1, enemy2 ->
                assume(
                    enemy1.name != enemy2.name ||
                        enemy1.damage != enemy2.damage ||
                        enemy1.maxHp != enemy2.maxHp ||
                        enemy1.defense != enemy2.defense ||
                        enemy1.weight != enemy2.weight
                )
                val randomEnemy1 = Enemy(enemy1.name, enemy1.damage, enemy1.weight, enemy1.maxHp, enemy1.defense, queue)
                val randomEnemy2 = Enemy(enemy2.name, enemy2.damage, enemy2.weight, enemy2.maxHp, enemy2.defense, queue)
                randomEnemy1 shouldNotBe randomEnemy2
            }
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
            checkAll(validEnemyGenerator) { enemy ->
                val randomEnemy = Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
                randomEnemy shouldNotBe null
            }
            enemy1 shouldNotBe null
            enemy2 shouldNotBe null
            enemy3 shouldNotBe null
            enemy4 shouldNotBe null
        }
        test("Have valid stats") {
            checkAll(arbitraryEnemyGenerator) {
                enemy ->
                if (enemy.maxHp <= 0 || enemy.defense < 0 || enemy.damage <0 || enemy.weight <= 0) {
                    assertThrows<InvalidStatValueException> {
                        Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
                    }
                } else {
                    assertDoesNotThrow {
                        Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
                    }
                }
            }
            assertThrows<InvalidStatValueException> {
                Enemy("", -1, -1, -1, -1, queue)
            }
            assertDoesNotThrow {
                Enemy("", 1, 1, 1, 1, queue)
            }
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = validEnemyGenerator,
                genB = Arb.positiveInt()
            ) { enemy, randomDamage ->
                val randomEnemy = Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
                randomEnemy.currentHp shouldBe enemy.maxHp
                if (randomDamage> enemy.maxHp) {
                    assertThrows<InvalidStatValueException> {
                        randomEnemy.currentHp -= randomDamage
                    }
                } else {
                    assertDoesNotThrow {
                        randomEnemy.currentHp -= randomDamage
                    }
                    randomEnemy.currentHp shouldNotBe enemy.maxHp
                    randomEnemy.currentHp shouldBeGreaterThanOrEqualTo 0
                }
            }
        }
        test("Not be able to have more current hp than its maxHp") {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 55),
                genA = validEnemyGenerator,
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt()
            ) {enemy, randomHealing, randomDamage ->
                assume {
                    randomDamage shouldBeLessThanOrEqual enemy.maxHp
                }
                val randomEnemy = Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
                randomEnemy.currentHp shouldBe enemy.maxHp
                randomEnemy.currentHp -= randomDamage
                randomEnemy.currentHp shouldNotBe enemy.maxHp
                if (randomHealing > randomEnemy.maxHp - randomEnemy.currentHp) {
                    assertThrows<InvalidStatValueException> {
                        randomEnemy.currentHp += randomHealing
                    }
                }
                else {
                    assertDoesNotThrow {
                        randomEnemy.currentHp += randomHealing
                    }
                    randomEnemy.currentHp shouldBeLessThanOrEqual randomEnemy.maxHp
                    randomEnemy.currentHp shouldBeGreaterThan 0
                }
            }
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
    }
})
