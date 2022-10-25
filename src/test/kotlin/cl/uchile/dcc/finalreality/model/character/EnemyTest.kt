package cl.uchile.dcc.finalreality.model.character

import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.nonNegativeInt()
            ) { name, damage, weight, maxHp, defense ->
                val randomEnemy1 = Enemy(name, damage, weight, maxHp, defense, queue)
                val randomEnemy2 = Enemy(name, damage, weight, maxHp, defense, queue)
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
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.string(),
                genG = Arb.positiveInt(),
                genH = Arb.positiveInt(),
                genI = Arb.positiveInt(),
                genJ = Arb.nonNegativeInt()
            ) { name1, damage1, weight1, maxHp1, defense1, name2, damage2, weight2, maxHp2, defense2 ->
                assume(
                    name1 != name2 ||
                        damage1 != damage2 ||
                        maxHp1 != maxHp2 ||
                        defense1 != defense2 ||
                        weight1 != weight2
                )
                val randomEnemy1 = Enemy(name1, damage1, weight1, maxHp1, defense1, queue)
                val randomEnemy2 = Enemy(name2, damage2, weight2, maxHp2, defense2, queue)
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
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.nonNegativeInt()
            ) { name, damage, weight, maxHp, defense ->
                val randomEnemy = Enemy(name, damage, weight, maxHp, defense, queue)
                randomEnemy shouldNotBe null
            }
            enemy1 shouldNotBe null
            enemy2 shouldNotBe null
            enemy3 shouldNotBe null
            enemy4 shouldNotBe null
        }
        test("Be able to have its currentHp changed to non-negative values") {
            checkAll(
                genA = Arb.string(),
                genB = Arb.positiveInt(),
                genC = Arb.positiveInt(),
                genD = Arb.positiveInt(),
                genE = Arb.nonNegativeInt(),
                genF = Arb.positiveInt()
            ) { name, damage, weight, maxHp, defense, randomDamage ->
                val randomEnemy = Enemy(name, damage, weight, maxHp, defense, queue)
                randomEnemy.currentHp shouldBe maxHp
                randomEnemy.currentHp = Integer.max(0, randomEnemy.currentHp - randomDamage)
                randomEnemy.currentHp shouldNotBe maxHp
                randomEnemy.currentHp shouldBeGreaterThanOrEqualTo 0
            }
        }
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
