package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.magic.blackmagic.RNGSeeder
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Poison
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.concurrent.LinkedBlockingQueue

class PoisonedTest : FunSpec({
    lateinit var poisonEffect1: Poisoned
    lateinit var poisonEffect2: Poisoned
    lateinit var poisonEffect3: Poisoned
    lateinit var character: PlayerCharacter
    lateinit var mage: WhiteMage
    lateinit var enemy1: Enemy
    lateinit var enemy2: Enemy
    lateinit var queue: LinkedBlockingQueue<GameCharacter>

    beforeEach {
        poisonEffect1 = Poisoned(10)
        poisonEffect2 = Poisoned(10)
        poisonEffect3 = Poisoned(20)
        queue = LinkedBlockingQueue()
        character = Knight("TestKnight", 100, 10, queue)
        mage = WhiteMage("TestMage", 100, 50, 10, queue)
        character.addDebuff(poisonEffect1)
        enemy1 = Enemy("TestEnemy1", 20, 10, 100, 2, queue)
        enemy2 = Enemy("TestEnemy2", 20, 10, 100, 2, queue)
        enemy2.addDebuff(poisonEffect3)
        RNGSeeder.setSeed(10)
    }
    context("Poisoned adverse effects should") {
        test("Have a string representation") {
            poisonEffect1.toString() shouldBe "Poisoned { finalDamage: 10, turnLimit: 5 }"
            poisonEffect2.toString() shouldBe "Poisoned { finalDamage: 10, turnLimit: 5 }"
            poisonEffect3.toString() shouldBe "Poisoned { finalDamage: 20, turnLimit: 5 }"
        }
        test("Always be equal to each other") {
            poisonEffect1 shouldBe poisonEffect1
            poisonEffect1 shouldBe poisonEffect2
            poisonEffect1 shouldBe poisonEffect3
            poisonEffect2 shouldBe poisonEffect3
        }
        test("Not be equal to other adverse effects") {
            val paralyzed = Paralyzed()
            val burned = Burned(10)
            poisonEffect1 shouldNotBe paralyzed
            poisonEffect1 shouldNotBe burned
        }
    }
    test("When a character is poisoned, they should receive damage upon attacking (using a turn)") {
        val testSword = Sword("TestSword", 10, 5)
        character.equip(testSword)
        character.rollEffects()
        character.attack(enemy1)
        character.currentHp shouldBe character.maxHp - 10
        enemy2.rollEffects()
        enemy2.attack(character)
        enemy2.currentHp shouldBe enemy2.maxHp - 20
    }
    context("When a white mage casts poison") {
        test("The target should remain poisoned if it already was, and the duration hasn't expired") {
            character.isPoisoned() shouldBe true
            val testStaff = Staff("TestStaff", 10, 10, 20)
            mage.equip(testStaff)
            mage.castWhiteMagicSpell(Poison(), character)
            character.isPoisoned() shouldBe true
            mage.castWhiteMagicSpell(Poison(), enemy2)
            enemy2.isPoisoned() shouldBe true
        }
        test("A non poisoned target will be poisoned") {
            // The given seed ensures the enemy will be poisoned on the third cast
            val testStaff = Staff("TestStaff", 10, 10, 20)
            mage.equip(testStaff)
            mage.castWhiteMagicSpell(Poison(), enemy1)
            enemy1.isPoisoned() shouldBe true
        }
    }
    test("When 5 turns have passed, the poisoned adverse effect should wear off") {
        val testSword = Sword("ReallyWeakSword", 1, 10)
        character.equip(testSword)
        for (i in 0..3) {
            character.rollEffects()
            character.attack(enemy1)
            character.isPoisoned() shouldBe true
        }
        character.rollEffects()
        character.attack(enemy1)
        character.isPoisoned() shouldNotBe true
    }
})
